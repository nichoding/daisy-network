package com.the9.daisy.pdl;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.the9.daisy.network.proto.Daisy.GameMsg;

/**
 * 
 * @author dingshengheng
 * 
 */
public class PdlChannelPipelineFactory implements ChannelPipelineFactory {

	private final IPdlServiceHandlerFactory pdlServerHandlerFactory;

	public PdlChannelPipelineFactory(
			IPdlServiceHandlerFactory pdlServerHandlerFactory) {
		super();
		this.pdlServerHandlerFactory = pdlServerHandlerFactory;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline ret = Channels.pipeline();
		ret.addLast("executor", new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(8, 1048576, 1048576)));
		ret.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		ret.addLast("protobufDecoder",
				new ProtobufDecoder(GameMsg.getDefaultInstance()));
		ret.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		ret.addLast("protobufEncoder", new ProtobufEncoder());
		// ret.addLast("heartbeat", new IdleStateAwareChannelHandler() {
		// @Override
		// public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
		// throws Exception {
		// logger.warn("close the channel hearbeat {} type={}",
		// e.getChannel(), e.getState());
		// e.getChannel().close();
		// }
		// });
		ret.addLast("handler", pdlServerHandlerFactory.get());
		return ret;
	}

}
