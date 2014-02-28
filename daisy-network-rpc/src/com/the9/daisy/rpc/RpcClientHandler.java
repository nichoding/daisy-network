package com.the9.daisy.rpc;

import java.net.ConnectException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.network.config.ServerConfig;
import com.the9.daisy.network.proto.Daisy.RpcMsg;
import com.the9.daisy.rpc.RpcClient.RpcPairMsgBox;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcClientHandler extends SimpleChannelHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RpcClientHandler.class);

	private final ServerConfig serverConfig;

	private final RpcClient rpcClient;

	public RpcClientHandler(RpcClient rpcClient, ServerConfig serverConfig) {
		this.rpcClient = rpcClient;
		this.serverConfig = serverConfig;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (e.getMessage() instanceof RpcMsg) {
			RpcMsg rpcMsg = (RpcMsg) e.getMessage();
			RpcPairMsgBox msgBox = rpcClient
					.getRpcPairMsgBox(rpcMsg.getSeqId());
			if (msgBox != null) {
				msgBox.setResponse(rpcMsg);
				msgBox.countDown();
			} else {
				logger.error("unknown msgType={} seqId={}", rpcMsg.getType(),
						rpcMsg.getSeqId());
			}
		} else {
			logger.error("unknown msg={}", e.getMessage());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if (e.getCause() instanceof ConnectException) {
			logger.warn("connect refused :{}({}/{})", new Object[] {
					serverConfig.getName(), serverConfig.getInternalIp(),
					serverConfig.getInternalPort() });
		} else {
			logger.error("exception exists:", e.getCause());
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		logger.info(
				"connected :{},{},{}({})",
				new Object[] { serverConfig.getName(),
						serverConfig.getInternalIp(),
						serverConfig.getInternalPort(),
						ctx.getChannel().getLocalAddress() });
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		logger.info(
				"disconnected :{},{},{}({})",
				new Object[] { serverConfig.getName(),
						serverConfig.getInternalIp(),
						serverConfig.getInternalPort(),
						ctx.getChannel().getLocalAddress() });
	}
}
