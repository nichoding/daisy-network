package com.the9.daisy.network.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;

/**
 * 
 * @author dingshengheng
 * 
 */
public abstract class AbstractService {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractService.class);

	private volatile ServiceState state = ServiceState.NEW;

	public AbstractService() {
		Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
	}

	static class ShutdownThread extends Thread {
		private final AbstractService service;

		public ShutdownThread(AbstractService service) {
			this.service = service;
		}

		@Override
		public void run() {
			if (service != null && !service.isStoppingOrTerminated()) {
				service.stop();
			}
		}
	}

	public abstract String getServerName();

	public abstract ServiceType getServiceType();

	public String getServiceName() {
		return new StringBuilder(getServerName()).append("-")
				.append(getServiceType().toString()).toString();
	}

	public ServiceState getState() {
		return state;
	}

	public boolean isRunning() {
		return state == ServiceState.RUNNING;
	}

	public boolean isStoppingOrTerminated() {
		return state == ServiceState.STOPPING
				|| state == ServiceState.TERMINATED;
	}

	protected abstract void onStart() throws ServiceException;

	protected abstract void onRun() throws ServiceException;

	protected abstract void onStop() throws ServiceException;

	protected abstract void onPause() throws ServiceException;

	protected abstract void onResume() throws ServiceException;

	public void start() {
		logger.info("starting service name={}", getServiceName());
		long startTime = System.currentTimeMillis();
		if (state != ServiceState.NEW) {
			logger.error("invalid state={}", state);
		}
		state = ServiceState.STARTING;
		try {
			onStart();
			state = ServiceState.RUNNING;
			logger.info("{} started successfully,timecost={} ms",
					getServiceName(), (System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("failed to start service,name=" + getServiceName(), e);
		}
	}

	public void stop() {
		logger.info("stopping service name={}", getServiceName());
		if (state == ServiceState.NEW || state == ServiceState.STOPPING
				|| state == ServiceState.TERMINATED) {
			logger.error("invalid state={}", state);
		}
		state = ServiceState.STOPPING;
		try {
			onStop();
			state = ServiceState.TERMINATED;
			logger.info("stop service name={} successfully", getServiceName());
		} catch (Exception e) {
			logger.error("failed to stopping service=" + getServiceName(), e);
		}
	}

	public void pause() {
		logger.info("pausing service name={}", getServiceName());
		if (state != ServiceState.RUNNING) {
			logger.error("invalid state={}", state);
		}
		state = ServiceState.PAUSING;
		try {
			onPause();
			state = ServiceState.PAUSED;
			logger.info("pause service name={} completely", getServiceName());
		} catch (Exception e) {
			logger.error("failed to pausing service=" + getServiceName(), e);
		}

	}

	public void resume() {
		logger.info("resuming service={}", getServiceName());
		if (state != ServiceState.PAUSED) {
			logger.error("invalid state={}", state);
		}
		state = ServiceState.RESUMING;
		try {
			onResume();
			state = ServiceState.RUNNING;
			logger.info("resume service name={} completely", getServiceName());
		} catch (Exception e) {
			logger.error("failed to resuming service=" + getServiceName(), e);
		}
	}

}
