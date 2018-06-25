package com.chatmq.module.services;

import java.io.IOException;

import com.chatmq.module.listen.ListenGroup;
import com.chatmq.module.utils.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class Services implements IServices{
	private final static String QUEUE_GROUPS = "SERVERGROUPS";
	private static Utils utils = new Utils();
	
	public void sendUser(Channel channel, String username, String EXCHANGE_NAME, String message) {
		String msg = username+": "+message;
		try {
			channel.queueDeclare(EXCHANGE_NAME, false, false, false, null);
			channel.basicPublish("", EXCHANGE_NAME, null, msg.getBytes("UTF-8"));
			System.out.println("["+utils.getDate()+"]  Enviando: '" + message + "' para " + EXCHANGE_NAME);
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in sendUser: " + e.getMessage());
		}
	}
	
	public void sendGroup(Channel channel, String username, String EXCHANGE_NAME, String message) {
		String msg = username+": "+message;
		try {
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

			channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes("UTF-8"));
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in sendGroup: " + e.getMessage());
		}
	}
	
	public void startGroups(Channel channel, String path) {
		String my_groups = utils.toString(path).replace("\n", ":");
		String[] list = my_groups.split(":");
		
		for(String group : list)
			new Thread(new ListenGroup(channel, group.toUpperCase())).start();
	}
	
	public void newGroup(Channel channel, String username, String group) {
		String msg = "NEW:"+group+":"+username.toLowerCase();
		try {
			channel.queueDeclare(QUEUE_GROUPS, false, false, false, null);
			channel.basicPublish("", QUEUE_GROUPS, null, msg.getBytes("UTF-8"));
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in newGroup: " + e.getMessage());
		}
	}
	
	public void getGroups(Channel channel, String username) {
		String msg = "GETGROUPS:"+username;
		try {
			channel.queueDeclare(QUEUE_GROUPS, false, false, false, null);
			channel.basicPublish("", QUEUE_GROUPS, null, msg.getBytes("UTF-8"));
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in getGroups: " + e.getMessage());
		}
	}
	
	public void getUsers(Channel channel, String username) {
		String msg = "GETUSERS:"+username;
		try {
			channel.queueDeclare(QUEUE_GROUPS, false, false, false, null);
			channel.basicPublish("", QUEUE_GROUPS, null, msg.getBytes("UTF-8"));
		} catch (IOException e) {
			System.out.println("["+utils.getDate()+"] Error in getUsers: " + e.getMessage());
		}
	}
}
