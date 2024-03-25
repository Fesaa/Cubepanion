package art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketPrepender extends MessageToByteEncoder<ByteBuf> {

  public PacketPrepender() {
  }

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer,
      ByteBuf byteBuf) throws Exception {
    int length = buffer.readableBytes();
    int varInt = PacketBuffer.getVarIntSize(length);
    if (varInt > 3) {
      throw new IllegalArgumentException("unable to fit " + length + " into 3");
    }

    byteBuf.ensureWritable(varInt + length);
    PacketBuffer.writeVarIntToBuffer(byteBuf, length);
    byteBuf.writeBytes(buffer, buffer.readerIndex(), length);
  }
}
