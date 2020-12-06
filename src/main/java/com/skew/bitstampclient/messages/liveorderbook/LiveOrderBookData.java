package com.skew.bitstampclient.messages.liveorderbook;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LiveOrderBookData {
	
	private Long timestamp;
	private Long microtimestamp;
	private List<List<String>> bids = new ArrayList<List<String>>();
	private List<List<String>> asks = new ArrayList<List<String>>();
}
