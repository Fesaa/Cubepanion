package org.cubepanion.core.gui.hud.nametags;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
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
    if (!this.addon.configuration().getQolConfig().getRankTag().get()) {
      return null;
    }

    ClientPlayer clientPlayer = this.addon.labyAPI().minecraft().getClientPlayer();
    if (clientPlayer == null) {
      return null;
    }
    if (!player.getName().equals(clientPlayer.getName())) {
      return null;
    }

    if (addon.getManager().getDivision().equals(CubeGame.LOBBY) || addon.getManager()
        .isInPreLobby()) {
      return RenderableComponent.of(Component.text(addon.getManager().getRankString()));
    }
    return null;
  }

  @Override
  public NameTagBackground getCustomBackground() {
    return NameTagBackground.custom(false, 0);
  }

}
