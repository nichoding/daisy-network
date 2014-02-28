package com.the9.daisy.rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.the9.daisy.network.config.ServerConfig;
import com.the9.daisy.network.config.ConfigManager;
import com.the9.daisy.network.msg.MsgCategory;
import com.the9.daisy.network.proto.Daisy.RpcMsg;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcEngine {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcEngine.class);

	private RpcMsgTypeHelper rpcMsgTypeHelper;

	protected ConfigManager configManager;

	protected Map<Integer, RpcClient> rpcClients = new HashMap<Integer, RpcClient>();

	public RpcEngine(ConfigManager configManager,
			RpcMsgTypeHelper rpcMsgTypeHelper) {
		super();
		this.configManager = configManager;
		this.rpcMsgTypeHelper = rpcMsgTypeHelper;
	}

	public void init() {
		ServerConfig self = configManager.getCurrentServerConfig();
		List<ServerConfig> configs = configManager.getAllServerConfig();
		for (ServerConfig s : configs) {
			int serverId = s.getId();
			if (serverId == configManager.getCurrentServerId()) {
				continue;
			}
			ServerConfig target = configManager.getServerConfigById(serverId);
			if (target.getType() == self.getType()) {
				continue;
			}
			RpcClient rpcClient = new RpcClient(self, target, configManager);
			rpcClients.put(serverId, rpcClient);
		}
		logger.info("rpc engine init finish");
	}

	public void destroy() {
		for (RpcClient e : rpcClients.values()) {
			e.disconnect();
		}
	}

	public RpcClient getRpcClientById(int serverId) {
		return rpcClients.get(serverId);
	}

	public RpcMsg sendMsg(int serverId, GeneratedMessage msg)
			throws InvalidProtocolBufferException {
		int msgType = rpcMsgTypeHelper.getMsgTypeByName(msg.getClass()
				.getSimpleName());
		MsgCategory category = rpcMsgTypeHelper.getMsgCategory(msgType);
		RpcClient client = getRpcClientById(serverId);
		if (category == MsgCategory.PAIR) {
			return client.sendWithReturn(msgType, msg);
		} else {
			client.send(msgType, msg);
			return null;
		}
	}
}