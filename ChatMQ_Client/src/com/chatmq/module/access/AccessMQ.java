package com.chatmq.module.access;

import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.Channel;
import com.chatmq.module.utils.Utils;
import com.rabbitmq.client.AMQP;

import java.io.IOException;

public class AccessMQ implements IAccessMQ {
	private final static String QUEUE_LOGIN = "SERVERLOGIN";
	
	private static Utils utils = new Utils();

	private boolean feedback = true;

	public boolean login(Channel channel, String username, String password) {
		String line = "" + username + ":" + password;

		try {
			channel.queueDeclare(QUEUE_LOGIN, false, false, false, null);
			channel.basicPublish("", QUEUE_LOGIN, null, line.getBytes("UTF-8"));

			System.out.println("\n["+utils.getDate()+"] Aguardando autenticação de login... \n");

			String AUTHLOGIN = username.toUpperCase() + "AUTH";
			channel.queueDeclare(AUTHLOGIN, false, false, false, null);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");

					if (message.equals("200"))
						feedback = true;
					if (message.equals("!200"))
						feedback = false;
				}
			};
			channel.basicConsume(AUTHLOGIN, true, consumer);

		} catch (IOException e) {
			System.out.println("Erro no AccessMQ: " + e.getMessage());
		}
		return feedback;
	}
	
}
