package com.skew.bitstampclient.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.skew.bitstampclient.messages.liveorderbook.BitstampTrade;
import com.skew.bitstampclient.messages.liveorderbook.LiveOrderBook;
import com.skew.bitstampclient.messages.liveorderbook.NormalizedBitstampOrderBook;

public class OrderBookMapper {
	public static StringBuilder csvLine = new StringBuilder();
	public static NormalizedBitstampOrderBook bitstampMapper(LiveOrderBook orderBook) {
		NormalizedBitstampOrderBook ob = new NormalizedBitstampOrderBook();
		
		String[] channelSplit = orderBook.getChannel().split("_");
		ob.setSymbol(channelNormalizer(channelSplit[channelSplit.length-1]));
		ob.setExchange("Bitstamp");
		
		csvLine.append(orderBook.getData().getTimestamp()).append(";")
		.append(ob.getSymbol()).append(";");
		
		
		List<BitstampTrade> asks = new ArrayList<>(10);
		List<BitstampTrade> bids = new ArrayList<>(10);
		/*
		 * e mo carica tutto sull'altro oggetto ;)
		 */
		if(orderBook.getData().getAsks().size() == 100) {
			BigDecimal askLevelQuantity = BigDecimal.ZERO;
			BigDecimal askLevelPrice    = BigDecimal.ZERO;
			BigDecimal bidLevelQuantity = BigDecimal.ZERO;
			BigDecimal bidLevelPrice    = BigDecimal.ZERO;
			
			int i = 0;
			while(i<10) {
				
				askLevelQuantity = new BigDecimal(orderBook.getData().getAsks().get(i).get(1));
				askLevelPrice = new BigDecimal(orderBook.getData().getAsks().get(i).get(0));
				bidLevelQuantity = new BigDecimal(orderBook.getData().getBids().get(i).get(1));
				bidLevelPrice = new BigDecimal(orderBook.getData().getBids().get(i).get(0));
				
				asks.add(new BitstampTrade(askLevelQuantity.setScale(7, BigDecimal.ROUND_HALF_UP),askLevelPrice.setScale(1, BigDecimal.ROUND_HALF_UP)));
				bids.add(new BitstampTrade(bidLevelQuantity.setScale(7, BigDecimal.ROUND_HALF_UP),bidLevelPrice.setScale(1, BigDecimal.ROUND_HALF_UP)));
				
				if(i == 0) {
					csvLine.append(bidLevelQuantity.toString()).append(";").append(bidLevelPrice.toString()).append(";")
					.append(askLevelQuantity.toString()).append(";").append(askLevelPrice.toString()).append(System.lineSeparator());
				}
				
				i++;
			}
			ob.setAsks(asks);
			ob.setBids(bids);
			
		} 
		
		return ob;
	}
	
	public static String channelNormalizer(String channel) {
		if (channel.length() == 6) {
			
			StringBuilder res = new StringBuilder();
			res.append(channel.substring(0,3).toUpperCase())
			   .append("/")
			   .append(channel.substring(3,6).toUpperCase());
			return res.toString();
			
		} else if (channel.length() == 8 &&channel.substring(0, 2).equalsIgnoreCase("xx")) {
			channel = channel.substring(3,8);
			StringBuilder res = new StringBuilder();
			res.append(channel.substring(0,3).toUpperCase())
			   .append("/")
			   .append(channel.substring(3,6).toUpperCase());
			return res.toString();
		} else {
			return "";
		}
	}
	
	public static String getAndResetCsvLine() {
		csvLine.trimToSize();
		int len = csvLine.length();
		
		String res = csvLine.toString();
		csvLine = new StringBuilder(len);
		return res;
	}
}
