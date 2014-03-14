package com.the9.daisy.network.proto;

import com.the9.daisy.network.proto.Rpc.LoginGameReq;
import com.the9.daisy.network.proto.Rpc.LoginGameAck;
import com.the9.daisy.network.proto.Rpc.LoginOutReq;
import com.the9.daisy.network.proto.Rpc.GetServerConfigReq;
import com.the9.daisy.network.proto.Rpc.GetServerConfigAck;
import com.the9.daisy.network.proto.Rpc.PublishNoticeReq;
import com.the9.daisy.network.proto.Rpc.StopServerReq;
import com.the9.daisy.network.proto.Rpc.PauseServerReq;
import com.the9.daisy.network.proto.Rpc.ResumeServerReq;
import com.the9.daisy.network.proto.Rpc.OpenServerReq;
import com.the9.daisy.network.proto.Rpc.GetServerStatusReq;
import com.the9.daisy.network.proto.Rpc.GetServerStatusAck;
import com.the9.daisy.network.proto.Rpc.GetOnlineCountReq;
import com.the9.daisy.network.proto.Rpc.GetOnlineCountAck;

public interface IRpcHandler {
	LoginGameAck loginGame(LoginGameReq msg);
	void loginOut(LoginOutReq msg);
	GetServerConfigAck getServerConfig(GetServerConfigReq msg);
	void publishNotice(PublishNoticeReq msg);
	void stopServer(StopServerReq msg);
	void pauseServer(PauseServerReq msg);
	void resumeServer(ResumeServerReq msg);
	void openServer(OpenServerReq msg);
	GetServerStatusAck getServerStatus(GetServerStatusReq msg);
	GetOnlineCountAck getOnlineCount(GetOnlineCountReq msg);
}
