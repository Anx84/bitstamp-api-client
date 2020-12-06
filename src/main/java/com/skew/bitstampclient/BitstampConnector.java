package com.skew.bitstampclient;

import org.slf4j.LoggerFactory;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class BitstampConnector {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BitstampConnector.class);
    static final CountDownLatch messageLatch = new CountDownLatch(1);
    private static Session session;

    private BitstampConnector() { }

    public static void connect() throws DeploymentException, InterruptedException, IOException {

        String uri = "wss://ws.bitstamp.net";

        LOG.info("Connecting to {}", uri);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(BitstampConnectorEndpoint.class, URI.create(uri));

        messageLatch.await(100, TimeUnit.SECONDS);
    }

    public static void disconnect() {

        try {
            LOG.info("BitstampConnector is going to close session");
            session.close();

        } catch (IOException e) {
            LOG.error("Error while disconnecting", e);
        }
    }

    public static void reconnect() {

        disconnect();

        try {
            
        	BitstampConnector.connect();

        } catch (InterruptedException | DeploymentException | IOException e) {
            LOG.error("Exception while connecting");
        }
    }


    public static void setSession(Session newSession) {
        session = newSession;
    }

    public static Session getSession() {
        return session;
    }



}
