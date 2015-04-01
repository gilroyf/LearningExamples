package com.gilroyfernandes.marketData;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class MarketDataTest {

	@Test 
	public void testEquals() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		MarketData md2 = MarketData.newIstance("IBM", d1, 100);
		assertTrue("Objects should be equal", md1.equals(md2));
	}
	
	@Test 
	public void testHashCodeEquals() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		MarketData md2 = MarketData.newIstance("IBM", d1, 100);
		assertTrue("hash codes should be equal", md1.hashCode() == md2.hashCode() );
	}
	
	@Test
	public void testNotEqualsDate() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		Date d2 = new Date();
		d2.setSeconds(d2.getSeconds() + 20);
		MarketData md3 = MarketData.newIstance("IBM", d2, 100);
		System.out.println("d1 = " + d1 + " d2 = " + d2);
		assertFalse("Objects should not be equal", md1.equals(md3) || md1.hashCode() == md3.hashCode());
		
	}
	@Test 
	public void testNotEqualsTicker() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		MarketData md2 = MarketData.newIstance("VOD", d1, 100);
		assertFalse("Objects should not be equal different tickers", md1.equals(md2));
	}
	@Test 
	public void testNotEqualsPrice() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		MarketData md2 = MarketData.newIstance("IBM", d1, 100.1);
		assertFalse("Objects should not be equal different price", md1.equals(md2));
	}
	@Test 
	public void testEqualsChangingPrice() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		MarketData md2 = MarketData.newIstance("IBM", d1, 100.1);
		md2.setValue(100);
		assertTrue("Objects should be equal different price", md1.equals(md2));
	}
	@Test 
	public void testexceptionifbadprice() {
		Date d1 = new Date();
		try {
			MarketData md1 = MarketData.newIstance("IBM", d1, -100);
			fail("Fail to throw illegalargument exception");
		} catch (Exception e) {
		
		}
	}
	@Test 
	public void testDateGetterImmutable() {
		Date d1 = new Date();
		MarketData md1 = MarketData.newIstance("IBM", d1, 100);
		Date d2 = md1.getTimestamp();
		d2.setSeconds(d2.getSeconds() + 100);
		assertFalse("Dates should not be equal", md1.getTimestamp().equals(d2));
	}
	@Test
	public void testGetTimestamp() {
//		fail("Not yet implemented");
	}

	@Test
	public void testSetTimestamp() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetValue() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testSetValue() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testNewIstance() {
	//	fail("Not yet implemented");
	}

}
