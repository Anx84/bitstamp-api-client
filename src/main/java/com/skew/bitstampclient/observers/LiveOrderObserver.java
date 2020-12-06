package com.skew.bitstampclient.observers;

import com.skew.bitstampclient.messages.liveorder.LiveOrder;

public interface LiveOrderObserver {

    /**
     * Receive an order that was created, changed or deleted.
     */
    void receive(LiveOrder liveOrder);

}
