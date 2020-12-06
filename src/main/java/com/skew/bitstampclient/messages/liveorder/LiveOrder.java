package com.skew.bitstampclient.messages.liveorder;

import lombok.Data;

@Data
public class LiveOrder {

    private LiveOrderData data;
    private String event;
    private String channel;

}
