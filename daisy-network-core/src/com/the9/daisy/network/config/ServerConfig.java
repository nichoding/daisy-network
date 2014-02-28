package com.the9.daisy.network.config;

/**
 * 
 * @author dingshengheng
 * 
 */
public class ServerConfig {
	/**
	 * server id
	 */
	private int id;
	/**
	 * server type
	 */
	private int type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 内网ip
	 */
	private String internalIp;
	/**
	 * 对内端口
	 */
	private int internalPort;
	/**
	 * 对外端口
	 */
	private int externalPort;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	public int getInternalPort() {
		return internalPort;
	}

	public void setInternalPort(int internalPort) {
		this.internalPort = internalPort;
	}

	public int getExternalPort() {
		return externalPort;
	}

	public void setExternalPort(int externalPort) {
		this.externalPort = externalPort;
	}

}
