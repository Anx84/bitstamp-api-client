package com.skew.bitstampclient.messages.subscription;

import org.slf4j.LoggerFactory;

import com.skew.bitstampclient.BitstampConnector;

import javax.websocket.EncodeException;
import java.io.IOException;


public class SubscriptionMessage {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SubscriptionMessage.class);

    public void subscribeChannel(String channel) throws IOException {

        String json = messageAsJson("bts:subscribe", channel);
        BitstampConnector.getSession().getBasicRemote().sendText(json);
    }

    public void unsubscribeChannel(String channel) throws IOException {

        String json = messageAsJson("bts:unsubscribe", channel);
        BitstampConnector.getSession().getBasicRemote().sendText(json);
    }

    private String messageAsJson(String event, String channel) {

        Subscription message = new Subscription();
        message.setEvent(event);

        SubscriptionData data = new SubscriptionData();
        data.setChannel(channel);

        message.setData(data);

        SubscriptionEncoder encoder = new SubscriptionEncoder();
        try {

            return encoder.encode(message);

        } catch (EncodeException e) {
            LOG.error("Error processing message", e);
        }

        return "";
    }

}
