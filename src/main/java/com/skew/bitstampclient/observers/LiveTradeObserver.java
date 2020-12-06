package com.skew.bitstampclient.observers;

import com.skew.bitstampclient.messages.livetrade.LiveTrade;

/**
 * Live ticker
 */
public interface LiveTradeObserver {

    void receive(LiveTrade liveTrade);

}
