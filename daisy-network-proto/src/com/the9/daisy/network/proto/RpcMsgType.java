package com.the9.daisy.network.proto;

public interface RpcMsgType {
	public static final int RPC_LoginGameReq = 20001;
	public static final int RPC_LoginGameAck = 20002;
	public static final int RPC_LoginOutReq = 20003;
	public static final int RPC_GetServerConfigReq = 20005;
	public static final int RPC_GetServerConfigAck = 20006;
	public static final int RPC_PublishNoticeReq = 20007;
	public static final int RPC_StopServerReq = 20009;
	public static final int RPC_PauseServerReq = 20011;
	public static final int RPC_ResumeServerReq = 20013;
	public static final int RPC_OpenServerReq = 20015;
	public static final int RPC_GetServerStatusReq = 20017;
	public static final int RPC_GetServerStatusAck = 20018;
	public static final int RPC_GetOnlineCountReq = 20019;
	public static final int RPC_GetOnlineCountAck = 20020;
}
