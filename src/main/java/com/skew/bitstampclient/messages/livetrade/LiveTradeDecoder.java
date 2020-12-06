package com.skew.bitstampclient.messages.livetrade;

import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class LiveTradeDecoder implements Decoder.Text<LiveTrade> {

    private static Gson gson = new Gson();

    @Override
    public LiveTrade decode(String s) throws DecodeException {
        return gson.fromJson(s, LiveTrade.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
