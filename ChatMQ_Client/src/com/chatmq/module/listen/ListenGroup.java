package com.chatmq.module.listen;

import com.chatmq.module.utils.Utils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class ListenGroup implements Runnable{

	private static Utils utils = new Utils();
	private String EXCHANGE_NAME;
	private Channel channel;
	
	public ListenGroup(Channel channel, String EXCHANGE_NAME) {
		this.EXCHANGE_NAME = EXCHANGE_NAME;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		try {
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, "");

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("["+EXCHANGE_NAME+" : "+utils.getDate()+"]: '" + message + "'");
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in Thread ListenGroup: " + e.getMessage());
		}
	}
}
