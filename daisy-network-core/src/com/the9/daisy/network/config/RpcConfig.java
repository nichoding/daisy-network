package com.the9.daisy.network.config;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcConfig {
	/**
	 * rpc 调用 超时时间(单位毫秒)
	 */
	private int timeout;
	/**
	 * tcp no delay
	 */
	private boolean noDelay;

	/**
	 * socket发送缓冲区大小(单位字节)
	 */
	private int sendBuffSize;
	/**
	 * socket接受缓冲区大小(单位字节)
	 */
	private int recvBuffSize;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isNoDelay() {
		return noDelay;
	}

	public void setNoDelay(boolean noDelay) {
		this.noDelay = noDelay;
	}

	public int getSendBuffSize() {
		return sendBuffSize;
	}

	public void setSendBuffSize(int sendBuffSize) {
		this.sendBuffSize = sendBuffSize;
	}

	public int getRecvBuffSize() {
		return recvBuffSize;
	}

	public void setRecvBuffSize(int recvBuffSize) {
		this.recvBuffSize = recvBuffSize;
	}

}
