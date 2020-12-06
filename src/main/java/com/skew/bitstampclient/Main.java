package com.skew.bitstampclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.DeploymentException;

public class Main {
	public static void main(String[] args) {
		List<String> pairs = new ArrayList<>();
		
		if(args.length<2) {
			System.out.println("Not enough arguments specified.\n "
					+ "Example usage: jarfile [runtime seconds] [pair1 (btcusd)] [pair2 (xrpusd)] ...");
			return;
		}
		for(int i=1; i< args.length; i++) {
			pairs.add(args[i]);
		}
		
		BitstampConnectorEndpoint.onOpen(new ConnectionOpen(pairs));
    	CountDownLatch count = new CountDownLatch(1);
    	
    	try {
    		BitstampConnector.connect();
    		count.await(Long.parseLong(args[0]), TimeUnit.SECONDS);
    		BitstampConnector.disconnect();
    		
    	}catch (InterruptedException | DeploymentException | IOException e) {
    		e.printStackTrace();
    	}
	}
}
