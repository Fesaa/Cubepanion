package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketPerkUpdate;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import net.labymod.api.client.session.SessionAccessor;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.logging.Logging;

public class CubeSocketPerkTracker {

  private static final Logging LOGGER = Logging.create(CubeSocketPerkTracker.class);
  private final CubeSocket socket;
  private final Cubepanion addon;
  private final SessionAccessor sessionAccessor;

  public CubeSocketPerkTracker(CubeSocket socket, Cubepanion addon) {
    this.socket = socket;
    this.addon = addon;
    this.sessionAccessor = addon.labyAPI().minecraft().sessionAccessor();
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
    if (sessionAccessor.getSession() == null) {
      return;
    }
    UUID uuid = sessionAccessor.getSession().getUniqueId();

    PerkCategory category = e.getCategory();
    ArrayList<String> perks = new ArrayList<>();

    for (ItemStack perk : e.getPerks()) {
      Optional<JsonElement> json = addon.getCodecLink().encode(perk);
      if (json.isEmpty()) {
        LOGGER.error("Failed to encode perk: " + perk);
        continue;
      }

      perks.add(json.get().toString());
    }

    socket.sendPacket(new PacketPerkUpdate(category, uuid, perks));
  }
}
