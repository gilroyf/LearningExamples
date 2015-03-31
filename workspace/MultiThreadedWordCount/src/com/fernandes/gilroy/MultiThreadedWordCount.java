package com.fernandes.gilroy;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class MultiThreadedWordCount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("args = " + args[0]);
		Scanner inp=null;
		ExecutorService pool = Executors.newFixedThreadPool(10);
		List<Future<Integer> > lfi = new LinkedList<>();
		ConcurrentHashMap<String, Integer> msi = new ConcurrentHashMap<String, Integer>();
		try{
			inp = new Scanner(new File(args[0]));
			int i = 0;
			while (inp.hasNextLine()) {
//				try {
//					  Thread.sleep(1);
//				} catch (InterruptedException ie) {
//					    //Handle exception
//					System.out.println("exception");
//					Thread.currentThread().interrupt();
//				}
				String ln = inp.nextLine();
				++i;
				//System.out.println(ln);
				Future<Integer> result = pool.submit(new WordCount(msi, ln));
				lfi.add(result);
			}
			Integer result=0;
			for (Future<Integer> e: lfi) {
				result += e.get();
			}
//			Set<String> keys = msi.keySet();
			for (Map.Entry<String, Integer> entry: msi.entrySet()) {
				System.out.println(entry.getKey() +" " + " = " + entry.getValue());
			}
			
		} catch (FileNotFoundException | InterruptedException | ExecutionException e) {
			
		} finally { 
			System.out.println("goign to close");
			inp.close();
		}
		
	}

}
