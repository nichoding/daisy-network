package com.the9.daisy.network.decoder;

import com.the9.daisy.common.exception.ServiceException;

public class SimpleEncrypter implements IEncrypter {

	@Override
	public byte[] encrypt(int index, byte[] input) throws ServiceException {
		if (input.length >= 2) {
			byte tmpByte = input[0];
			input[0] = input[input.length - 1];
			input[input.length - 1] = tmpByte;
			for (int i = index; i < input.length; i += 2) {
				tmpByte = input[i];
				input[i] = (byte) ~(tmpByte << 4 & 0x00F0 | tmpByte >> 4 & 0x000F);
			}
		}
		return input;

	}

	@Override
	public byte[] decrypt(int index, byte[] input) throws ServiceException {
		if (input.length >= 2) {
			byte tmpByte = 0;
			for (int i = index; i < input.length; i += 2) {
				tmpByte = (byte) ~input[i];
				input[i] = (byte) (tmpByte << 4 & 0x00F0 | tmpByte >> 4 & 0x000F);
			}
			tmpByte = input[0];
			input[0] = input[input.length - 1];
			input[input.length - 1] = tmpByte;
		}
		return input;

	}

}
