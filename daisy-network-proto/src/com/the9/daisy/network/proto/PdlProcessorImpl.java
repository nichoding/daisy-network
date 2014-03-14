package com.the9.daisy.network.proto;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.msg.Msg;
import com.the9.daisy.network.msg.MsgProcessorAdapter;
import com.the9.daisy.pdl.ISessionManager;
import com.the9.daisy.network.proto.Pdl.LoginGame_C2S_Msg;
import com.the9.daisy.network.proto.Pdl.LoginOut_C2S_Msg;
import com.the9.daisy.network.proto.Pdl.KeepAlive_C2S_Msg;

public class PdlProcessorImpl extends MsgProcessorAdapter implements PdlMsgType {
	private static final Logger logger = LoggerFactory
			.getLogger(PdlProcessorImpl.class);

	private final IPdlHandler pdlHandler;

	private final ISessionManager sessionManager;

	public PdlProcessorImpl(IPdlHandler pdlHandler,
			ISessionManager sessionService) {
		super();
		this.pdlHandler = pdlHandler;
		this.sessionManager = sessionService;
	}

	@Override
	public void process(Channel channel, Msg msg)
			throws InvalidProtocolBufferException {
		switch (msg.getType()) {
		case PDL_LoginGame_C2S_Msg:
			pdlHandler.loginGame(LoginGame_C2S_Msg.parseFrom(msg.getContent()),sessionManager.getSession(channel));
			break;
		case PDL_LoginOut_C2S_Msg:
			pdlHandler.loginOut(LoginOut_C2S_Msg.parseFrom(msg.getContent()),sessionManager.getSession(channel));
			break;
		case PDL_KeepAlive_C2S_Msg:
			pdlHandler.keepAlive(KeepAlive_C2S_Msg.parseFrom(msg.getContent()),sessionManager.getSession(channel));
			break;
		default:
			logger.error("unkown msg type:{}", msg.getType());
			break;
		}
	}
}
