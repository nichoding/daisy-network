set "TOOLPATH=%cd%/protoc.exe"
%TOOLPATH% --java_out=src --proto_path=src/com/the9/daisy/network/proto src/com/the9/daisy/network/proto/common_pdl.proto
%TOOLPATH% --java_out=src --proto_path=src/com/the9/daisy/network/proto src/com/the9/daisy/network/proto/common_rpc.proto
%TOOLPATH% --java_out=src --proto_path=src/com/the9/daisy/network/proto src/com/the9/daisy/network/proto/pdl.proto
%TOOLPATH% --java_out=src --proto_path=src/com/the9/daisy/network/proto src/com/the9/daisy/network/proto/rpc.proto
cd ./gen
set OUTPUT=../src/com/the9/daisy/network/proto
set PROTOPATH=../src/com/the9/daisy/network/proto
set SERVICEPATH=../src/com/the9/daisy/network/proto/service.proto

%TOOLPATH% --plugin=protoc-gen-daisy=protoc-gen-daisy.bat --daisy_out=%OUTPUT% --proto_path=%PROTOPATH% %SERVICEPATH%
@pause