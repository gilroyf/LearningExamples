package com.fernandes.gilroy;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

final class WordCount implements Callable<Integer>{
	final String m_s;
	final ConcurrentHashMap<String, Integer> mmsi;
	public WordCount(ConcurrentHashMap<String, Integer> msi, String s) {
		m_s = s;
		mmsi=msi;
	}

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		String arr[]= m_s.split(" ");
		for (String s:arr) {
			Integer v = mmsi.get(s);
			if (v == null) {
				if (mmsi.putIfAbsent(s, 1) == null) {
					
					
				}
			} else {
	
				while (!mmsi.replace(s, v, v+1))
					v=mmsi.get(s);
			}
		}
		return 0;
	}
		
		
	
	

}
