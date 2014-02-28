package com.the9.daisy.network.server;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServer {

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
		onStart();
		for (AbstractService service : serviceMap.values()) {
			service.start();
		}
	}

	protected abstract void onStop();

	public void stop() {
		onStop();
		for (AbstractService service : serviceMap.values()) {
			service.stop();
		}
	}
}
