package com.gilroyfernandes.marketData;

import java.util.*;

public final class MarketData {

	private final String ticker;
	private Date timestamp;
	private double value;
	
	public Date getTimestamp() {
		return new Date(timestamp.getTime());
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * @return the ticker
	 */
	public String getTicker() {
		return ticker;
	}

	private MarketData(String t, Date ts, double v) {
		if (v < 0)
			throw new IllegalArgumentException();
		ticker = t;
		timestamp = ts;
		value = v;
	}
	
	/**
	 * @param t - ticker for instrument
	 * @param ts - timestamp of the value
	 * @param v - value. Needs to be positive
	 * @return MarketData
	 * @throws IllegalArgumentException
	 */
	public static MarketData newIstance(String t, Date ts, double v) {
		return new MarketData(t, ts, v);
	}
	
	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MarketData)) {
			return false;
		}
		MarketData other=(MarketData)o;
		
		if (this.ticker.equals(other.ticker) && this.timestamp.equals(other.timestamp) && value == other.value )
			return true;
		else
			return false;
	}
	
	@Override public int hashCode() {
		int result  = 17;
		result = 31 * result + ticker.hashCode();
		result = 31 * result + timestamp.hashCode();
		long temp = Double.doubleToLongBits(value);
		result = 31 * result + (int)(temp ^ (temp >>> 32));
		return result;
	}
}
