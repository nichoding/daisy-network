package com.the9.daisy.pdl;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.network.config.PdlConfig;
import com.the9.daisy.network.msg.MsgDispatcher;
import com.the9.daisy.network.server.AbstractService;
import com.the9.daisy.network.server.ServiceType;

/**
 * 
 * @author dingshengheng
 * 
 */
public class PdlService extends AbstractService {
	private static final Logger logger = LoggerFactory
			.getLogger(PdlService.class);
	private final String name;
	private final int port;
	private final MsgDispatcher dispatcher;
	private final PdlConfig pdlConfig;
	private final IPdlServiceHandlerFactory pdlServerHandlerFactory;
	private ServerBootstrap bootstrap;

	public PdlService(final String name, final int port,
			final MsgDispatcher dispatcher, PdlConfig pdlConfig,
			final IPdlServiceHandlerFactory pdlServerHandlerFactory) {
		super();
		this.name = name;
		this.port = port;
		this.dispatcher = dispatcher;
		this.pdlConfig = pdlConfig;
		this.pdlServerHandlerFactory = pdlServerHandlerFactory;
	}

	public MsgDispatcher getMsgDispatcher() {
		return dispatcher;
	}

	@Override
	public String getServerName() {
		return name;
	}

	@Override
	public ServiceType getServiceType() {
		return ServiceType.PDL;
	}

	@Override
	protected void onStart() throws ServiceException {
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new PdlChannelPipelineFactory(
				pdlServerHandlerFactory));
		bootstrap.setOption("child.tcpNoDelay", pdlConfig.isNoDelay());
		bootstrap.setOption("reuseAddress", true);
		bootstrap.bind(new InetSocketAddress(port));
		logger.info("{} listen on {}", getServiceName(), port);
	}

	@Override
	protected void onRun() throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() throws ServiceException {
		if (bootstrap != null) {
			bootstrap.shutdown();
		}
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
