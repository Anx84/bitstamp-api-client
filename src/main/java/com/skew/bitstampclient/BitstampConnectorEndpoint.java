package com.skew.bitstampclient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.skew.bitstampclient.messages.Message;
import com.skew.bitstampclient.messages.liveorder.LiveOrder;
import com.skew.bitstampclient.messages.liveorderbook.LiveOrderBook;
import com.skew.bitstampclient.messages.livetrade.LiveTrade;
import com.skew.bitstampclient.messages.subscription.SubscriptionMessage;
import com.skew.bitstampclient.observers.BookObserver;
import com.skew.bitstampclient.observers.CloseObserver;
import com.skew.bitstampclient.observers.ConnectionOpenObserver;
import com.skew.bitstampclient.observers.LiveOrderObserver;
import com.skew.bitstampclient.observers.LiveTradeObserver;
import com.skew.bitstampclient.observers.MessageObserver;


@ClientEndpoint
public class BitstampConnectorEndpoint {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BitstampConnectorEndpoint.class);

    private Gson gson = new Gson();

    private static Set<String> liveTradePairSubscriptions = new HashSet<>();
    private static Set<String> liveOrderPairSubscriptions = new HashSet<>();
    private static Set<String> liveOrderBookSubscriptions = new HashSet<>();

    private static List<ConnectionOpenObserver> openObservers = new ArrayList<>();
    private static List<MessageObserver> messageObservers = new ArrayList<>();
    private static List<CloseObserver> closeObservers = new ArrayList<>();
    private static List<LiveTradeObserver> liveTradeObservers = new ArrayList<>();
    private static List<LiveOrderObserver> liveOrderObservers = new ArrayList<>();
    private static List<BookObserver> liveOrderBookObservers = new ArrayList<>();

    public static void onOpen(ConnectionOpenObserver observer) {
        openObservers.add(observer);
    }

    public static void onMessage(MessageObserver observer) {
        messageObservers.add(observer);
    }

    public static void onLiveTrade(String pair, LiveTradeObserver observer) {

        if (!liveTradePairSubscriptions.contains(pair)) {

            SubscriptionMessage message = new SubscriptionMessage();
            try {
                message.subscribeChannel("live_trades_" + pair.toLowerCase().trim());
            } catch (Exception e) {
                LOG.error("Error", e);
            }

            liveTradePairSubscriptions.add(pair);
            liveTradeObservers.add(observer);
        }
    }

    public static void onLiveOrder(String pair, LiveOrderObserver observer) {

        if (!liveOrderPairSubscriptions.contains(pair)) {

            SubscriptionMessage message = new SubscriptionMessage();
            try {
                message.subscribeChannel("live_orders_" + pair.toLowerCase().trim());

            } catch (Exception e) {
                LOG.error("Error", e);
            }

            liveOrderPairSubscriptions.add(pair);
            liveOrderObservers.add(observer);
        }
    }
    
    public static void onOrderBook(String pair, BookObserver observer) {
    	
    	if(!liveOrderBookSubscriptions.contains(pair)) {
    		SubscriptionMessage message = new SubscriptionMessage(); 
    		try {
    			message.subscribeChannel("order_book_" + pair.toLowerCase().trim());
    		} catch (Exception e ) {
    			LOG.error("Error", e);
    		}
    		liveOrderBookSubscriptions.add(pair);
    		liveOrderBookObservers.add(observer);
    	}
    }

    public static void onClose(CloseObserver observer) {
        closeObservers.add(observer);
    }

    @OnOpen
    public void eventOpen(Session session) {

        LOG.debug("Connected to endpoint: {}", session.getBasicRemote());

        BitstampConnector.setSession(session);

        for (ConnectionOpenObserver observer : openObservers) {
            observer.receive();
        }
    }

    @OnMessage
    public void eventMessage(String jsonMessage) {

        LOG.debug("Received: {}", jsonMessage);

        for (MessageObserver observer : messageObservers) {
            observer.receive(jsonMessage);
        }

        Message message = gson.fromJson(jsonMessage, Message.class);

        switch (message.getEvent()) {

            case "trade":

                LiveTrade trade = gson.fromJson(jsonMessage, LiveTrade.class);
                for (LiveTradeObserver observer : liveTradeObservers) {
                    observer.receive(trade);
                }
                break;

            case "order_created":
            case "order_changed":
            case "order_deleted":

                LiveOrder order = gson.fromJson(jsonMessage, LiveOrder.class);
                for (LiveOrderObserver observer : liveOrderObservers) {
                    observer.receive(order);
                }
                break;

            case "bts:subscription_succeeded":
                break;

            case "bts:request_reconnect":
                BitstampConnector.reconnect();
                break;
            case "data":
            	LiveOrderBook orderBook = gson.fromJson(jsonMessage, LiveOrderBook.class);
            	for(BookObserver observer: liveOrderBookObservers) {
            		observer.receive(orderBook);
            	}
            	break;

            default:
                LOG.warn("Unknown event: {}", message.getEvent());
        }

        BitstampConnector.messageLatch.countDown();
    }

    @OnError
    public void eventError(Throwable t) {
        LOG.error("Error", t);
    }

    @OnClose
    public void eventClose() {

        LOG.info("onClose event");

        for (CloseObserver observer : closeObservers) {
            observer.receive();
        }
    }


}