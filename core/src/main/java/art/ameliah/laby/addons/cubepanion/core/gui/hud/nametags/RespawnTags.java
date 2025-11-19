package art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerRespawnEvent;
import art.ameliah.laby.addons.cubepanion.core.gui.imp.SpawnProtectionComponent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.state.EntityExtraKeys;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.NotNull;

public class RespawnTags extends ComponentNameTag {

  private final Cubepanion addon;
  private final HashMap<UUID, SpawnProtectionComponent> components;

  public RespawnTags(Cubepanion addon) {
    this.addon = addon;
    this.components = new HashMap<>();
  }

  @Subscribe
  public void onGameUpdate(GameJoinEvent e) {
    this.components.clear();
  }

  @Subscribe
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    if (!Cubepanion.get().getManager().isPlaying(CubeGame.TEAM_EGGWARS)
    && !Cubepanion.get().getManager().isPlaying(CubeGame.BEDWARS)) {
      return;
    }

    SpawnProtectionComponent spawnProtectionComponent = new SpawnProtectionComponent(this.addon);
    spawnProtectionComponent.enable(false);
    this.components.put(e.getUUID(), spawnProtectionComponent);
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot)  {
    if (!this.addon.configuration().getQolConfig().getRespawnTimer().get()) {
      return List.of();
    }

    if (!snapshot.has(EntityExtraKeys.CUSTOM_AVATAR_DATA)) {
      return List.of();
    }

    var data = snapshot.get(EntityExtraKeys.CUSTOM_AVATAR_DATA);
    var playerInfo = data.playerInfo();
    if (playerInfo == null) {
      return List.of();
    }

    UUID uuid = playerInfo.profile().getUniqueId();
    SpawnProtectionComponent gen = components.get(uuid);
    if (gen == null) {
      return List.of();
    }

    Component component = gen.getComponent(System.currentTimeMillis());
    if (component == Component.empty()
        || ((TextComponent) component).getText().equals(Component.empty().getText())) {
      components.remove(uuid);
      return List.of();
    }

    return List.of(component);
  }

}
