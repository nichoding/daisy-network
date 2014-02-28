package com.the9.daisy.common.util;

import org.dom4j.Element;

import com.the9.daisy.common.exception.ServiceException;

public abstract class XmlUtil {

	public static String attributeValueString(Element element, String attr) {
		if (element == null) {
			throw new ServiceException("element is null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new ServiceException(String.format("缺少属性%s：%s", attr,
					element.asXML()));
		}
		return value;
	}

	public static int attributeValueInt(Element element, String attr) {
		if (element == null) {
			throw new ServiceException("element is null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new ServiceException(String.format("缺少属性%s：%s", attr,
					element.asXML()));
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ServiceException(String.format("属性%s不是数值类型：%s", attr,
					element.asXML()));
		}
	}

	public static int attributeValueInt(Element element, String attr,
			int defaultValue) {
		if (element == null) {
			throw new ServiceException("element is null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ServiceException(String.format("属性%s不是数值类型：%s", attr,
					element.asXML()));
		}
	}

	public static boolean attributeValueBoolean(Element element, String attr) {
		if (element == null) {
			throw new ServiceException("element is null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new ServiceException(String.format("缺少属性%s：%s", attr,
					element.asXML()));
		}
		try {
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			throw new ServiceException(String.format("属性%s不是布尔类型：%s", attr,
					element.asXML()));
		}
	}

	public static boolean attributeValueBoolean(Element element, String attr,
			boolean defaultValue) {
		if (element == null) {
			throw new ServiceException("element is null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			throw new ServiceException(String.format("属性%s不是布尔类型：%s", attr,
					element.asXML()));
		}
	}

	public static Element subElement(Element parent, String name)
			throws ServiceException {
		if (parent == null) {
			throw new ServiceException("parent is null");
		}
		Element result = parent.element(name);
		if (result == null) {
			throw new ServiceException(String.format("找不到%s节点的子节点%s",
					parent.getName(), name));
		}
		return result;
	}
}
