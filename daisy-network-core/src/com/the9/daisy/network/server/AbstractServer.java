package com.the9.daisy.network.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServer {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractServer.class);

	protected int serverId;

	public AbstractServer(int serverId) {
		super();
		this.serverId = serverId;
	}

	protected Map<String, AbstractService> serviceMap = new HashMap<String, AbstractService>();

	public void register(AbstractService service) {
		String serviceName = service.getServiceName();
		if (serviceMap.get(serviceName) == null) {
			serviceMap.put(serviceName, service);
		}
	}

	protected abstract void onStart();

	public void start() {
		long beginTime = System.currentTimeMillis();
		try {
			onStart();
		} catch (Exception e) {
			logger.info("error exists on start serverId={} will exits.",
					serverId, e);
			this.stop();
			return;
		}
		for (AbstractService service : serviceMap.values()) {
			service.start();
		}
		logger.info("serverId={} start completely in {} ms!", serverId,
				(System.currentTimeMillis() - beginTime));
	}

	protected abstract void onStop();

	public void stop() {
		onStop();
		for (AbstractService service : serviceMap.values()) {
			service.stop();
		}
		logger.info("serverId={} stop completely,bye-bye!", serverId);
	}
}
