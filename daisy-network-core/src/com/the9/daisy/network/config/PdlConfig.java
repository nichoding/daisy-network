package com.the9.daisy.network.config;
/**
 * 
 * @author dingshengheng
 *
 */
public class PdlConfig {
	/**
	 * 是否对网络数据报进行压缩
	 */
	private boolean networkCompress;
	/**
	 * 网络数据报压缩阀值
	 */
	private int networkCompressTreashold;
	/**
	 * 是否对网路数据报进行加密
	 */
	private boolean networkCrypto;
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

	public boolean isNetworkCompress() {
		return networkCompress;
	}

	public void setNetworkCompress(boolean networkCompress) {
		this.networkCompress = networkCompress;
	}

	public int getNetworkCompressTreashold() {
		return networkCompressTreashold;
	}

	public void setNetworkCompressTreashold(int networkCompressTreashold) {
		this.networkCompressTreashold = networkCompressTreashold;
	}

	public boolean isNetworkCrypto() {
		return networkCrypto;
	}

	public void setNetworkCrypto(boolean networkCrypto) {
		this.networkCrypto = networkCrypto;
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

	public boolean isNoDelay() {
		return noDelay;
	}

	public void setNoDelay(boolean noDelay) {
		this.noDelay = noDelay;
	}

}
