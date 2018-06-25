package com.chatmq.module.services;

import com.rabbitmq.client.Channel;

public interface IServices {

	public abstract void sendUser(Channel channel, String username, String EXCHANGE_NAME, String message);
	
	public abstract void sendGroup(Channel channel, String username, String EXCHANGE_NAME, String message);
	
	public abstract void startGroups(Channel channel, String path);
	
	public abstract void newGroup(Channel channel, String username, String group);
	
	public abstract void getGroups(Channel channel, String username);
	
	public abstract void getUsers(Channel channel, String username);
}
