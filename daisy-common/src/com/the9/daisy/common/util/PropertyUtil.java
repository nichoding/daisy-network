package com.the9.daisy.common.util;

import java.text.MessageFormat;
import java.util.Properties;

import com.the9.daisy.common.exception.ServiceException;
/**
 * 
 * @author dingshengheng
 *
 */
public abstract class PropertyUtil {
	public static int getInt(Properties pro, String key) {
		if (pro == null) {
			throw new ServiceException("Properties==null");
		}
		String value = pro.getProperty(key);
		if (value == null) {
			throw new ServiceException(MessageFormat.format("缺少属性%s", key));
		} else {
			return Integer.parseInt(value);
		}
	}

	public static String getString(Properties pro, String key) {
		if (pro == null) {
			throw new ServiceException("Properties==null");
		}
		String value = pro.getProperty(key);
		if (value == null) {
			throw new ServiceException(MessageFormat.format("缺少属性%s", key));
		} else {
			return value;
		}
	}

	public static boolean getBoolean(Properties pro, String key) {
		if (pro == null) {
			throw new ServiceException("Properties==null");
		}
		String value = pro.getProperty(key);
		if (value == null) {
			throw new ServiceException(MessageFormat.format("缺少属性%s", key));
		} else {
			return Boolean.parseBoolean(value);
		}
	}
}
