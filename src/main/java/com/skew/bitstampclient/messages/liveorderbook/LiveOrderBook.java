package com.skew.bitstampclient.messages.liveorderbook;

import lombok.Data;

@Data
public class LiveOrderBook {
	
	private LiveOrderBookData data;
	private String event;
	private String channel;
}
