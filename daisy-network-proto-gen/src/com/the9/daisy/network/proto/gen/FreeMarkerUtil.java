package com.the9.daisy.network.proto.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(FreeMarkerUtil.class);

	public static void analysisTemplate(String templatePath,
			String templateName, String filePath, String fileName,
			Map<?, ?> root) {
		try {
			Configuration config = new Configuration();
			logger.info("templatePath={}", templatePath);
			config.setDirectoryForTemplateLoading(new File(templatePath));
			config.setObjectWrapper(new DefaultObjectWrapper());
			Template template = config.getTemplate(templateName, "UTF-8");
			String file = filePath + File.separatorChar + fileName;
			logger.info("target filepath={}", file);
			FileOutputStream fos = new FileOutputStream(file);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("IOException:", e);
		} catch (TemplateException e) {
			logger.error("TemplateException:", e);
		}
	}
}