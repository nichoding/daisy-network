package com.the9.daisy.pdl;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.GeneratedMessage;

public abstract class AbstractSession {

	protected int userId;

	protected String sessionKey;

	protected final Channel channel;

	public AbstractSession(Channel channel) {
		super();
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public int getUserId() {
		return userId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void bind(int userId, String sessionKey) {
		this.userId = userId;
		this.sessionKey = sessionKey;
	}

	public abstract void sendMsg(GeneratedMessage msg);
}
