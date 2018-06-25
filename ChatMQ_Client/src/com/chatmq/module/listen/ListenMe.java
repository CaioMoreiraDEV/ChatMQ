package com.chatmq.module.listen;

import com.chatmq.module.utils.Utils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class ListenMe implements Runnable{

	private static Utils utils = new Utils();
	private String QUEUE_NAME;
	private Channel channel;
	
	public ListenMe(Channel channel, String QUEUE_NAME) {
		this.QUEUE_NAME = QUEUE_NAME;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		try {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");

					System.out.println("[Me : "+utils.getDate()+"] " + message);
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);

		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in Thread ListenMe: " + e.getMessage());
		}
	}
}
