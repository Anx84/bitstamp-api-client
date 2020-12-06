package com.skew.bitstampclient.messages.subscription;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Subscription {

    private String event;
    private String channel;
    private SubscriptionData data;

}
