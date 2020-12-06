package com.skew.bitstampclient.messages.liveorderbook;

import java.util.List;

import lombok.Data;

@Data
public class NormalizedBitstampOrderBook {
	
	private String exchange;
	private String symbol;
	
	private List<BitstampTrade> bids;
	private List<BitstampTrade> asks;
	
	
}
