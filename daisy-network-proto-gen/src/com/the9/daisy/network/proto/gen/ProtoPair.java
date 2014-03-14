package com.the9.daisy.network.proto.gen;

public class ProtoPair implements Comparable<ProtoPair> {

	private Proto request;
	private Proto response;

	public Proto getRequest() {
		return request;
	}

	public void setRequest(Proto request) {
		this.request = request;
	}

	public Proto getResponse() {
		return response;
	}

	public void setResponse(Proto response) {
		this.response = response;
	}

	@Override
	public int compareTo(ProtoPair o) {
		if (this.request != null && o.getRequest() != null) {
			if (this.request.getType() < o.getRequest().getType()) {
				return -1;
			} else {
				return 1;
			}
		}
		if (this.request != null && o.getRequest() == null) {
			if (this.request.getType() < o.getResponse().getType()) {
				return -1;
			} else {
				return 1;
			}
		}
		if (this.request == null && o.getRequest() != null) {
			if (this.response.getType() < o.getRequest().getType()) {
				return -1;
			} else {
				return 1;
			}
		}
		if (this.request == null && o.getRequest() == null) {
			if (this.response.getType() < o.getResponse().getType()) {
				return -1;
			} else {
				return 1;
			}
		}
		return -1;
	}
}
