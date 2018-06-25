package com.chatmq.module.listen;

import com.chatmq.module.utils.Utils;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ListenAuthenticate implements Runnable {
	private final static String pathUsers = "C:/Users/caio/workspace/ChatMQ_Server/docs/users.txt";
	private final static String pathAuth = "C:/Users/caio/workspace/ChatMQ_Server/docs/auth.txt";
	private final static String QUEUE_LOGIN = "SERVERLOGIN";
	
	private static Utils utils = new Utils();
	private Channel channel;
	
	public ListenAuthenticate(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public void run() {
		try {
			channel.queueDeclare(QUEUE_LOGIN, false, false, false, null);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");

					String[] bodyParts = message.split(":");

					String[] registryParts = null;
					if (utils.getLineFirst(pathAuth, bodyParts[0]) != null) {
						registryParts = utils.getLineFirst(pathAuth, bodyParts[0]).split(":");
					}

					String AUTHLOGIN = bodyParts[0].toUpperCase() + "AUTH";
					channel.queueDeclare(AUTHLOGIN, false, false, false, null);

					if ((registryParts != null) && bodyParts[0].equals(registryParts[0])) {
						if (bodyParts[1].equals(registryParts[1])) {
							System.out.println("["+utils.getDate()+"] Usuário '" + bodyParts[0] + "' se conectou!");

							channel.basicPublish("", AUTHLOGIN, null, "200".getBytes("UTF-8"));
						} else {
							channel.basicPublish("", AUTHLOGIN, null, "!200".getBytes("UTF-8"));
						}
					} else {
						System.out.println("["+utils.getDate()+"] Novo usuário '" + bodyParts[0] + "' se conectou!");

						utils.writeFile(pathUsers, bodyParts[0].toLowerCase());
						utils.writeFile(pathAuth, message);

						channel.basicPublish("", AUTHLOGIN, null, "200".getBytes("UTF-8"));
					}
				}
			};
			channel.basicConsume(QUEUE_LOGIN, true, consumer);

		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Erro na Thread " + QUEUE_LOGIN + ": " + e.getMessage());
		}
	}

}
