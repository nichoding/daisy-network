package com.the9.daisy.network.proto;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.network.msg.Msg;
import com.the9.daisy.network.msg.MsgProcessorAdapter;
import com.the9.daisy.pdl.ISessionManager;
<#list protoList as proto>
import com.the9.daisy.network.proto.Pdl.${proto.fullName};
</#list>

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
	public void process(Channel channel, Msg msg){
		try {
			switch (msg.getType()) {
			<#list protoList as proto>
			case ${prefix}${proto.fullName}:
				pdlHandler.${proto.simpleNamePascal}(${proto.fullName}.parseFrom(msg.getContent()),sessionManager.getSession(channel));
				break;
			</#list>
			default:
				logger.error("unkown msg type:{}", msg.getType());
				break;
				}
			} catch (Exception e) {
			logger.error("unkown exception", e);
			throw new ServiceException(e);
		}
	}
}
