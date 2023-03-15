package org.cubecraftutilities.core.gui.hud.nametags;

import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.font.RenderableComponent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.gui.imp.SpawnProtectionComponent;
import org.jetbrains.annotations.Nullable;

public class RespawnTags extends NameTag {

  private final CCU addon;

  public RespawnTags(CCU addon) {
    this.addon = addon;
  }

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if (!(this.entity instanceof Player)) {
      return null;
    }

    if (!this.addon.configuration().getQolConfig().getRespawnTimer().get()) {
      return null;
    }

    NetworkPlayerInfo playerInfo = ((Player) this.entity).networkPlayerInfo();
    if (playerInfo == null) {
      return null;
    }
    UUID uuid = playerInfo.profile().getUniqueId();
    SpawnProtectionComponent spawnProtectionComponentGen = this.addon.getManager().getSpawnProtectionManager().getSpawnProtectionComponent(uuid);
    if (spawnProtectionComponentGen == null) {
      return null;
    }

    Component spawnProtectionComponent = spawnProtectionComponentGen.getComponent();
    if (spawnProtectionComponent == Component.empty() || ((TextComponent) spawnProtectionComponent).getText().equals(Component.empty().getText())) {
      return null;
    }

    return RenderableComponent.of(spawnProtectionComponent);
  }

}
