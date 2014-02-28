package com.the9.daisy.network.decoder;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.the9.daisy.common.exception.ServiceException;

public class ZipCompressor implements ICompressor {

	@Override
	public byte[] compress(byte[] input) throws ServiceException {
		int inputLength = input.length;
		byte[] ret = null;
		Deflater deflater = new Deflater();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(inputLength);
		try {
			deflater.setInput(input);
			deflater.finish();
			byte[] buf = new byte[1024];
			while (!deflater.finished()) {
				int got = deflater.deflate(buf);
				baos.write(buf, 0, got);
			}
			ret = baos.toByteArray();
		} finally {
			deflater.end();
			try {
				baos.close();
			} catch (Exception e) {
			}
		}
		return ret;
	}

	@Override
	public byte[] uncompress(byte[] input) throws ServiceException {
		if (input.length == 0) {

			return input;
		}
		byte[] ret = null;
		Inflater decompressor = new Inflater();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
		try {
			decompressor.reset();
			decompressor.setInput(input, 0, input.length);
			byte[] buf = new byte[1024];
			int got = 0;
			while (!decompressor.finished()) {
				try {
					got = decompressor.inflate(buf);
				} catch (DataFormatException e) {
					throw new ServiceException(e);
				}
				baos.write(buf, 0, got);
			}
			ret = baos.toByteArray();
		} finally {
			decompressor.end();
			try {
				baos.close();
			} catch (Exception e) {
			}
		}
		return ret;
	}

}
