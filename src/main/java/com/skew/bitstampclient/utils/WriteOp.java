package com.skew.bitstampclient.utils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;

public class WriteOp implements CompletionHandler<Integer, AsynchronousFileChannel> {

    private final ByteBuffer buf;
    private long             position;

    WriteOp( ByteBuffer buf, long position) {
        this.buf = buf;
        this.position = position;
    }

    @Override
    public void completed( Integer result, AsynchronousFileChannel channel) {
        if (buf.hasRemaining()) { // incomplete write
            position += result;
            channel.write(buf, position, channel, this);
        }
    }

    @Override
    public void failed( Throwable ex, AsynchronousFileChannel channel) {
        // ?
    }
}
