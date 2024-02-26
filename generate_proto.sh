curl https://raw.githubusercontent.com/Fesaa/CubepanionAPI/master/packets.proto >> packets.proto
protoc -I=./ --java_out=./core/src/main/java packets.proto
rm packets.proto