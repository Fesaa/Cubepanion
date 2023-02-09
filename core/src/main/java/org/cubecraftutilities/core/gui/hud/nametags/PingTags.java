package org.cubecraftutilities.core.gui.hud.nametags;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.font.RenderableComponent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.Colours;
import org.jetbrains.annotations.Nullable;

public class PingTags extends NameTag {

  private final CCU addon;


  public PingTags(CCU addon) {
    this.addon = addon;
  }

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if (!(this.entity instanceof Player)) {
      return null;
    }

    if (!this.addon.configuration().getPingInNameTag().get()) {
      return null;
    }

    NetworkPlayerInfo playerInfo = ((Player) this.entity).networkPlayerInfo();
    if (playerInfo == null) {
      return null;
    }

    int ping = playerInfo.getCurrentPing();
    if (ping == 0) {
      return null;
    }

    Component pingDisplay = Component.text("[", NamedTextColor.GRAY)
        .append(Component.text(ping, Colours.pingColour(ping)))
        .append(Component.text("] ", NamedTextColor.GRAY));

    return RenderableComponent.of(pingDisplay);
  }

  @Override
  public float getScale() {
    return 0.75F;
  }

}
