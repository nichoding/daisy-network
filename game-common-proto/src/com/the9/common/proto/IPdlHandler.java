package com.the9.common.proto;

import com.the9.daisy.pdl.AbstractSession;
import com.the9.common.proto.Pdl.LoginGame_C2S_Msg;
import com.the9.common.proto.Pdl.LoginOut_C2S_Msg;
import com.the9.common.proto.Pdl.KeepAlive_C2S_Msg;

public interface IPdlHandler {
		void loginGame(LoginGame_C2S_Msg msg, AbstractSession session);
		void loginOut(LoginOut_C2S_Msg msg, AbstractSession session);
		void keepAlive(KeepAlive_C2S_Msg msg, AbstractSession session);
}
