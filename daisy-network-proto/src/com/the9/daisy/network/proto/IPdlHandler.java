package com.the9.daisy.network.proto;

import com.the9.daisy.pdl.AbstractSession;
import com.the9.daisy.network.proto.Pdl.LoginGame_C2S_Msg;
import com.the9.daisy.network.proto.Pdl.LoginOut_C2S_Msg;
import com.the9.daisy.network.proto.Pdl.KeepAlive_C2S_Msg;

public interface IPdlHandler {
		void loginGame(LoginGame_C2S_Msg msg, AbstractSession session);
		void loginOut(LoginOut_C2S_Msg msg, AbstractSession session);
		void keepAlive(KeepAlive_C2S_Msg msg, AbstractSession session);
}
