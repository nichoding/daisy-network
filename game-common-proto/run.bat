set "TOOLPATH=%cd%/protoc.exe"
cd ../../game-common-proto/
%TOOLPATH% --java_out=src --proto_path=src/com/the9/common/proto src/com/the9/common/proto/common.proto
%TOOLPATH% --java_out=src --proto_path=src/com/the9/common/proto src/com/the9/common/proto/pdl.proto
%TOOLPATH% --java_out=src --proto_path=src/com/the9/common/proto src/com/the9/common/proto/rpc.proto

cd ../tool-proto-gen/dist

set OUTPUT=../../game-common-proto/src/com/the9/common/proto
set PROTOPATH=../../game-common-proto/src/com/the9/common/proto

%TOOLPATH% --plugin=protoc-gen-daisy=protoc-gen-daisy.bat --daisy_out=%OUTPUT% --proto_path=%PROTOPATH% ../../game-common-proto/src/com/the9/common/proto/service.proto
@pause