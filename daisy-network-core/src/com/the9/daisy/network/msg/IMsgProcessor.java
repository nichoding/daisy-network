package com.the9.daisy.network.msg;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.proto.Daisy.RpcMsg;

public interface IMsgProcessor {
	RpcMsg process(Channel channel, RpcMsg msg)
			throws InvalidProtocolBufferException;

	void process(Channel channel, Msg msg)
			throws InvalidProtocolBufferException;
}
