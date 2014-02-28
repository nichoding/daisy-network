package com.the9.daisy.network.msg;

import com.google.protobuf.ByteString;

/**
 * 
 * @author dingshengheng
 * 
 */
public class Msg {
	private final int id;
	private final int type;
	private final ByteString content;
	private final long timestamp;

	public Msg(int id, int type, ByteString content, long timestamp) {
		super();
		this.id = id;
		this.type = type;
		this.content = content;
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public ByteString getContent() {
		return content;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
