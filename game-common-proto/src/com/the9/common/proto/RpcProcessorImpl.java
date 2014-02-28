package com.the9.common.proto;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.msg.IMsgTypeHelper;
import com.the9.daisy.network.msg.MsgProcessorAdapter;
import com.the9.daisy.network.proto.Daisy.RpcMsg;
import com.the9.common.proto.Rpc.LoginGameReq;
import com.the9.common.proto.Rpc.LoginGameAck;
import com.the9.common.proto.Rpc.LoginOutReq;
import com.the9.common.proto.Rpc.GetServerConfigReq;
import com.the9.common.proto.Rpc.GetServerConfigAck;


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
		case RPC_LoginGameReq:
			retMsg = rpcHandler.loginGame(LoginGameReq.parseFrom(msg.getContent()));
			msgTypeHelper.getMsgTypeByName(LoginGameAck.class.getSimpleName());
			break;
		case RPC_LoginOutReq:
			rpcHandler.loginOut(LoginOutReq.parseFrom(msg.getContent()));
			break;
		case RPC_GetServerConfigReq:
			retMsg = rpcHandler.getServerConfig(GetServerConfigReq.parseFrom(msg.getContent()));
			msgTypeHelper.getMsgTypeByName(GetServerConfigAck.class.getSimpleName());
			break;
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
