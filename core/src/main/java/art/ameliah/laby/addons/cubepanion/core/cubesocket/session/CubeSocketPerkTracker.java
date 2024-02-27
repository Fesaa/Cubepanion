package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketUtils;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory;
import com.google.gson.JsonElement;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.logging.Logging;
import java.util.ArrayList;
import java.util.Optional;

public class CubeSocketPerkTracker {

  private static final Logging LOGGER = Logging.create(CubeSocketPerkTracker.class);
  private final CubeSocket socket;
  private final Cubepanion addon;

  public CubeSocketPerkTracker(CubeSocket socket,Cubepanion addon) {
      this.socket = socket;
      this.addon = addon;
  }

  @Subscribe
  public void onPerkLoad(PerkLoadEvent e) {
    if (socket.getState() != CubeSocketState.CONNECTED) {
      return;
    }
    if (e.fromWS()) {
      return;
    }
    if (this.addon.getCodecLink() == null) {
      return;
    }

    PerkCategory category = e.getCategory().getProtoCategory();
    ArrayList<String> perks = new ArrayList<>();

    for (ItemStack perk : e.getPerks()) {
      Optional<JsonElement> json = addon.getCodecLink().encode(perk);
      if (json.isEmpty()) {
        LOGGER.error("Failed to encode perk: " + perk);
        continue;
      }

      perks.add(json.get().toString());
    }

    socket.sendPacket(PacketUtils.PerkUpdatePacket(category, perks));
  }
}
