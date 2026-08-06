package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;


import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketConnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketDisconnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketReloadRequest;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketDisconnect;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketHelloPong;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLocationUpdate;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLogin;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLoginComplete;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPing;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPong;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketReload;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketSetProtocol;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import io.netty.channel.ChannelHandlerContext;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.concurrent.ThreadFactoryBuilder;
import net.labymod.api.util.I18n;

public class CubeSocketSession extends PacketHandler {

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
      (new ThreadFactoryBuilder()).withNameFormat("CubeSocketSessionExecutor#d").build());
  private final CubeSocket socket;
  private final SessionAccessor sessionAccessor;

  private int keepAlivesSent = 0;
  private int keepAlivesReceived = 0;
  private long lastReload = -1;

  public CubeSocketSession(CubeSocket socket, SessionAccessor sessionAccessor) {
    this.socket = socket;
    this.sessionAccessor = sessionAccessor;
  }

  public int getKeepAlivesReceived() {
    return keepAlivesReceived;
  }

  public int getKeepAlivesSent() {
    return keepAlivesSent;
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    if (socket.getState() != CubeSocketState.OFFLINE) {
      socket.updateState(CubeSocketState.OFFLINE);
      socket.fireEventSync(new CubeSocketDisconnectEvent(
          I18n.translate("cubepanion.notifications.cubesocket.disconnect.apiServer")));
    }
  }

  @Override
  public void handle(PacketHelloPong packet) {
    this.socket.updateState(CubeSocketState.LOGIN);
    UUID uuid;
    if (this.sessionAccessor.getSession() != null) {
      uuid = this.sessionAccessor.getSession().getUniqueId();
    } else {
      uuid = UUID.randomUUID();
    }

    this.socket.sendPacket(new PacketLogin(uuid));
  }

  @Override
  public void handle(PacketPong packet) {
    this.keepAlivesReceived++;
    this.socket.keepAlive();

    this.executorService.schedule(() -> {
      this.socket.sendPacket(new PacketPing());
      this.keepAlivesSent++;
    }, 5L, TimeUnit.SECONDS);
  }

  @Override
  public void handle(PacketLoginComplete packet) {
    this.socket.updateState(CubeSocketState.CONNECTED);

    socket.fireEventSync(new CubeSocketConnectEvent());
    this.socket.sendPacket(new PacketPing());

    this.executorService.schedule(() -> {
      int protocolVersion = Laby.labyAPI().minecraft().getProtocolVersion();
      this.socket.sendPacket(new PacketSetProtocol(protocolVersion));
    }, 1L, TimeUnit.SECONDS);

    this.executorService.schedule(() -> {
      var fakeEvent = new GameJoinEvent(CubeGame.LOBBY, CubeGame.LOBBY, false);
      this.socket.sendPacket(new PacketLocationUpdate(fakeEvent));
    }, 2L, TimeUnit.SECONDS);
  }

  @Override
  public void handle(PacketDisconnect packet) {
    this.socket.updateState(CubeSocketState.OFFLINE);
    this.socket.fireEventSync(new CubeSocketDisconnectEvent(packet.getReason()));
  }

  @Override
  public void handle(PacketReload packet) {
    long now = System.currentTimeMillis();
    if (now - this.lastReload < 5000L) {
      this.lastReload = now;
      log.warn("CubeSocket tried reloading data less than 5s apart, ignoring");
      return;
    }


    this.socket.fireEventSync(new CubeSocketReloadRequest());
    CubepanionAPI.I().loadInitialData();
    this.lastReload = now;
  }
}
