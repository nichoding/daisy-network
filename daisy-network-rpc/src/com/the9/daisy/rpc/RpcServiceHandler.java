package com.the9.daisy.rpc;

import java.io.IOException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.network.proto.Daisy.RpcMsg;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcServiceHandler extends SimpleChannelHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcServiceHandler.class);

	protected final RpcService rpcServer;

	public RpcServiceHandler(final RpcService rpcServer) {
		this.rpcServer = rpcServer;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		logger.info("channel connected {}", ctx.getChannel());
		rpcServer.getAllOpenChannels().add(ctx.getChannel());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		logger.info("channel disconnected {}", ctx.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if (e.getCause() instanceof IOException) {
			logger.warn(e.getCause().getMessage());
		} else {
			logger.error("server failed", e.getCause());
		}
		e.getChannel().close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (e.getMessage() instanceof RpcMsg) {
			RpcMsg rpcMsg = (RpcMsg) e.getMessage();
			rpcServer.getgetDispatcher().diapacth(
					new RpcMsgEvent(ctx.getChannel(), rpcMsg));
		} else {
			logger.error("unkown message ignore", e);
		}
	}
}
