package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;


import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketConnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketDisconnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketHelloPong;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLogin;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketLoginComplete;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPerkUpdate;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPing;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPong;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.CodecLink;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.concurrent.ThreadFactoryBuilder;
import net.labymod.api.util.I18n;

public class CubeSocketSession extends PacketHandler {

  private static final Gson gson = new Gson();
  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,
      (new ThreadFactoryBuilder()).withNameFormat("CubeSocketSessionExecutor#d").build());
  private final CubeSocket socket;
  private final SessionAccessor sessionAccessor;
  private final CodecLink codecLink;

  private int keepAlivesSent = 0;
  private int keepAlivesReceived = 0;

  public CubeSocketSession(CubeSocket socket, SessionAccessor sessionAccessor,
      CodecLink codecLink) {
    this.socket = socket;
    this.sessionAccessor = sessionAccessor;
    this.codecLink = codecLink;
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
  public void handle(PacketPerkUpdate packet) {
    if (sessionAccessor.getSession() == null) {
      return;
    }
    if (this.codecLink == null) {
      return;
    }

    // Ignore updates from ourselves, the LoadPerkEvent has already fired
    UUID uuid = sessionAccessor.getSession().getUniqueId();
    if (packet.getSender().equals(uuid)) {
      return;
    }

    List<ItemStack> perks = new ArrayList<>(packet.getPerks().length);
    for (String perk : packet.getPerks()) {
      JsonObject json = gson.fromJson(perk, JsonObject.class);
      Optional<ItemStack> stack = codecLink.decode(json);
      stack.ifPresent(perks::add);
    }

    this.socket.fireEventSync(new PerkLoadEvent(packet.getPerkCategory(), perks, true));
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
  }
}
