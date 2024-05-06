package art.ameliah.laby.addons.cubepanion.core.cubesocket;


import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketDisconnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketStateUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Protocol;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketDisconnect;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketHelloPing;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketGameTracker;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketPerkTracker;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketSession;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketState;
import art.ameliah.laby.addons.cubepanion.core.events.CubeJoinEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.labymod.api.Laby;
import net.labymod.api.client.session.Session;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.concurrent.ThreadFactoryBuilder;
import net.labymod.api.event.Event;
import net.labymod.api.event.EventBus;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.notification.NotificationController;
import net.labymod.api.service.Service;
import net.labymod.api.util.I18n;
import net.labymod.api.util.logging.Logging;
import net.labymod.api.util.time.TimeUtil;
import org.jetbrains.annotations.Nullable;


public class CubeSocket extends Service {

  private static final Logging LOGGER = Logging.create(CubeSocket.class);
  private static final String host;
  private static final int port = 9527;

  static {
    if (System.getenv("CUBEPANION_DEV") != null) {
      host = "192.168.0.118";
    } else {
      host = "ameliah.art";
    }
  }

  private final Protocol protocol = new Protocol();
  private final Cubepanion addon;
  private final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0,
      (new ThreadFactoryBuilder()).withNameFormat("CubeSocketNio#d").build());
  private final ExecutorService executor = Executors.newFixedThreadPool(2,
      (new ThreadFactoryBuilder()).withNameFormat("CubeSocketExecutor#d").build());
  private final SessionAccessor sessionAccessor;
  private final EventBus eventBus;
  private final NotificationController notificationController;

  private CubeSocketSession session = null;
  private CubeSocketHandler channelHandler = null;
  private Bootstrap bootstrap;
  private volatile CubeSocketState state;
  private long timeLastKeepAlive;
  private long timeNextConnect;
  private int connectTries;
  private String lastDisconnectReason;

  public CubeSocket(Cubepanion addon, SessionAccessor sessionAccessor, EventBus eventBus,
      NotificationController notifications) {
    this.addon = addon;
    this.state = CubeSocketState.OFFLINE;
    this.timeNextConnect = TimeUtil.getMillis();
    this.connectTries = 0;
    this.sessionAccessor = sessionAccessor;
    this.eventBus = eventBus;
    this.notificationController = notifications;

    this.eventBus.registerListener(new CubeSocketNotifications(this, this.notificationController));
    this.eventBus.registerListener(new CubeSocketPerkTracker(this, addon));
    this.eventBus.registerListener(new CubeSocketGameTracker(this));

    Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
      try {
        if (this.addon.getManager() == null) {
          return;
        }
        if (!this.addon.getManager().onCubeCraft()) {
          return;
        }

        long durationKeepAlive = TimeUtil.getMillis() - this.timeLastKeepAlive;
        long durationConnect = this.timeNextConnect - TimeUtil.getMillis();

        if (state != CubeSocketState.OFFLINE && durationKeepAlive > 25000L) {
          disconnect(I18n.translate("cubepanion.notifications.cubesocket.disconnect.timeout"));
        }

        if (state == CubeSocketState.OFFLINE && durationConnect < 0L) {
          connect();
        }
      } catch (Exception e) {
        LOGGER.error("Error in CubeSocket keep alive", e);
      }
    }, 0L, 5L, TimeUnit.SECONDS);
  }

  private void connect() {
    if (this.connectTries > 4) {
      return;
    }

    executor.execute(() -> {
      synchronized (this) {
        if (state != CubeSocketState.OFFLINE) {
          return;
        }

        this.keepAlive();
        this.updateState(CubeSocketState.HELLO);
        this.connectTries++;
        Session session = this.sessionAccessor.getSession();
        if (session == null) {
          return;
        }

        this.session = new CubeSocketSession(this, this.sessionAccessor, this.addon.getCodecLink());
        this.channelHandler = new CubeSocketHandler(this, this.session);
        this.lastDisconnectReason = null;

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.nioEventLoopGroup);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.handler(this.channelHandler);

        try {
          this.bootstrap.connect(host, port).syncUninterruptibly();
          this.sendPacket(new PacketHelloPing(TimeUtil.getMillis()));
        } catch (Exception e) {
          this.updateState(CubeSocketState.OFFLINE);
          LOGGER.warn("Failed to connect to CubeSocket", e);
        }
      }
    });
  }

  @Subscribe
  public void onCubeJoin(CubeJoinEvent e) {
    this.connect();
  }


  @Subscribe
  public void onNetworkDisconnect(ServerDisconnectEvent e) {
    if (this.isConnected()) {
      this.disconnect(I18n.translate("cubepanion.notifications.cubesocket.disconnect.server"));
    }
    this.connectTries = 0;
  }

  private void disconnect(String reason) {
    long delay = (long) (1000.0 * Math.random() * 60.0);
    this.timeNextConnect = TimeUtil.getMillis() + 10000L + delay;
    this.lastDisconnectReason = reason;
    if (this.state == CubeSocketState.OFFLINE) {
      return;
    }

    this.fireEventSync(new CubeSocketDisconnectEvent(I18n.translate(reason)));
    this.updateState(CubeSocketState.OFFLINE);
    this.sendPacket(new PacketDisconnect("logout"), (ch) -> {
      if (ch.isOpen()) {
        ch.close();
      }
    });
    this.session = null;
  }


  public void updateState(CubeSocketState state) {
    synchronized (this) {
      this.state = state;
    }
    this.fireEventSync(new CubeSocketStateUpdateEvent(state));
  }

  public void keepAlive() {
    this.timeLastKeepAlive = TimeUtil.getMillis();
  }

  public void fireEventSync(Event event) {
    Laby.labyAPI().minecraft().executeOnRenderThread(() -> this.eventBus.fire(event));
  }

  public void sendPacket(Packet packet) {
    this.sendPacket(packet, null);
  }

  public void sendPacket(Packet packet, Consumer<SocketChannel> callback) {
    NioSocketChannel channel = this.getChannel();
    if (channel == null || !channel.isActive()) {
      return;
    }
    if (channel.eventLoop().inEventLoop()) {
      channel
          .writeAndFlush(packet)
          .addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      if (callback != null) {
        callback.accept(channel);
      }
    } else {
      channel
          .eventLoop()
          .execute(() -> {
                channel
                    .writeAndFlush(packet)
                    .addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                if (callback != null) {
                  callback.accept(channel);
                }
              }
          );
    }


  }

  public NioSocketChannel getChannel() {
    return this.channelHandler == null ? null : this.channelHandler.getChannel();
  }

  public boolean isConnected() {
    return this.state == CubeSocketState.CONNECTED;
  }

  public CubeSocketState getState() {
    return this.state;
  }

  public @Nullable CubeSocketSession getSession() {
    return this.session;
  }

  public String getLastDisconnectReason() {
    return lastDisconnectReason;
  }

  public int getPort() {
    return port;
  }

  public String getHost() {
    return host;
  }

  public Protocol getProtocol() {
    return protocol;
  }

  public void setConnectTries(int connectTries) {
    this.connectTries = connectTries;
  }

}






















