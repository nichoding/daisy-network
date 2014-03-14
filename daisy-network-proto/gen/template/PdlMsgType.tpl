package com.the9.daisy.network.proto;

public interface PdlMsgType {
<#list protoList as proto>
	public static final int ${prefix}${proto.fullName} = ${proto.type?string('#####')};
</#list>
}
