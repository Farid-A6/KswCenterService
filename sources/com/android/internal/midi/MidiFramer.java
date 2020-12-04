package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import java.io.IOException;

public class MidiFramer extends MidiReceiver {
    public String TAG = "MidiFramer";
    private byte[] mBuffer = new byte[3];
    private int mCount;
    private boolean mInSysEx;
    private int mNeeded;
    private MidiReceiver mReceiver;
    private byte mRunningStatus;

    public MidiFramer(MidiReceiver receiver) {
        this.mReceiver = receiver;
    }

    public static String formatMidiData(byte[] data, int offset, int count) {
        String text = "MIDI+" + offset + " : ";
        for (int i = 0; i < count; i++) {
            text = text + String.format("0x%02X, ", new Object[]{Byte.valueOf(data[offset + i])});
        }
        return text;
    }

    public void onSend(byte[] data, int offset, int count, long timestamp) throws IOException {
        int i;
        int offset2 = offset;
        int sysExStartOffset = this.mInSysEx ? offset : -1;
        for (int i2 = 0; i2 < count; i2++) {
            byte currentByte = data[offset2];
            int currentInt = currentByte & 255;
            if (currentInt >= 128) {
                if (currentInt < 240) {
                    this.mRunningStatus = currentByte;
                    this.mCount = 1;
                    this.mNeeded = MidiConstants.getBytesPerMessage(currentByte) - 1;
                } else if (currentInt < 248) {
                    if (currentInt == 240) {
                        this.mInSysEx = true;
                        i = offset2;
                    } else if (currentInt != 247) {
                        this.mBuffer[0] = currentByte;
                        this.mRunningStatus = 0;
                        this.mCount = 1;
                        this.mNeeded = MidiConstants.getBytesPerMessage(currentByte) - 1;
                    } else if (this.mInSysEx) {
                        this.mReceiver.send(data, sysExStartOffset, (offset2 - sysExStartOffset) + 1, timestamp);
                        this.mInSysEx = false;
                        i = -1;
                    }
                    sysExStartOffset = i;
                } else {
                    if (this.mInSysEx) {
                        this.mReceiver.send(data, sysExStartOffset, offset2 - sysExStartOffset, timestamp);
                        sysExStartOffset = offset2 + 1;
                    }
                    this.mReceiver.send(data, offset2, 1, timestamp);
                }
            } else if (!this.mInSysEx) {
                byte[] bArr = this.mBuffer;
                int i3 = this.mCount;
                this.mCount = i3 + 1;
                bArr[i3] = currentByte;
                int i4 = this.mNeeded - 1;
                this.mNeeded = i4;
                if (i4 == 0) {
                    if (this.mRunningStatus != 0) {
                        this.mBuffer[0] = this.mRunningStatus;
                    }
                    this.mReceiver.send(this.mBuffer, 0, this.mCount, timestamp);
                    this.mNeeded = MidiConstants.getBytesPerMessage(this.mBuffer[0]) - 1;
                    this.mCount = 1;
                }
            }
            offset2++;
        }
        if (sysExStartOffset >= 0 && sysExStartOffset < offset2) {
            this.mReceiver.send(data, sysExStartOffset, offset2 - sysExStartOffset, timestamp);
        }
    }
}
