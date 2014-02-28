package com.the9.daisy.network.msg;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 
 * @author dingshengheng
 * 
 */
public abstract class AbstractMsgEvent {

	protected static enum EventType {
		PDL, RPC
	}

	protected final long createTime;// 事件创建时间

	public AbstractMsgEvent(long createTime) {
		this.createTime = createTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public abstract void handle(MsgDispatcher dispacher)
			throws InvalidProtocolBufferException;

	public abstract EventType getEventType();

	public abstract int getMsgType();

}
