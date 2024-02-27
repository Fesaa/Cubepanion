package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;


import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.proto.S2CHelloPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.CodecLink;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.logging.Logging;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CubeSocketSession extends PacketHandler {
  private static final Logging LOGGER = Logging.create(CubeSocketSession.class);
  private static final Gson gson = new Gson();
  private final CubeSocket socket;
  private final SessionAccessor sessionAccessor;
  private final CodecLink codecLink;

  public CubeSocketSession(CubeSocket socket, WebSocketClientHandshaker handshaker, SessionAccessor sessionAccessor, CodecLink codecLink) {
      super(handshaker);
      this.socket = socket;
      this.sessionAccessor = sessionAccessor;
      this.codecLink = codecLink;
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    socket.updateState(CubeSocketState.OFFLINE);
  }

  @Override
  protected void handle(S2CPerkUpdatePacket packet) {
    if (sessionAccessor.getSession() == null) {
      return;
    }
    if (this.codecLink == null) {
      return;
    }

    // Ignore updates from ourselves, the LoadPerkEvent has already fired
    UUID uuid = sessionAccessor.getSession().getUniqueId();
    if (packet.getUuid().equals(uuid.toString())) {
      return;
    }

    List<ItemStack> perks = new ArrayList<>(packet.getPerksList().size());
    for (String perk : packet.getPerksList()) {
      JsonObject json = gson.fromJson(perk, JsonObject.class);
      Optional<ItemStack> stack = codecLink.decode(json);
      stack.ifPresent(perks::add);
    }

    LOGGER.info("Received perk update for " + packet.getUuid() + " with " + perks.size() + " perks");
    PerkCategory category = PerkCategory.fromProtoCategory(packet.getCategory());
    this.socket.fireEventSync(new PerkLoadEvent(category, perks, true));
  }

  @Override
  protected void handle(S2CHelloPacket packet) {
    socket.updateState(CubeSocketState.CONNECTED);
  }
}
