package com.the9.daisy.pdl;

import java.io.IOException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.network.msg.AbstractMsgDecoder;
import com.the9.daisy.network.msg.Msg;
import com.the9.daisy.network.proto.Daisy.GameMsg;

/**
 * 
 * @author dingshengheng
 * 
 */
public abstract class AbstractPdlServiceHandler extends SimpleChannelHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractPdlServiceHandler.class);

	protected final PdlService pdlServer;

	protected final AbstractMsgDecoder msgDecoder;

	public AbstractPdlServiceHandler(PdlService pdlServer, AbstractMsgDecoder msgDecoder) {
		super();
		this.pdlServer = pdlServer;
		this.msgDecoder = msgDecoder;
	}

	protected abstract void onChannelConnected(ChannelHandlerContext ctx,
			ChannelStateEvent e);

	protected abstract void onChannelClosed(ChannelHandlerContext ctx,
			ChannelStateEvent e);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		onChannelConnected(ctx, e);
	}

	/**
	 * 玩家断开，登出游戏
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	@Override
	public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		logger.info("服务器主动关闭连接:{}", ctx.getChannel());
		ctx.sendDownstream(e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		logger.info("{} channelDisconnected", ctx.getChannel());
	}

	/**
	 * 接受到来自客户端的消息
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (e.getMessage() instanceof GameMsg) {
			GameMsg gameMsg = (GameMsg) e.getMessage();
			try {
				// 对gameMsg的解压缩，解密
				Msg msg = msgDecoder.decodeMsg(gameMsg);
				pdlServer.getMsgDispatcher().diapacth(
						new PdlMsgEvent(ctx.getChannel(), msg));
			} catch (ServiceException ex) {
				logger.error("processGameMsg failed:" + ex.getMessage(), ex);
			}
		} else {
			logger.error("unkown msg :{}", e.getMessage());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if (e.getCause() instanceof IOException) {
			logger.warn("error exists ", e.getCause());
		} else {
			logger.error("server failed:", e.getCause());
		}
		if (e.getChannel().isConnected()) {
			e.getChannel().close();
		}
	}

}
