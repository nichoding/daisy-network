package com.the9.daisy.network.msg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 
 * @author dingshengheng
 * 
 */
public class MsgDispatcher {
	private static final Logger logger = LoggerFactory
			.getLogger(MsgDispatcher.class);

	private final IMsgProcessor pdlProcessor;

	private final IMsgProcessor rpcProcessor;

	public MsgDispatcher(IMsgProcessor pdlProcessor, IMsgProcessor rpcProcessor) {
		this.pdlProcessor = pdlProcessor;
		this.rpcProcessor = rpcProcessor;
	}

	private ExecutorService pool = Executors.newCachedThreadPool();

	public void diapacth(final AbstractMsgEvent event) {
		final MsgDispatcher dispacher = this;
		pool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					event.handle(dispacher);
				} catch (InvalidProtocolBufferException e) {
					logger.error("error exist:", e);
				}
				logger.info(
						"process msg event={},type={},delay={} ms",
						new Object[] {
								event.getEventType().toString(),
								event.getMsgType(),
								(System.currentTimeMillis() - event
										.getCreateTime()) });
			}
		});
	}

	public IMsgProcessor getPdlProcessor() {
		return pdlProcessor;
	}

	public IMsgProcessor getRpcProcessor() {
		return rpcProcessor;
	}

}
