package com.chatmq.module.app;

import com.chatmq.module.listen.ListenAuthenticate;
import com.chatmq.module.listen.ListenGroups;
import com.chatmq.module.utils.Utils;
import com.chatmq.module.connection.ConnectionMQ;
import com.rabbitmq.client.Channel;

public class App {
	
	private static ConnectionMQ conn = new ConnectionMQ();
	private static Channel channel = conn.getChannel();
	private static Utils utils = new Utils();
	
	public static void main(String[] argv) throws Exception {
		System.out.println("["+utils.getDate()+"] ChatMQ Server iniciado!\n");

		Thread queueLogin = new Thread(new ListenAuthenticate(channel));
		queueLogin.start();
		
		Thread queueGroups = new Thread(new ListenGroups(channel));
		queueGroups.start();
	}
}
