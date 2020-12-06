package com.skew.bitstampclient.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.concurrent.atomic.AtomicLong;

public class AsyncAppender {

    private final AsynchronousFileChannel channel;
    /** Where new append operations are told to start writing. */
    private final AtomicLong              projectedSize;

    public AsyncAppender( AsynchronousFileChannel channel) throws IOException {
        this.channel = channel;
        this.projectedSize = new AtomicLong(channel.size());
    }

    public void append( ByteBuffer buf) throws IOException {
        final int buflen = buf.remaining();
        long size;
        do {
            size = projectedSize.get();
        } while (!projectedSize.compareAndSet(size, size + buflen));

        channel.write(buf, channel.size(), channel, new WriteOp(buf, size));
    }
}