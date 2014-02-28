package com.the9.common.proto;

public interface RpcMsgType {
<#list protoList as proto>
	public static final int ${prefix}${proto.fullName} = ${proto.type?string('#####')};
</#list>
}
