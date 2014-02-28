package com.the9.daisy.rpc;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.network.config.RpcConfig;
import com.the9.daisy.network.msg.MsgDispatcher;
import com.the9.daisy.network.proto.Daisy.RpcMsg;
import com.the9.daisy.network.server.AbstractService;
import com.the9.daisy.network.server.ServiceType;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcService extends AbstractService {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcService.class);
	private final int port;
	private final String name;
	private final MsgDispatcher dispatcher;
	private final RpcConfig rpcConfig;
	private ServerBootstrap bootstrap;
	private final ChannelGroup allOpenChannels = new DefaultChannelGroup();

	public RpcService(final String name, final int port,
			final MsgDispatcher dispatcher, final RpcConfig rpcConfig) {
		super();
		this.port = port;
		this.dispatcher = dispatcher;
		this.name = name;
		this.rpcConfig = rpcConfig;
	}

	public MsgDispatcher getgetDispatcher() {
		return dispatcher;
	}

	public ChannelGroup getAllOpenChannels() {
		return allOpenChannels;
	}

	@Override
	public String getServerName() {
		return name;
	}

	@Override
	public ServiceType getServiceType() {
		return ServiceType.RPC;
	}

	@Override
	protected void onStart() throws ServiceException {
		logger.info("{} start on {}", super.getServiceName(), port);
		final ExecutionHandler executionHandler = new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(8, 1048576, 1048576));
		final RpcService thisServer = this;
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline ret = Channels.pipeline();
				ret.addLast("executor", executionHandler);
				ret.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				ret.addLast("protobufDecoder",
						new ProtobufDecoder(RpcMsg.getDefaultInstance()));
				ret.addLast("frameEncoder",
						new ProtobufVarint32LengthFieldPrepender());
				ret.addLast("protobufEncoder", new ProtobufEncoder());
				ret.addLast("handler", new RpcServiceHandler(thisServer));
				return ret;
			}
		});
		bootstrap.setOption("child.tcpNoDelay", rpcConfig.isNoDelay());
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("reuseAddress", true);
		Channel serverChannel = bootstrap.bind(new InetSocketAddress(port));
		allOpenChannels.add(serverChannel);

	}

	@Override
	protected void onStop() throws ServiceException {
		if (bootstrap != null) {
			bootstrap.shutdown();
		}
	}

	@Override
	protected void onRun() throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() throws ServiceException {
		// TODO Auto-generated method stub

	}
}
