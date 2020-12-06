package com.skew.bitstampclient.messages;

import lombok.Data;
import org.slf4j.LoggerFactory;

@Data
public class Message {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Message.class);

    private String event;
    private String channel;
    private Object data;

    public String getPair() {

        if (channel == null || 
        		!channel.startsWith("order_book_") ||
        		!channel.startsWith("order_book_") ||
        		!channel.startsWith("live_trades_")) {
            return "";
        }

        String[] split = channel.split("_");
        return split[2];
    }
}
