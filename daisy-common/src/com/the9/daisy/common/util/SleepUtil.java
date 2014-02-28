package com.the9.daisy.common.util;


/**
 * 
 * @author dingshengheng
 * 
 */
public abstract class SleepUtil {

	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// ignore
		}
	}

}
