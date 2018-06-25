package com.chatmq.module.connection;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.chatmq.module.utils.Utils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionMQ {
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private static Utils utils = new Utils();
	
	public ConnectionMQ() {
		factory = new ConnectionFactory();
	    factory.setHost("localhost");
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (IOException | TimeoutException e) {
			System.out.println("["+utils.getDate()+"] Erro: "+e.getMessage());
		}
	}

	public ConnectionFactory getFactory() {
		return factory;
	}

	public Connection getConnection() {
		return connection;
	}

	public Channel getChannel() {
		return channel;
	}
}

