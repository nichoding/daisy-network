package com.the9.daisy.pdl;

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
public class PdlMsgTypeHelper implements IMsgTypeHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(PdlMsgTypeHelper.class);

	private static Map<String, Integer> typeMap4C2S = new HashMap<String, Integer>();
	private static Map<Integer, String> nameMap4C2S = new HashMap<Integer, String>();
	private static Map<String, Integer> typeMap4S2C = new HashMap<String, Integer>();
	private static Map<Integer, String> nameMap4S2C = new HashMap<Integer, String>();

	private final static String PDL_PREFIX = "PDL_";
	private final static String PDL_SUFFIX_C2S = "_C2S_Msg";
	private final static String PDL_SUFFIX_S2C = "_S2C_Msg";

	private final static int PDL_PREFIX_LENGTH = PDL_PREFIX.length();
	private final static int PDL_SUFFIX_C2S_LENGTH = PDL_SUFFIX_C2S.length();
	private final static int PDL_SUFFIX_S2C_LENGTH = PDL_SUFFIX_S2C.length();

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
			if (name.startsWith(PDL_PREFIX)) {
				if (name.endsWith(PDL_SUFFIX_S2C)) {
					name = name.substring(PDL_PREFIX_LENGTH, nameLength
							- PDL_SUFFIX_S2C_LENGTH);
					initS2CMap(typeValue, name);
				} else if (name.endsWith(PDL_SUFFIX_C2S)) {
					name = name.substring(PDL_PREFIX_LENGTH, nameLength
							- PDL_SUFFIX_C2S_LENGTH);
					initC2SMap(typeValue, name);
				} else {
					logger.warn("unkown field:{} ignore!", name);
				}
			} else {
				logger.warn("unkown field:{} ignore!", name);
			}
		}
		logger.info("PdlMsgTypeHelper init finish");
	}

	private static void initC2SMap(int type, String name) {
		typeMap4C2S.put(name, type);
		nameMap4C2S.put(type, name);
	}

	private static void initS2CMap(int type, String name) {
		typeMap4S2C.put(name, type);
		nameMap4S2C.put(type, name);
	}

	public String getMsgNameByType(int type) {
		String name = nameMap4C2S.get(type);
		if (name == null) {
			return nameMap4S2C.get(type);
		}
		return name;
	}

	public int getMsgTypeByName(String fullName) {
		if (fullName.endsWith(PDL_SUFFIX_C2S)) {
			return typeMap4C2S.get(fullName.substring(0, fullName.length()
					- PDL_SUFFIX_C2S_LENGTH));
		} else if (fullName.endsWith(PDL_SUFFIX_S2C)) {
			return typeMap4S2C.get(fullName.substring(0, fullName.length()
					- PDL_SUFFIX_S2C_LENGTH));
		} else {
			throw new ServiceException("fullName is not a pdl msg name");
		}
	}

	@Override
	public MsgCategory getMsgCategory(int type) {
		String s2cName = nameMap4S2C.get(type);
		String c2sName = nameMap4C2S.get(type);
		if (s2cName != null && c2sName == null) {
			return MsgCategory.SEND;
		}
		if (s2cName == null && c2sName != null) {
			return MsgCategory.RECV;
		}
		if (s2cName != null && c2sName != null) {
			return MsgCategory.PAIR;
		}
		if (s2cName == null && c2sName == null) {
			throw new ServiceException("msg type is not found");
		}
		// nerver reach
		return null;
	}

}
