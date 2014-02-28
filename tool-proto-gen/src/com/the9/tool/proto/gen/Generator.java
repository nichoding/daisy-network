package com.the9.tool.proto.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Generator {
	private static final Logger logger = LoggerFactory
			.getLogger(Generator.class);
	private final String templateDir;
	private final ProtoConfig pdlConfig;
	private final ProtoConfig rpcConfig;
	private final String outDir;

	public Generator(ProtoConfig pdlConfig, ProtoConfig rpcConfig,
			String outDir, String templateDir) {
		super();
		this.pdlConfig = pdlConfig;
		this.rpcConfig = rpcConfig;
		this.outDir = outDir;
		this.templateDir = templateDir;
	}

	public void generate(List<String> pdlNameList, List<String> rpcNameList) {
		generatePdl(pdlNameList);
		generateRpc(rpcNameList);
		logger.info("generate finish");
	}

	private void generatePdl(List<String> pdlNameList) {
		Map<String, Proto> c2sMap = new HashMap<String, Proto>();
		Map<String, Proto> s2cMap = new HashMap<String, Proto>();
		List<Proto> allPdlProtoList = new ArrayList<Proto>();
		int type = pdlConfig.getMsgTypeBegin();
		List<Proto> c2sProtoList = new ArrayList<Proto>();
		for (String fullName : pdlNameList) {
			String simpleName = getPdlSimpleName(fullName);
			String simpleNamePascal = getSimpleNamePascal(simpleName);
			Proto proto = new Proto();
			proto.setFullName(fullName);
			proto.setSimpleName(simpleName);
			proto.setSimpleNamePascal(simpleNamePascal);
			allPdlProtoList.add(proto);
			if (fullName.endsWith(pdlConfig.getOneSuffix())) {
				proto.setType(type);
				type = type + 2;
				c2sMap.put(simpleName, proto);
				c2sProtoList.add(proto);
			}
			if (fullName.endsWith(pdlConfig.getOtherSuffix())) {
				s2cMap.put(simpleName, proto);
			}
		}
		int s2cType = 0;
		List<ProtoPair> pairs = new ArrayList<ProtoPair>();
		for (String simpleName : c2sMap.keySet()) {
			ProtoPair pair = new ProtoPair();
			Proto c2sProto = c2sMap.get(simpleName);
			pair.setRequest(c2sProto);
			Proto s2cProto = s2cMap.get(simpleName);
			if (s2cProto != null) {
				s2cType = c2sProto.getType() + 1;
				s2cProto.setType(s2cType);
				pair.setResponse(s2cProto);
			}
			pairs.add(pair);
		}
		for (Proto e : allPdlProtoList) {
			if (e.getType() == 0
					&& e.getFullName().endsWith(pdlConfig.getOtherSuffix())) {
				s2cType = s2cType + 2;
				e.setType(s2cType);
				ProtoPair pair = new ProtoPair();
				pair.setResponse(e);
				pairs.add(pair);
			}
		}
		Collections.sort(c2sProtoList);
		generateIPdlHandler(c2sProtoList);
		Collections.sort(allPdlProtoList);
		generatePdlMsgType(pdlConfig.getPrefix(), allPdlProtoList);
		generatePdlProcessorImpl(pdlConfig.getPrefix(), c2sProtoList);
		Collections.sort(pairs);
		generatePdlMsgTypeXml(pairs);
	}

	private void generateIPdlHandler(List<Proto> protoList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("protoList", protoList);
		FreeMarkerUtil.analysisTemplate(templateDir, "IPdlHandler.tpl", outDir,
				"IPdlHandler.java", params);
		logger.info("generate [IPdlHandler.java] finish");
	}

	private void generatePdlMsgType(String prefix, List<Proto> protoList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("protoList", protoList);
		params.put("prefix", prefix);
		FreeMarkerUtil.analysisTemplate(templateDir, "PdlMsgType.tpl", outDir,
				"PdlMsgType.java", params);
		logger.info("generate [PdlMsgType.java] finish");
	}

	private void generatePdlProcessorImpl(String prefix, List<Proto> protoList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("protoList", protoList);
		params.put("prefix", prefix);
		FreeMarkerUtil.analysisTemplate(templateDir, "PdlProcessorImpl.tpl",
				outDir, "PdlProcessorImpl.java", params);
		logger.info("generate [PdlProcessorImpl.java] finish");
	}

	private void generatePdlMsgTypeXml(List<ProtoPair> pairs) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("root");
		XMLWriter writer;
		try {
			for (ProtoPair pair : pairs) {
				Element msg = root.addElement("msg");
				String name = null;
				Proto request = pair.getRequest();
				Proto response = pair.getResponse();
				if (request != null) {
					name = request.getSimpleName();
				}
				if (response != null) {
					name = response.getSimpleName();
				}
				msg.addAttribute("name", name);
				if (request != null) {
					Element c2s = msg.addElement("c2s");
					c2s.addAttribute("value", String.valueOf(request.getType()));
				}
				if (response != null) {
					Element s2c = msg.addElement("s2c");
					s2c.addAttribute("value",
							String.valueOf(response.getType()));
				}
			}
			OutputStream os = new FileOutputStream(new File(outDir
					+ File.separatorChar + "PdlMsgType.xml"));
			writer = new XMLWriter(os, OutputFormat.createPrettyPrint());
			writer.write(doc);
		} catch (IOException e) {
			logger.error("error:", e);
		}
		logger.info("generate [PdlMsgType.xml] finish");
	}

	private String getPdlSimpleName(String fullName) {
		if (fullName.endsWith(pdlConfig.getOneSuffix())) {
			return fullName.substring(0,
					fullName.length() - pdlConfig.getOneSuffixLength());
		} else if (fullName.endsWith(pdlConfig.getOtherSuffix())) {
			return fullName.substring(0,
					fullName.length() - pdlConfig.getOtherSuffixLength());
		}
		throw new RuntimeException("unkown pdl proto name");
	}

	private void generateRpc(List<String> rpcNameList) {
		Map<String, Proto> reqMap = new HashMap<String, Proto>();
		Map<String, Proto> ackMap = new HashMap<String, Proto>();
		List<Proto> allRpcProtoList = new ArrayList<Proto>();
		int type = rpcConfig.getMsgTypeBegin();
		for (String fullName : rpcNameList) {
			String simpleName = getRpcSimpleName(fullName);
			String simpleNamePascal = getSimpleNamePascal(simpleName);
			Proto proto = new Proto();
			proto.setFullName(fullName);
			proto.setSimpleName(simpleName);
			proto.setSimpleNamePascal(simpleNamePascal);
			allRpcProtoList.add(proto);
			if (fullName.endsWith(rpcConfig.getOneSuffix())) {
				proto.setType(type);
				type = type + 2;
				reqMap.put(simpleName, proto);
			}
			if (fullName.endsWith(rpcConfig.getOtherSuffix())) {
				ackMap.put(simpleName, proto);
			}
		}
		List<ProtoPair> pairs = new ArrayList<ProtoPair>();
		for (String simpleName : reqMap.keySet()) {
			ProtoPair pair = new ProtoPair();
			Proto reqProto = reqMap.get(simpleName);
			pair.setRequest(reqProto);
			Proto ackProto = ackMap.get(simpleName);
			if (ackProto != null) {
				ackProto.setType(reqProto.getType() + 1);
				pair.setResponse(ackProto);
			}
			pairs.add(pair);
		}
		Collections.sort(allRpcProtoList);
		Collections.sort(pairs);
		generateIRpcHandler(allRpcProtoList, pairs);
		generateRpcMsgType(rpcConfig.getPrefix(), allRpcProtoList);
		generateRpcProcessorImpl(rpcConfig.getPrefix(), allRpcProtoList, pairs);
	}

	public void generateIRpcHandler(List<Proto> allRpcProtoList,
			List<ProtoPair> pairs) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("protoList", allRpcProtoList);
		params.put("pairList", pairs);
		FreeMarkerUtil.analysisTemplate(templateDir, "IRpcHandler.tpl", outDir,
				"IRpcHandler.java", params);
		logger.info("generate [IRpcHandler.java] finish");
	}

	public void generateRpcMsgType(String prefix, List<Proto> allRpcProtoList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("protoList", allRpcProtoList);
		params.put("prefix", prefix);
		FreeMarkerUtil.analysisTemplate(templateDir, "RpcMsgType.tpl", outDir,
				"RpcMsgType.java", params);
		logger.info("generate [RpcMsgType.java] finish");
	}

	public void generateRpcProcessorImpl(String prefix,
			List<Proto> allRpcProtoList, List<ProtoPair> pairs) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("protoList", allRpcProtoList);
		params.put("prefix", prefix);
		params.put("pairList", pairs);
		FreeMarkerUtil.analysisTemplate(templateDir, "RpcProcessorImpl.tpl",
				outDir, "RpcProcessorImpl.java", params);
		logger.info("generate [RpcProcessorImpl.java] finish");
	}

	private String getRpcSimpleName(String fullName) {
		if (fullName.endsWith(rpcConfig.getOneSuffix())) {
			return fullName.substring(0,
					fullName.length() - rpcConfig.getOneSuffixLength());
		} else if (fullName.endsWith(rpcConfig.getOtherSuffix())) {
			return fullName.substring(0,
					fullName.length() - rpcConfig.getOtherSuffixLength());
		}
		throw new RuntimeException("unkown rpc proto name");

	}

	private String getSimpleNamePascal(String simpleName) {
		char firstChar = simpleName.charAt(0);
		char firstWordLowerCase = Character.toLowerCase(firstChar);
		return firstWordLowerCase
				+ simpleName.substring(1, simpleName.length());
	}

}
