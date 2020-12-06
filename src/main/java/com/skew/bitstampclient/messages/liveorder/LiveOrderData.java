package com.skew.bitstampclient.messages.liveorder;

import lombok.Data;

@Data
public class LiveOrderData {

    private long id;
    private double amount;
    private String amount_str;
    private double price;
    private String price_str;
    private int order_type;
    private String datetime;
    private long microtimestamp;
}
