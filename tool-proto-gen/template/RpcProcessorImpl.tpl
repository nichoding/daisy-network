package com.the9.common.proto;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.msg.IMsgTypeHelper;
import com.the9.daisy.network.msg.MsgProcessorAdapter;
import com.the9.daisy.network.proto.Daisy.RpcMsg;
<#list protoList as proto>
import com.the9.common.proto.Rpc.${proto.fullName};
</#list>


public class RpcProcessorImpl extends MsgProcessorAdapter implements RpcMsgType {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcProcessorImpl.class);

	private IRpcHandler rpcHandler;

	private IMsgTypeHelper msgTypeHelper;

	public RpcProcessorImpl(IRpcHandler rpcHandler, IMsgTypeHelper msgTypeHelper) {
		super();
		this.rpcHandler = rpcHandler;
		this.msgTypeHelper = msgTypeHelper;
	}

	@Override
	public RpcMsg process(Channel channel, RpcMsg msg)
			throws InvalidProtocolBufferException {
		GeneratedMessage retMsg = null;
		int retMsgType = -1;
		switch (msg.getType()) {
		<#list pairList as pair>
		case ${prefix}${pair.request.fullName}:
			<#if pair.response?exists>retMsg = </#if>rpcHandler.${pair.request.simpleNamePascal}(${pair.request.fullName}.parseFrom(msg.getContent()));
			<#if pair.response?exists>
			msgTypeHelper.getMsgTypeByName(${pair.response.fullName}.class.getSimpleName());
			</#if>
			break;
		</#list>
		default:
			logger.error("unkown msg type:{}", msg.getType());
			break;
		}
		RpcMsg ret = null;
		if (retMsg != null) {
			RpcMsg.Builder builder = RpcMsg.newBuilder();
			builder.setSeqId(msg.getSeqId());
			builder.setTimestamp(System.currentTimeMillis());
			builder.setType(retMsgType);
			builder.setContent(retMsg.toByteString());
			ret = builder.build();
			if (channel != null) {
				channel.write(ret);
			}
		}
		return ret;
	}
}
