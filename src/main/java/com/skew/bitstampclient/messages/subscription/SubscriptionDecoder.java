package com.skew.bitstampclient.messages.subscription;

import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class SubscriptionDecoder implements Decoder.Text<Subscription> {

    private static Gson gson = new Gson();

    @Override
    public Subscription decode(String s) throws DecodeException {
        return gson.fromJson(s, Subscription.class);
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
