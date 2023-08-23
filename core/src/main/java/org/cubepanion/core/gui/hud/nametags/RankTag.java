package org.cubepanion.core.gui.hud.nametags;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.CubeGame;
import org.jetbrains.annotations.Nullable;

public class RankTag extends NameTag {

  private final Cubepanion addon;

  public RankTag(Cubepanion addon) {
    this.addon = addon;
  }

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if (!(this.entity instanceof Player player)) {
      return null;
    }

    ClientPlayer clientPlayer = this.addon.labyAPI().minecraft().getClientPlayer();
    if (clientPlayer == null) {
      return null;
    }
    if (!player.getName().equals(clientPlayer.getName())) {
      return null;
    }

    if (!addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
      return null;
    }
    return RenderableComponent.of(Component.text(addon.getManager().getRankString()));
  }

}