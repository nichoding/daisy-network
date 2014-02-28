package com.the9.daisy.network.msg;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.proto.Daisy.RpcMsg;

public class MsgProcessorAdapter implements IMsgProcessor {

	@Override
	public RpcMsg process(Channel channel, RpcMsg msg)
			throws InvalidProtocolBufferException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void process(Channel channel, Msg msg)
			throws InvalidProtocolBufferException {
		// TODO Auto-generated method stub
	}

}
