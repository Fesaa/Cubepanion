package org.ccu.core.listener;

import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent.Context;
import org.ccu.core.CCU;
import org.ccu.core.gui.imp.SpawnProtectionComponent;

public class PlayerNameTagRenderListener {

  private final CCU addon;

  public PlayerNameTagRenderListener(CCU addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onPlayerNameTagRenderEvent(PlayerNameTagRenderEvent playerNameTagRenderEvent) {
    if (!this.addon.configuration().getRespawnTimer().get()) {
      return;
    }

    if (playerNameTagRenderEvent.context().equals(Context.TAB_LIST)) {
      return;
    }

    NetworkPlayerInfo playerInfo = playerNameTagRenderEvent.playerInfo();
    if (playerInfo == null) {
      return;
    }

    UUID uuid = playerInfo.profile().getUniqueId();
    SpawnProtectionComponent spawnProtectionComponentGen = this.addon.getManager().getSpawnProtectionManager().getSpawnProtectionComponent(uuid);
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
