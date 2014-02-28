package com.the9.daisy.pdl;

import org.jboss.netty.channel.Channel;

public interface ISessionManager {
	AbstractSession addSession(Channel channel);

	void removeSession(Channel channel);

	AbstractSession getSession(Channel channel);

}
