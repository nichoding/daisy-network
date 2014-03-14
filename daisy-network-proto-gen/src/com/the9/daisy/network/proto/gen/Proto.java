package com.the9.daisy.network.proto.gen;

public class Proto implements Comparable<Proto> {
	private int type;
	private String simpleName;
	private String simpleNamePascal;
	private String fullName;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSimpleNamePascal() {
		return simpleNamePascal;
	}

	public void setSimpleNamePascal(String simpleNamePascal) {
		this.simpleNamePascal = simpleNamePascal;
	}

	@Override
	public int compareTo(Proto o) {
		if (this.type < o.getType()) {
			return -1;
		}
		return 1;
	}

}
