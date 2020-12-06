package com.skew.bitstampclient.messages.liveorderbook;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class LiveOrderBookDecoder implements Decoder.Text<LiveOrderBook>{
	
	private static Gson gson = new Gson();
	
	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LiveOrderBook decode(String s) throws DecodeException {
		// TODO Auto-generated method stub
		return gson.fromJson(s, LiveOrderBook.class);
	}

	@Override
	public boolean willDecode(String s) {
		// TODO Auto-generated method stub
		return (s != null);
	}
	

}
