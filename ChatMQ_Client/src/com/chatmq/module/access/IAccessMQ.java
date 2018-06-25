package com.chatmq.module.access;

import com.rabbitmq.client.Channel;

public interface IAccessMQ {

	public abstract boolean login(Channel channel, String username, String password);
}
