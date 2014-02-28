package com.the9.daisy.network.decoder;

import com.the9.daisy.common.exception.ServiceException;

public interface IEncrypter {

	byte[] encrypt(int index, byte[] input) throws ServiceException;

	byte[] decrypt(int index, byte[] input) throws ServiceException;
}
