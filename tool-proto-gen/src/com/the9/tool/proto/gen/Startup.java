package com.the9.tool.proto.gen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;

public class Startup {
	private static final Logger logger = LoggerFactory.getLogger(Startup.class);

	public static void main(String[] args) throws Exception {
		String outDir = ".";
		String tempateDir = "./template";
		if (args.length >= 0) {
			outDir = args[0];
			logger.info("outDir='{}'", new File(outDir).getAbsolutePath());
			tempateDir = args[1];
			logger.info("tempateDir='{}'",
					new File(tempateDir).getAbsolutePath());
		}
		CodeGeneratorRequest request = CodeGeneratorRequest
				.parseFrom(System.in);
		List<String> pdlNameList = new ArrayList<String>();
		List<String> rpcNameList = new ArrayList<String>();
		for (FileDescriptorProto file : request.getProtoFileList()) {
			if (file.getName().equals("common.proto")) {
				logger.info("ignore common.proto");
			} else if (file.getName().equals("pdl.proto")) {
				List<DescriptorProto> protoTypeList = file.getMessageTypeList();
				for (DescriptorProto proto : protoTypeList) {
					pdlNameList.add(proto.getName());
				}
			} else if (file.getName().equals("rpc.proto")) {
				List<DescriptorProto> protoTypeList = file.getMessageTypeList();
				for (DescriptorProto proto : protoTypeList) {
					rpcNameList.add(proto.getName());
				}
			} else {
				logger.warn("unkown proto file={}", file.getName());
			}
		}
		ProtoConfig pdlConfig = new ProtoConfig(10001, "PDL_", "_C2S_Msg",
				"_S2C_Msg");
		ProtoConfig rpcConfig = new ProtoConfig(20001, "RPC_", "Req", "Ack");
		new Generator(pdlConfig, rpcConfig, outDir, tempateDir).generate(
				pdlNameList, rpcNameList);
	}
}
