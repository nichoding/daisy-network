package com.the9.daisy.network.decoder;

import com.the9.daisy.common.exception.ServiceException;

public interface ICompressor {

	byte[] compress(byte[] input) throws ServiceException;

	byte[] uncompress(byte[] input) throws ServiceException;
}
