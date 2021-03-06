package com.the9.daisy.network.proto;

<#list protoList as proto>
import com.the9.daisy.network.proto.Rpc.${proto.fullName};
</#list>

public interface IRpcHandler {
	<#list pairList as pair>
	<#if pair.response?exists>${pair.response.fullName}<#else>void</#if> ${pair.request.simpleNamePascal}(${pair.request.fullName} msg);
	</#list>
}
