package com.chatmq.module.listen;

import java.io.IOException;

import com.chatmq.module.utils.Utils;

import com.rabbitmq.client.*;

public class ListenGroups implements Runnable {
	private final static String pathGroups = "C:/Users/caio/workspace/ChatMQ_Server/docs/groups.txt";
	private final static String pathUsers = "C:/Users/caio/workspace/ChatMQ_Server/docs/users.txt";
	private final static String QUEUE_GROUPS = "SERVERGROUPS";
	
	private static Utils utils = new Utils();
	private Channel channel;
	
	public ListenGroups(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public void run() {
		try {
			channel.queueDeclare(QUEUE_GROUPS, false, false, false, null);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					
					String[] parts = message.split(":");
					
					if("NEW".equals(parts[0])) {
						String line = utils.getLineFirst(pathGroups, parts[1]);
						if(line != null) {
							utils.removeLine(pathGroups, line);
							if(!(utils.hasPart(line, parts[2]))) {
								line += ":"+parts[2];
							}
							utils.writeFile(pathGroups, line);
						} else {
							line = ""+parts[1]+":"+parts[2];
							utils.writeFile(pathGroups, line);
						}
						System.out.println("["+utils.getDate()+"] Arquivo de grupos atualizado.");
					}
					
					if("GETGROUPS".equals(parts[0])) {
						String msg = "Lista de grupos: \n"+utils.toString(pathGroups);
						String QUEUE_NAME = parts[1];
						try {
							channel.queueDeclare(QUEUE_NAME, false, false, false, null);
							channel.basicPublish("", QUEUE_NAME, null, msg.getBytes("UTF-8"));
						} catch (IOException e) {
							System.out.println("["+utils.getDate()+"] Error in getGroups: " + e.getMessage());
						}
						System.out.println("["+utils.getDate()+"] Retornando lista de grupos para '"+QUEUE_NAME.toLowerCase()+"'.");
					}
					
					if("GETUSERS".equals(parts[0])) {
						String msg = "Lista de usuários: \n"+utils.toString(pathUsers);
						String QUEUE_NAME = parts[1];
						try {
							channel.queueDeclare(QUEUE_NAME, false, false, false, null);
							channel.basicPublish("", QUEUE_NAME, null, msg.getBytes("UTF-8"));
						} catch (IOException e) {
							System.out.println("["+utils.getDate()+"] Error in getGroups: " + e.getMessage());
						}
						System.out.println("["+utils.getDate()+"] Retornando lista de usuários para '"+QUEUE_NAME.toLowerCase()+"'.");
					}
				}
			};
			channel.basicConsume(QUEUE_GROUPS, true, consumer);
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Erro na Thread " + QUEUE_GROUPS + ": " + e.getMessage());
		}
	}
}
