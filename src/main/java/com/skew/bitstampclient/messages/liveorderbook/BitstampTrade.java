package com.skew.bitstampclient.messages.liveorderbook;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BitstampTrade {
	
	private BigDecimal quantity;
	private BigDecimal price;
}
