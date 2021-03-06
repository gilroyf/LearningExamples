package com.gf.tutorial.websockets;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpointdemo")
public class WebSocketEndPointDemo {
	@OnOpen
	public void handleOpen() {
		System.out.println("client is connected");
	}
	
	@OnClose
	public void handleClose() {
		System.out.println("client is clcose");
	}
	
	@OnMessage
	public String handleMessage(String message) {
		System.out.println("received message "+ message);
		String response = " echo " + message;
		return response;

	}
	
	@OnError
	public void handleError(Throwable t)
	{
		
		
	}
}
