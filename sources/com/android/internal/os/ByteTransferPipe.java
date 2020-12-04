package com.android.internal.os;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;

public class ByteTransferPipe extends TransferPipe {
    static final String TAG = "ByteTransferPipe";
    private ByteArrayOutputStream mOutputStream;

    public ByteTransferPipe() throws IOException {
    }

    public ByteTransferPipe(String bufferPrefix) throws IOException {
        super(bufferPrefix, TAG);
    }

    /* access modifiers changed from: protected */
    public OutputStream getNewOutputStream() {
        this.mOutputStream = new ByteArrayOutputStream();
        return this.mOutputStream;
    }

    public byte[] get() throws IOException {
        go((FileDescriptor) null);
        return this.mOutputStream.toByteArray();
    }
}
