package art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.labymod.api.util.logging.Logging;

public class PacketDecoder extends ByteToMessageDecoder {

  private static final Logging LOGGER = Logging.getLogger();

  private final CubeSocket cubeSocket;

  public PacketDecoder(CubeSocket cubeSocket) {
    this.cubeSocket = cubeSocket;
  }


  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
      List<Object> objects) throws Exception {
    if (byteBuf.readableBytes() < 1) {
      return;
    }

    PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
    int id = packetBuffer.readVarIntFromBuffer();
    Packet packet = this.cubeSocket.getProtocol().getPacket(id);
    if (id != 0 && id != 1) {
      LOGGER.debug("[CUBESOCKET] [IN] " + id + " " + packet.getClass().getSimpleName());
    }

    packet.read(packetBuffer);
    if (byteBuf.readableBytes() > 0) {
      String packetName = packet.getClass().getSimpleName();
      throw new IOException("Packet " + packetName + " (" + id + ") was larger than I expected, found " + byteBuf.readableBytes() + " bytes extra whilst reading packet " + packet );
    }

    objects.add(packet);
  }
}
