package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketHelloPong;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLoginComplete;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPerkUpdate;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPong;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.labymod.api.util.logging.Logging;

public abstract class PacketHandler extends SimpleChannelInboundHandler<Object> {

  protected static final Logging LOGGER = Logging.create(PacketHandler.class);

  protected void channelRead0(ChannelHandlerContext ctx, Object packet) {
    this.handlePacket((Packet)packet);
  }

  protected void handlePacket(Packet packet) {
    packet.handle(this);
  }

  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
  }


  public abstract void handle(PacketPong packet);

  public abstract void handle(PacketPerkUpdate packet);

  public abstract void handle(PacketHelloPong packet);

  public abstract void handle(PacketLoginComplete packet);


}
