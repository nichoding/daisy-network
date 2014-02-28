package com.the9.daisy.pdl;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.msg.AbstractMsgEvent;
import com.the9.daisy.network.msg.Msg;
import com.the9.daisy.network.msg.MsgDispatcher;

/**
 * 
 * @author dingshengheng
 * 
 */
public class PdlMsgEvent extends AbstractMsgEvent {

	private final Msg msg;

	private final Channel channel;

	public PdlMsgEvent(Channel channel, Msg msg) {
		super(System.currentTimeMillis());
		this.channel = channel;
		this.msg = msg;
	}

	@Override
	public void handle(MsgDispatcher dispatcher)
			throws InvalidProtocolBufferException {
		dispatcher.getPdlProcessor().process(channel, msg);
	}

	@Override
	public int getMsgType() {
		return msg.getType();
	}

	@Override
	public EventType getEventType() {
		return EventType.PDL;
	}

}
