package com.skew.bitstampclient.dataprocessors;

import org.slf4j.LoggerFactory;

import com.skew.bitstampclient.messages.livetrade.LiveTrade;
import com.skew.bitstampclient.messages.livetrade.LiveTradeData;
import com.skew.bitstampclient.observers.LiveTradeObserver;

public class LiveTradeDataProcessor implements LiveTradeObserver {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LiveTradeDataProcessor.class);

    @Override
    public void receive(LiveTrade liveTrade) {

        // A live trade has been received, now we can do anything with it:

        LiveTradeData data = liveTrade.getData();

        if (data.getAmount() > 5000) {
            LOG.info("Live Trade: {}", liveTrade);

        } else {
            LOG.info("Received trade {} but it was ignored.", data.getId());
        }

    }
}
