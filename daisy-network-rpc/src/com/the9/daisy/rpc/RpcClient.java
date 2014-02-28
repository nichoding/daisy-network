package com.the9.daisy.rpc;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.common.util.SleepUtil;
import com.the9.daisy.network.config.ConfigManager;
import com.the9.daisy.network.config.ServerConfig;
import com.the9.daisy.network.proto.Daisy;
import com.the9.daisy.network.proto.Daisy.RpcMsg;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcClient {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcClient.class);
	/**
	 * 目标server配置
	 */
	protected final ServerConfig target;
	/**
	 * 本server配置
	 */
	protected final ServerConfig self;

	protected final ConfigManager configManager;

	public ServerConfig getServerConfig() {
		return target;
	}

	public ServerConfig getAppConfig() {
		return self;
	}

	public RpcClient(final ServerConfig self, final ServerConfig target,
			final ConfigManager configManager) {
		this.target = target;
		this.self = self;
		this.configManager = configManager;
	}

	/**
	 * client channel
	 */
	private Channel socketChannel;

	private ClientBootstrap bootstrap;
	/**
	 * 消息id生成器
	 */
	protected final AtomicInteger seqId = new AtomicInteger();
	/**
	 * 初始消息id （2的24次方-1）
	 */
	private static final int LAST_SEQ_ID = 0x00ffffff;

	public void connect() throws ServiceException {
		logger.info(
				"rpc connect to {}({}/{})",
				new Object[] { target.getName(), target.getInternalIp(),
						target.getInternalPort() });
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		final RpcClient rpcClient = this;
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline ret = Channels.pipeline();
				ret.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				ret.addLast("protobufDecoder",
						new ProtobufDecoder(Daisy.RpcMsg.getDefaultInstance()));
				ret.addLast("frameEncoder",
						new ProtobufVarint32LengthFieldPrepender());
				ret.addLast("protobufEncoder", new ProtobufEncoder());
				ret.addLast("handler", new RpcClientHandler(rpcClient, target));
				return ret;
			}
		});
		ChannelFuture cf = null;
		int times = 0;
		while (true) {
			cf = bootstrap.connect(new InetSocketAddress(
					target.getInternalIp(), target.getInternalPort()));
			while (!cf.isDone()) {
				SleepUtil.sleep(10);
			}
			if (cf.isSuccess()) {
				break;
			}
			logger.warn("trying reconnect to {}({}/{})",
					new Object[] { target.getName(), target.getInternalIp(),
							target.getInternalPort() });
			if (++times >= 10) {
				throw new ServiceException(String.format(
						"connect refused:%s(%s/%d)", target.getName(),
						target.getInternalIp(), target.getInternalPort()));
			}
		}
		socketChannel = cf.getChannel();
	}

	public boolean isConnected() {
		if (socketChannel != null) {
			return socketChannel.isConnected();
		}
		return false;
	}

	public void disconnect() throws ServiceException {
		if (isConnected()) {
			logger.info("{} disconnect", socketChannel);
			socketChannel.disconnect();
		}
		if (bootstrap != null) {
			bootstrap.releaseExternalResources();
		}
	}

	protected void writeRequest(RpcMsg msg) throws ServiceException {
		if (!isConnected()) {
			connect();
		}
		socketChannel.write(msg);
	}

	protected RpcMsg readRequest(int seqId) throws ServiceException {
		RpcPairMsgBox box = pairMsgMap.get(seqId);
		try {
			CountDownLatch latch = box.getLatch();
			int rpcTimeout = configManager.getRpcConfig().getTimeout();
			if (rpcTimeout <= 0) {
				latch.await();
			} else {
				latch.await(rpcTimeout, TimeUnit.MILLISECONDS);
			}
		} catch (InterruptedException e) {

			throw new ServiceException(e);
		}
		if (box.getResponse() == null) {
			logger.error("readRequest timeout");
			throw new ServiceException("readRequest timeout");
		}
		return box.getResponse();
	}

	private int buildSeqId() {
		seqId.compareAndSet(LAST_SEQ_ID, 0);
		return (self.getId() << 24) | (seqId.incrementAndGet());
	}

	public RpcMsg buildMsg(int msgType, GeneratedMessage msg) {
		RpcMsg rpcMsg = RpcMsg.newBuilder()
				.setTimestamp(System.currentTimeMillis())
				.setSeqId(buildSeqId()).setType(msgType)
				.setContent(msg.toByteString()).build();
		return rpcMsg;
	}

	public void send(int msgType, GeneratedMessage msg) throws ServiceException {
		RpcMsg rpcMsg = buildMsg(msgType, msg);
		writeRequest(rpcMsg);
	}

	public RpcMsg sendWithReturn(int msgType, GeneratedMessage msg)
			throws ServiceException {
		RpcMsg rpcMsg = buildMsg(msgType, msg);
		writeRequest(rpcMsg);
		putRpcPairMsgBox(rpcMsg);
		return readRequest(rpcMsg.getSeqId());
	}

	static class RpcPairMsgBox {
		private int seqId;
		private RpcMsg request;
		private RpcMsg response;
		private CountDownLatch latch;

		public int getSeqId() {
			return seqId;
		}

		public void setSeqId(int seqId) {
			this.seqId = seqId;
		}

		public RpcMsg getRequest() {
			return request;
		}

		public void setRequest(RpcMsg request) {
			this.request = request;
		}

		public RpcMsg getResponse() {
			return response;
		}

		public void setResponse(RpcMsg response) {
			this.response = response;
		}

		public CountDownLatch getLatch() {
			return latch;
		}

		public void setLatch(CountDownLatch latch) {
			this.latch = latch;
		}

		public void countDown() {
			if (latch != null) {
				latch.countDown();
			}
		}

	}

	private Map<Integer, RpcPairMsgBox> pairMsgMap = new ConcurrentHashMap<Integer, RpcPairMsgBox>();

	public void putRpcPairMsgBox(RpcMsg request) {
		RpcPairMsgBox box = new RpcPairMsgBox();
		box.setSeqId(request.getSeqId());
		box.setRequest(request);
		CountDownLatch latch = new CountDownLatch(1);
		box.setLatch(latch);
		pairMsgMap.put(request.getSeqId(), box);
	}

	public RpcPairMsgBox getRpcPairMsgBox(int seqId) {
		return pairMsgMap.get(seqId);
	}

	public boolean removeRpcPairMsgBox(int seqId) {
		return pairMsgMap.remove(seqId) != null;
	}
}
