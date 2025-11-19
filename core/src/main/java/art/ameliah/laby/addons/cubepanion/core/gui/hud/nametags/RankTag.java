package art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.render.state.EntityExtraKeys;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class RankTag extends ComponentNameTag {

  private final Cubepanion addon;

  public RankTag(Cubepanion addon) {
    this.addon = addon;
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
    var component = this.getRenderableComponent(snapshot);
    return component == null ? List.of() : List.of(component);
  }

  protected @Nullable Component getRenderableComponent(EntitySnapshot snapshot) {
    if (!this.addon.configuration().getQolConfig().getRankTag().get()) {
      return null;
    }

    if (!snapshot.has(EntityExtraKeys.CUSTOM_AVATAR_DATA)) {
      return null;
    }

    var data = snapshot.get(EntityExtraKeys.CUSTOM_AVATAR_DATA);
    var playerInfo = data.playerInfo();
    if (playerInfo == null) {
      return null;
    }

    ClientPlayer clientPlayer = this.addon.labyAPI().minecraft().getClientPlayer();
    if (clientPlayer == null) {
      return null;
    }
    if (!playerInfo.profile().getUsername().equals(clientPlayer.getName())) {
      return null;
    }

    if (addon.getManager().getDivision().equals(CubeGame.LOBBY) || addon.getManager()
        .isInPreGameState()) {
      return Component.text(addon.getManager().getRankString());
    }
    return null;
  }

  @Override
  protected int getBackgroundColor(EntitySnapshot snapshot) {
    return 0;
  }

}
