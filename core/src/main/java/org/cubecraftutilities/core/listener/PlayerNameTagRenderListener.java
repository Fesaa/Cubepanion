package org.cubecraftutilities.core.listener;

import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent.Context;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.Colours;
import org.cubecraftutilities.core.gui.imp.SpawnProtectionComponent;

public class PlayerNameTagRenderListener {

  private final CCU addon;

  public PlayerNameTagRenderListener(CCU addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onPlayerNameTagRenderEvent(PlayerNameTagRenderEvent playerNameTagRenderEvent) {
    if (playerNameTagRenderEvent.context().equals(Context.TAB_LIST)) {
      return;
    }

    Component nameTag = playerNameTagRenderEvent.nameTag();
    NetworkPlayerInfo playerInfo = playerNameTagRenderEvent.playerInfo();
    if (playerInfo == null) {
      return;
    }
    UUID uuid = playerInfo.profile().getUniqueId();

    // Adding spawn protection counter
    if (this.addon.configuration().getRespawnTimer().get()) {
        SpawnProtectionComponent spawnProtectionComponentGen = this.addon.getManager().getSpawnProtectionManager().getSpawnProtectionComponent(uuid);
        if (spawnProtectionComponentGen != null) {
          Component spawnProtectionComponent = spawnProtectionComponentGen.getComponent();
          if (spawnProtectionComponent != Component.empty()) {
            nameTag = nameTag.append(Component.text("    "))
                .append(spawnProtectionComponent);
        }
      }
    }

    // Adding Ping
    if (this.addon.configuration().getPingInNameTag().get()) {
      int ping = playerInfo.getCurrentPing();
      if (ping > 0) {
        nameTag = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text(ping, Colours.pingColour(ping)))
            .append(Component.text("] ", NamedTextColor.GRAY))
            .append(nameTag);
      }
    }
    playerNameTagRenderEvent.setNameTag(nameTag);
  }



}
