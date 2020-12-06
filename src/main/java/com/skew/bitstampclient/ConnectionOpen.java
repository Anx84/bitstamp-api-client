package com.skew.bitstampclient;

import java.util.ArrayList;
import java.util.List;

import com.skew.bitstampclient.dataprocessors.LiveOrderBookDataProcessor;
import com.skew.bitstampclient.observers.ConnectionOpenObserver;

public class ConnectionOpen implements ConnectionOpenObserver {
	private List<String> pairs = new ArrayList<>();
	
	public ConnectionOpen() {
		pairs.add("btcusd");
	}
	
	public ConnectionOpen(List<String> pairs) {
		// TODO Auto-generated constructor stub
		this.pairs = pairs;
	}
	
    @Override
    public void receive() {

        // Connection is open, now we can start subscribing to channels:
    	pairs.forEach( p -> {
    		BitstampConnectorEndpoint.onOrderBook(p, new LiveOrderBookDataProcessor());

    	});
    }
}
