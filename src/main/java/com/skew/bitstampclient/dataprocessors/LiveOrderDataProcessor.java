package com.skew.bitstampclient.dataprocessors;

import org.slf4j.LoggerFactory;

import com.skew.bitstampclient.messages.liveorder.LiveOrder;
import com.skew.bitstampclient.messages.liveorder.LiveOrderData;
import com.skew.bitstampclient.observers.LiveOrderObserver;

public class LiveOrderDataProcessor implements LiveOrderObserver {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LiveOrderDataProcessor.class);

    @Override
    public void receive(LiveOrder liveOrder) {

        // A live order has been received, now we can do anything with it:

        if (liveOrder.getEvent().equals("order_created")) {

            LiveOrderData data = liveOrder.getData();

            if (data.getAmount() > 5000) {
                LOG.info("Live Order: {}", liveOrder);
            } else {
                LOG.info("Received order {} but it was ignored.", data.getId());
            }

        }

    }
}
