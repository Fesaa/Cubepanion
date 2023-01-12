package org.ccu.core.listener;

import com.google.inject.Inject;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import org.ccu.core.CCU;
import org.ccu.core.gui.imp.SpawnProtectionComponent;

public class PlayerNameTagRenderListener {

  private final CCU addon;

  @Inject
  public PlayerNameTagRenderListener(CCU addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onPlayerNameTagRenderEvent(PlayerNameTagRenderEvent playerNameTagRenderEvent) {
    if (!this.addon.configuration().getRespawnTimer().get()) {
      return;
    }

    NetworkPlayerInfo playerInfo = playerNameTagRenderEvent.playerInfo();
    if (playerInfo == null) {
      return;
    }

    UUID uuid = playerInfo.profile().getUniqueId();
    SpawnProtectionComponent spawnProtectionComponentGen = this.addon.getManager().getSpawnProtectionComponent(uuid);
    if (spawnProtectionComponentGen == null) {
      return;
    }

    Component spawnProtectionComponent = spawnProtectionComponentGen.getComponent();
    if (spawnProtectionComponent == Component.empty()) {
      return;
    }

    playerNameTagRenderEvent.setNameTag(playerNameTagRenderEvent.nameTag()
            .append(Component.text("    "))
            .append(spawnProtectionComponent));
  }



}
