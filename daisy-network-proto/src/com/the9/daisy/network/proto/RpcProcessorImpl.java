package com.the9.daisy.network.proto;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.msg.IMsgTypeHelper;
import com.the9.daisy.network.msg.MsgProcessorAdapter;
import com.the9.daisy.network.proto.Daisy.RpcMsg;
import com.the9.daisy.network.proto.Rpc.LoginGameReq;
import com.the9.daisy.network.proto.Rpc.LoginGameAck;
import com.the9.daisy.network.proto.Rpc.LoginOutReq;
import com.the9.daisy.network.proto.Rpc.GetServerConfigReq;
import com.the9.daisy.network.proto.Rpc.GetServerConfigAck;
import com.the9.daisy.network.proto.Rpc.PublishNoticeReq;
import com.the9.daisy.network.proto.Rpc.StopServerReq;
import com.the9.daisy.network.proto.Rpc.PauseServerReq;
import com.the9.daisy.network.proto.Rpc.ResumeServerReq;
import com.the9.daisy.network.proto.Rpc.OpenServerReq;
import com.the9.daisy.network.proto.Rpc.GetServerStatusReq;
import com.the9.daisy.network.proto.Rpc.GetServerStatusAck;
import com.the9.daisy.network.proto.Rpc.GetOnlineCountReq;
import com.the9.daisy.network.proto.Rpc.GetOnlineCountAck;


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
		case RPC_PublishNoticeReq:
			rpcHandler.publishNotice(PublishNoticeReq.parseFrom(msg.getContent()));
			break;
		case RPC_StopServerReq:
			rpcHandler.stopServer(StopServerReq.parseFrom(msg.getContent()));
			break;
		case RPC_PauseServerReq:
			rpcHandler.pauseServer(PauseServerReq.parseFrom(msg.getContent()));
			break;
		case RPC_ResumeServerReq:
			rpcHandler.resumeServer(ResumeServerReq.parseFrom(msg.getContent()));
			break;
		case RPC_OpenServerReq:
			rpcHandler.openServer(OpenServerReq.parseFrom(msg.getContent()));
			break;
		case RPC_GetServerStatusReq:
			retMsg = rpcHandler.getServerStatus(GetServerStatusReq.parseFrom(msg.getContent()));
			msgTypeHelper.getMsgTypeByName(GetServerStatusAck.class.getSimpleName());
			break;
		case RPC_GetOnlineCountReq:
			retMsg = rpcHandler.getOnlineCount(GetOnlineCountReq.parseFrom(msg.getContent()));
			msgTypeHelper.getMsgTypeByName(GetOnlineCountAck.class.getSimpleName());
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
