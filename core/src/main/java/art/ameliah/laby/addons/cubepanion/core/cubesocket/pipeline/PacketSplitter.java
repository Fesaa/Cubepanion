package art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class PacketSplitter extends ByteToMessageDecoder {

  public PacketSplitter() {
  }

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer,
      List<Object> list) throws Exception {
    buffer.markReaderIndex();
    if (!buffer.isReadable()) {
      buffer.resetReaderIndex();
      return;
    }

    int packetLength = PacketBuffer.readVarIntFromBuffer(buffer);
    if (buffer.readableBytes() < packetLength) {
      buffer.resetReaderIndex();
      return;
    }

    list.add(buffer.readBytes(packetLength));
  }
}
