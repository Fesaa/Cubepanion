package art.ameliah.laby.addons.cubepanion.core.cubesocket.pipeline;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.labymod.api.util.logging.Logging;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

  private static final Logging LOGGER = Logging.getLogger();

  private final CubeSocket cubeSocket;

  public PacketEncoder(CubeSocket cubeSocket) {
    this.cubeSocket = cubeSocket;
  }

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf)
      throws Exception {
    int id = this.cubeSocket.getProtocol().getPacketId(packet);
    if (id != 0 && id != 1) {
      LOGGER.debug("[CUBESOCKET] [OUT] " + id + " " + packet.getClass().getSimpleName());
    }

    PacketBuffer buffer = new PacketBuffer(byteBuf);
    buffer.writeVarIntToBuffer(id);
    packet.write(buffer);
  }
}
