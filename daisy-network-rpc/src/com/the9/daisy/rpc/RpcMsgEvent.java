package com.the9.daisy.rpc;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.msg.AbstractMsgEvent;
import com.the9.daisy.network.msg.MsgDispatcher;
import com.the9.daisy.network.proto.Daisy.RpcMsg;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcMsgEvent extends AbstractMsgEvent {

	private final RpcMsg msg;

	private final Channel channel;

	public RpcMsgEvent(Channel channel, RpcMsg msg) {
		super(System.currentTimeMillis());
		this.channel = channel;
		this.msg = msg;
	}

	@Override
	public void handle(MsgDispatcher dispatcher)
			throws InvalidProtocolBufferException {
		dispatcher.getRpcProcessor().process(channel, msg);
	}

	@Override
	public int getMsgType() {
		return msg.getType();
	}

	@Override
	public EventType getEventType() {
		return EventType.RPC;
	}

}
