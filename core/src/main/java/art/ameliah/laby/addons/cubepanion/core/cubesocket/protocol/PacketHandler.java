package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketDisconnect;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketHelloPong;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLoginComplete;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPerkUpdate;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPong;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketReload;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.labymod.api.util.logging.Logging;

public abstract class PacketHandler extends SimpleChannelInboundHandler<Object> {

  protected Logging log = Logging.create(CubeSocket.class.getSimpleName());

  protected void channelRead0(ChannelHandlerContext ctx, Object packet) {
    this.handlePacket((Packet) packet);
  }

  protected void handlePacket(Packet packet) {
    packet.handle(this);
  }

  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    log.error("An exception occurred while handling a packet {}", cause);
  }


  public abstract void handle(PacketPong packet);

  public abstract void handle(PacketPerkUpdate packet);

  public abstract void handle(PacketHelloPong packet);

  public abstract void handle(PacketLoginComplete packet);

  public abstract void handle(PacketDisconnect packet);

  public abstract void handle(PacketReload packet);

}
