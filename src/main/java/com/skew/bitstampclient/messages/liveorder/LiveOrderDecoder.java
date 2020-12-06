package com.skew.bitstampclient.messages.liveorder;

import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class LiveOrderDecoder implements Decoder.Text<LiveOrder> {

    private static Gson gson = new Gson();

    @Override
    public LiveOrder decode(String s) throws DecodeException {
        return gson.fromJson(s, LiveOrder.class);
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
