package com.the9.daisy.rpc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.the9.daisy.common.exception.ServiceException;
import com.the9.daisy.network.msg.IMsgTypeHelper;
import com.the9.daisy.network.msg.MsgCategory;

/**
 * 
 * @author dingshengheng
 * 
 */
public class RpcMsgTypeHelper implements IMsgTypeHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(RpcMsgTypeHelper.class);

	private static Map<String, Integer> typeMap4Req = new HashMap<String, Integer>();
	private static Map<Integer, String> nameMap4Req = new HashMap<Integer, String>();
	private static Map<String, Integer> typeMap4Ack = new HashMap<String, Integer>();
	private static Map<Integer, String> nameMap4Ack = new HashMap<Integer, String>();

	private final static String RPC_PREFIX = "RPC_";
	private final static String RPC_SUFFIX_REQ = "Req";
	private final static String RPC_SUFFIX_ACK = "Ack";

	private final static int RPC_PREFIX_LENGTH = RPC_PREFIX.length();
	private final static int RPCSUFFIX_REQ_LENGTH = RPC_SUFFIX_REQ.length();
	private final static int RPCSUFFIX_ACK_LENGTH = RPC_SUFFIX_ACK.length();

	@SuppressWarnings("rawtypes")
	public void init(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field e : fields) {
			String name = e.getName();
			int nameLength = name.length();
			int typeValue = 0;
			try {
				typeValue = e.getInt(null);
			} catch (Exception ex) {
				throw new ServiceException("fail to init MsgTypeHelper", ex);
			}
			if (name.startsWith(RPC_PREFIX)) {
				if (name.endsWith(RPC_SUFFIX_REQ)) {
					name = name.substring(RPC_PREFIX_LENGTH, nameLength
							- RPCSUFFIX_REQ_LENGTH);
					initReqMap(typeValue, name);
				} else if (name.endsWith(RPC_SUFFIX_ACK)) {
					name = name.substring(RPC_PREFIX_LENGTH, nameLength
							- RPCSUFFIX_ACK_LENGTH);
					initAckMap(typeValue, name);
				} else {
					logger.warn("unkown field:{} ignore!", name);
				}
			} else {
				logger.warn("unkown field:{} ignore!", name);
			}
		}
		logger.info("RpcMsgTypeHelper init finish");
	}

	private void initReqMap(int type, String name) {
		typeMap4Req.put(name, type);
		nameMap4Req.put(type, name);
	}

	private void initAckMap(int type, String name) {
		typeMap4Ack.put(name, type);
		nameMap4Ack.put(type, name);
	}

	public String getMsgNameByType(int type) {
		String name = nameMap4Req.get(type);
		if (name == null) {
			return nameMap4Ack.get(type);
		}
		return name;
	}

	public int getMsgTypeByName(String fullName) {
		if (fullName.endsWith(RPC_SUFFIX_REQ)) {
			return typeMap4Req.get(fullName.substring(0, fullName.length()
					- RPCSUFFIX_REQ_LENGTH));
		} else if (fullName.endsWith(RPC_SUFFIX_ACK)) {
			return typeMap4Ack.get(fullName.substring(0, fullName.length()
					- RPCSUFFIX_ACK_LENGTH));
		} else {
			throw new ServiceException("fullName is not a rpc msg name");
		}
	}

	@Override
	public MsgCategory getMsgCategory(int type) {
		String reqName = nameMap4Req.get(type);
		Integer ackType = typeMap4Ack.get(reqName);
		if (reqName != null && ackType == null) {
			return MsgCategory.RECV;
		}
		if (reqName == null && ackType != null) {
			return MsgCategory.SEND;
		}
		if (reqName != null && ackType != null) {
			return MsgCategory.PAIR;
		}
		if (reqName == null && ackType == null) {
			throw new ServiceException("msg type is not found");
		}
		// nerver reach
		return null;
	}
}
