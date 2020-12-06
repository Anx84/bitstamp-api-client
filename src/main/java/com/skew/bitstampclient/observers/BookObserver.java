package com.skew.bitstampclient.observers;

import com.skew.bitstampclient.messages.liveorderbook.LiveOrderBook;

public interface BookObserver {

    void receive(LiveOrderBook message);

}
