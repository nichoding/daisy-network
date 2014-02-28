package com.the9.common.proto;

import com.the9.common.proto.Rpc.LoginGameReq;
import com.the9.common.proto.Rpc.LoginGameAck;
import com.the9.common.proto.Rpc.LoginOutReq;
import com.the9.common.proto.Rpc.GetServerConfigReq;
import com.the9.common.proto.Rpc.GetServerConfigAck;

public interface IRpcHandler {
	LoginGameAck loginGame(LoginGameReq msg);
	void loginOut(LoginOutReq msg);
	GetServerConfigAck getServerConfig(GetServerConfigReq msg);
}
