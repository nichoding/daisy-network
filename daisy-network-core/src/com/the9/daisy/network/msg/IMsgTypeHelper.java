package com.the9.daisy.network.msg;


/**
 * 
 * @author dingshengheng
 * 
 */
public interface IMsgTypeHelper {
	@SuppressWarnings("rawtypes")
	void init(Class clazz);

	String getMsgNameByType(int type);

	int getMsgTypeByName(String fullName);

	MsgCategory getMsgCategory(int type);
}
