package com.the9.daisy.network.proto.gen;

public class ProtoConfig {
	private final int msgTypeBegin;
	private final String prefix;
	private final String oneSuffix;
	private final String otherSuffix;

	public ProtoConfig(int msgTypeBegin, String prefix, String oneSuffix,
			String otherSuffix) {
		super();
		this.msgTypeBegin = msgTypeBegin;
		this.prefix = prefix;
		this.oneSuffix = oneSuffix;
		this.otherSuffix = otherSuffix;
	}

	public int getMsgTypeBegin() {
		return msgTypeBegin;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getOneSuffix() {
		return oneSuffix;
	}

	public String getOtherSuffix() {
		return otherSuffix;
	}

	public int getOneSuffixLength() {
		return oneSuffix.length();
	}

	public int getOtherSuffixLength() {
		return otherSuffix.length();
	}
}
