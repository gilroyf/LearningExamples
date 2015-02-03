package com.gilroyfernandes.fileReceiver;

import java.io.*;
import java.net.*;


public class FileReceiver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("input file = " + args[0]);
		System.out.println("output file = " + args[1]);
		String inputFile = args[0];
		String outputFile = args[1];
		Socket s = null;
		try {
			try {
				s = new Socket("127.0.0.1", 40000);
			} finally {

			} 
		}catch (UnknownHostException e) {
			System.err.println("UnknownHost Exception "+ e.getMessage());
		} catch (IOException e) {
			System.err.println("IOException "+ e.getMessage());	
		}
		try {
			Thread.sleep(4000);
			OutputStream os = s.getOutputStream();
			String fileName = inputFile;
			byte[] bo = fileName.getBytes();
			os.write(bo);
			
//			DataInputStream dis=new DataInputStream(new BufferedInputStream(s.getInputStream()));
			InputStream is = s.getInputStream();
			int bufferSize = s.getReceiveBufferSize();
			FileOutputStream fos = new FileOutputStream(outputFile);
			byte[] bytes = new byte[bufferSize];
			int readbytes=0;
			while ( (readbytes=is.read(bytes)) > 0 ) {
				fos.write(bytes,0,readbytes);
			}
			fos.flush();
			fos.close();
			s.close();
		} catch (InterruptedException e) {
			System.err.println("IOException "+ e.getMessage());						
		}catch (IOException e) {
			System.err.println("IOException "+ e.getMessage());				
		}

	}
}


