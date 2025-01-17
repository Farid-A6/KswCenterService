package com.wits.pms.mcu.custom;

import android.telephony.SmsManager;
import android.util.Log;
import com.android.internal.midi.MidiConstants;
import com.wits.pms.mcu.McuService;
import java.io.PrintStream;

public abstract class KswMcuReceiver implements McuService.OnReceiveData {
    private static byte[] bytes;
    private byte[] currentPack;
    private int dataIndex;
    private byte[] realPack;
    private boolean recvHead;
    private int recvHeadIndex;

    public abstract void onMcuMessage(byte[] bArr);

    public static void main(String... a) {
        PrintStream printStream = System.out;
        printStream.println("-----" + (((float) (((41 & 255) << 8) + (14 & 255))) / 100.0f));
    }

    public void reset() {
        this.dataIndex = 0;
        this.currentPack = null;
        this.realPack = null;
        this.recvHead = false;
    }

    public void onReceiveMcu(byte[] packs) {
        int i = 0;
        while (i < packs.length) {
            try {
                int data = packs[i] & 255;
                if (this.recvHead) {
                    if (this.currentPack != null) {
                        this.currentPack[this.dataIndex] = (byte) data;
                        if (this.dataIndex == 3) {
                            this.realPack = new byte[(packs[i] + 5)];
                        }
                        if (this.realPack != null && this.dataIndex == this.realPack.length - 1) {
                            System.arraycopy(this.currentPack, 0, this.realPack, 0, this.realPack.length);
                            onMcuMessage(this.realPack);
                            this.currentPack = null;
                            this.realPack = null;
                            this.recvHead = false;
                        }
                    }
                } else if (!this.recvHead && data == 242) {
                    this.dataIndex = 0;
                    this.recvHead = true;
                    this.currentPack = new byte[128];
                    this.currentPack[0] = MidiConstants.STATUS_SONG_POSITION;
                }
                this.dataIndex++;
                i++;
            } catch (Exception e) {
                reset();
                printHex("onDataReceived McuException", packs);
                Log.e("McuService", "McuException", e);
                return;
            }
        }
    }

    public static void printHex(String method, byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append("-----[");
        for (byte b : data) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append("0x");
            sb.append(hex.toUpperCase());
            sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]\n");
        Log.v("KswMcuMessage", sb.toString());
    }
}
