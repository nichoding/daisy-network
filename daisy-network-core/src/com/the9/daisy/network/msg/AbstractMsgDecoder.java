package com.the9.daisy.network.msg;

import com.google.protobuf.ByteString;
import com.the9.daisy.network.decoder.ICompressor;
import com.the9.daisy.network.decoder.IEncrypter;
import com.the9.daisy.network.proto.Daisy.GameMsg;

public abstract class AbstractMsgDecoder {

	public abstract ICompressor getCompressor();

	public abstract IEncrypter getEncrypter();

	public final static int GAMEMSG_FLAG_COMPRESS = 1;
	public final static int GAMEMSG_FLAG_ENCRYPT = 2;

	public GameMsg encodeMsg(Msg msg, boolean networkCompress,
			int networkCompressTreashold, boolean networkCrypto) {
		int flag = 0;
		byte[] data = msg.getContent().toByteArray();
		int length = msg.getContent().size();
		if (networkCompress && length >= networkCompressTreashold) {
			getCompressor().compress(data);
			flag |= GAMEMSG_FLAG_COMPRESS;
		}
		if (networkCrypto) {
			flag |= GAMEMSG_FLAG_ENCRYPT;
			data = getEncrypter().encrypt(data.length % 2, data);
		}
		GameMsg gameMsg = GameMsg.newBuilder().setId(msg.getId())
				.setType(msg.getType()).setFlag(flag)
				.setTimestamp(msg.getTimestamp())
				.setContent(ByteString.copyFrom(data)).build();
		return gameMsg;
	}

	public Msg decodeMsg(GameMsg msg) {
		byte[] data = msg.getContent().toByteArray();
		int length = msg.getContent().size();
		if ((msg.getFlag() & GAMEMSG_FLAG_ENCRYPT) != 0) {
			data = getEncrypter().decrypt(length % 2, data);
		}
		if ((msg.getFlag() & GAMEMSG_FLAG_COMPRESS) != 0) {
			data = getCompressor().uncompress(data);
		}
		return new Msg(msg.getId(), msg.getType(), ByteString.copyFrom(data),
				msg.getTimestamp());
	}

}
