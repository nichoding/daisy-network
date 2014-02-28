package com.the9.common.proto;

import com.the9.daisy.pdl.AbstractSession;
<#list protoList as proto>
import com.the9.common.proto.Pdl.${proto.fullName};
</#list>

public interface IPdlHandler {
	<#list protoList as proto>
		void ${proto.simpleNamePascal}(${proto.fullName} msg, AbstractSession session);
	</#list>
}
