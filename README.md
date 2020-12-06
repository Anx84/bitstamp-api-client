Java Bitstamp API Client
========================
This is a [Bitstamp WebSockets API v2](https://www.bitstamp.net/websocket/v2/) client.
 
[Bitstamp](https://bitstamp.net) is a Crypto Exchange where you can trade a number of cryptocurrencies.

The [original API client version](https://gitlab.com/amrodrigues/java-bitstamp-api-client) allowed you to connect to Bitstamp, subscribe to crypto pairs, receive live order data and live trade data. This version extends its features: it can also get live order books and simultaneously save order book data being received into a generated csv file.

See [Bitstamp API documentation](https://www.bitstamp.net/websocket/v2/)

This project is not affiliated with Bitstamp Ltd. The source code it MIT-Licensed, meaning you can do anything with it without any restriction.

**Usage**: Run the main class (com.skew.bitstambclient.Main) specifying the running time and crypto pairs you want to receive data from.


	\*
	 * Running the jar in the target folder with these arguments will execute it for 2 minutes and 
	 * take data from btcusd and xrpusd pairs
	 *\
	 
	java -jar filename.jar 120 btcusd xrpusd



Examples:

    package your.package;
    
    import org.slf4j.LoggerFactory;
    
    import com.company.bitstampclient.*;
    import javax.websocket.DeploymentException;
    import java.io.IOException;
    import java.util.Scanner;
    
    public class ConnectionTest {
    
        private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ConnectionTest.class);
    
        public void connect() {
    
            BitstampConnectorEndpoint.onOpen(new ConnectionOpen());
    
            try {
                BitstampConnector.connect();
    
            } catch (InterruptedException | DeploymentException | IOException e) {
                LOG.error("Exception while connecting", e);
            }
    
            new Scanner(System.in).nextLine();
        }
    
    }
    

When the connection is open, the receive() method will be called. Now we can start subscribing to feeds:

*File ConnectionOpen.java:*

    package your.package;
    
    import com.company.bitstampclient.observers.ConnectionOpenObserver;
    
    public class ConnectionOpen implements ConnectionOpenObserver {
    
        @Override
        public void receive() {
    
            BitstampConnectorEndpoint.onLiveTrade("xrpusd", new XrpUsdLiveTrade());
    
            BitstampConnectorEndpoint.onLiveOrder("xrpusd", new XrpUsdLiveOrder());
    
        }
    }    


The following two classes allow you to do anything with the feed messages as they arrive in real time:

*File XrpUsdLiveTrade.java*

    package your.package;
    
    import com.company.bitstampclient.messages.livetrade.LiveTrade;
    import com.company.bitstampclient.messages.livetrade.LiveTradeData;
    import com.company.bitstampclient.observers.LiveTradeObserver;
    import org.slf4j.LoggerFactory;
    
    public class XrpUsdLiveTrade implements LiveTradeObserver {
    
        private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(XrpUsdLiveTrade.class);
    
        @Override
        public void receive(LiveTrade liveTrade) {
    
            LiveTradeData data = liveTrade.getData();
    
            if (data.getAmount() > 5000) {
                LOG.info("Live Trade: {}", liveTrade);
    
            } else {
                LOG.info("Received trade {} but it was ignored.", data.getId());
            }
    
        }
    }


Similarly, XrpUsdLiveOrder will receive all live orders:

*File XrpUsdLiveOrder.java*


    package your.package;
    
    import com.company.bitstampclient.messages.liveorder.LiveOrder;
    import com.company.bitstampclient.messages.liveorder.LiveOrderData;
    import com.company.bitstampclient.observers.LiveOrderObserver;
    import org.slf4j.LoggerFactory;
    
    public class XrpUsdLiveOrder implements LiveOrderObserver {
    
        private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(XrpUsdLiveOrder.class);
    
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
