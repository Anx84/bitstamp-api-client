package com.skew.bitstampclient.dataprocessors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skew.bitstampclient.messages.liveorderbook.LiveOrderBook;
import com.skew.bitstampclient.messages.liveorderbook.LiveOrderBookData;
import com.skew.bitstampclient.messages.liveorderbook.NormalizedBitstampOrderBook;
import com.skew.bitstampclient.observers.BookObserver;
import com.skew.bitstampclient.utils.AsyncAppender;
import com.skew.bitstampclient.utils.OrderBookMapper;



public class LiveOrderBookDataProcessor implements BookObserver {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LiveOrderBookDataProcessor.class);
    private static final String fileName = "orderbook" + System.currentTimeMillis();
    private static final Path path = Paths.get(fileName + ".csv");
	private static  AsynchronousFileChannel	fileChannel;
    
    @Override
	public void receive(LiveOrderBook orderBook) {
		if(orderBook != null && orderBook.getEvent().length()>0) {
			LiveOrderBookData data = orderBook.getData();
			NormalizedBitstampOrderBook ob = OrderBookMapper.bitstampMapper(orderBook);
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			
			
			if(data.getAsks().size()>0) {

				System.out.println(g.toJson(ob));
				//UUID.randomUUID().toString();

			    
				try {
					if(fileChannel == null || !fileChannel.isOpen()) {
						 fileChannel = AsynchronousFileChannel.open(
						  path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
					}
					
				    String csvLine = OrderBookMapper.getAndResetCsvLine(); 
				    
				    ByteBuffer buffer = ByteBuffer.allocate(1024);
				    buffer.put(csvLine.getBytes());
				    buffer.flip();
				    
				    AsyncAppender appender = new AsyncAppender(fileChannel);
				    appender.append(buffer);
				    
				    fileChannel.close();
			    
				} catch (IOException e) {
					// TODO Auto-generated catch block
					LOG.error(e.getMessage());
					
				}
				
			}
		}
		
	}
	
	

}
