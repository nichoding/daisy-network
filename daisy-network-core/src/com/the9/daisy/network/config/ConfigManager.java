package com.the9.daisy.network.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.common.util.XmlUtil;

/**
 * 
 * @author dingshengheng
 * 
 */
public class ConfigManager {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigManager.class);

	public ConfigManager(String configFilePath, int currentServerId) {
		super();
		this.configFilePath = configFilePath;
		this.currentServerId = currentServerId;
	}

	private final int currentServerId;

	private final String configFilePath;

	private RpcConfig rpcConfig = new RpcConfig();

	private PdlConfig pdlConfig = new PdlConfig();

	private List<ServerConfig> servers = new ArrayList<ServerConfig>();

	private Map<Integer, ServerConfig> idMap = new HashMap<Integer, ServerConfig>();

	private Map<String, ServerConfig> nameMap = new HashMap<String, ServerConfig>();

	public void load() {
		File file = new File(configFilePath);
		logger.info("start to load config file path={}", file.getAbsolutePath());
		SAXReader reader = new SAXReader();
		Document doc;
		Element root = null;
		try {
			doc = reader.read(file);
			root = doc.getRootElement();
		} catch (Exception e) {
			throw new ServiceException("failed to load config file", e);
		}
		configPdl(root);
		configRpc(root);
		configServers(root);
		logger.info("successfully load config file path={}",
				file.getAbsoluteFile());
	}

	private void configPdl(Element root) {
		Element pdl = XmlUtil.subElement(root, "pdl");
		pdlConfig.setNetworkCompress(XmlUtil.attributeValueBoolean(pdl,
				"networkCompress"));
		pdlConfig.setNetworkCompressTreashold(XmlUtil.attributeValueInt(pdl,
				"networkCompressThreshold"));
		pdlConfig.setNetworkCrypto(XmlUtil.attributeValueBoolean(pdl,
				"networkCrypto"));
		pdlConfig.setSendBuffSize(XmlUtil.attributeValueInt(pdl, "sendBuff"));
		pdlConfig.setRecvBuffSize(XmlUtil.attributeValueInt(pdl, "recvBuff"));
		pdlConfig.setNoDelay(XmlUtil.attributeValueBoolean(pdl, "noDelay"));
	}

	private void configRpc(Element root) {
		Element rpc = XmlUtil.subElement(root, "rpc");
		rpcConfig.setTimeout(XmlUtil.attributeValueInt(rpc, "timeout"));
		rpcConfig.setNoDelay(XmlUtil.attributeValueBoolean(rpc, "noDelay"));
		rpcConfig.setSendBuffSize(XmlUtil.attributeValueInt(rpc, "sendBuff"));
		rpcConfig.setRecvBuffSize(XmlUtil.attributeValueInt(rpc, "recvBuff"));
	}

	@SuppressWarnings("unchecked")
	private void configServers(Element root) {
		Element serverList = XmlUtil.subElement(root, "servers");
		for (Element e : (List<Element>) serverList.elements()) {
			ServerConfig config = new ServerConfig();
			config.setId(XmlUtil.attributeValueInt(e, "id"));
			config.setName(XmlUtil.attributeValueString(e, "name"));
			config.setType(XmlUtil.attributeValueInt(e, "type"));
			config.setInternalIp(XmlUtil.attributeValueString(e, "internalIp"));
			config.setInternalPort(XmlUtil.attributeValueInt(e, "internalPort"));
			config.setExternalPort(XmlUtil.attributeValueInt(e, "externalPort",
					-1));
			servers.add(config);
			idMap.put(config.getId(), config);
			nameMap.put(config.getName(), config);
		}
	}

	public List<ServerConfig> getAllServerConfig() {
		return servers;
	}

	public ServerConfig getServerConfigById(int serverId) {
		return idMap.get(serverId);
	}

	public ServerConfig getServerConfigById(String serverName) {
		return nameMap.get(serverName);
	}

	public ServerConfig getCurrentServerConfig() {
		return idMap.get(currentServerId);
	}

	public int getCurrentServerId() {
		return currentServerId;
	}

	public RpcConfig getRpcConfig() {
		return rpcConfig;
	}

	public PdlConfig getPdlConfig() {
		return pdlConfig;
	}

}
