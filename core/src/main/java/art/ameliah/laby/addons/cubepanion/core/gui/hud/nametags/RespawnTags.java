package art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerRespawnEvent;
import art.ameliah.laby.addons.cubepanion.core.gui.imp.SpawnProtectionComponent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.HashMap;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.Nullable;

public class RespawnTags extends NameTag {

  private final Cubepanion addon;
  private final HashMap<UUID, SpawnProtectionComponent> components;

  public RespawnTags(Cubepanion addon) {
    this.addon = addon;
    this.components = new HashMap<>();
  }

  @Subscribe
  public void onGameUpdate(GameUpdateEvent e) {
    if (e.isSwitch()) {
      this.components.clear();
    }
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
  protected @Nullable RenderableComponent getRenderableComponent() {
    if (!(this.entity instanceof Player)) {
      return null;
    }

    if (!this.addon.configuration().getQolConfig().getRespawnTimer().get()) {
      return null;
    }

    NetworkPlayerInfo playerInfo = ((Player) this.entity).getNetworkPlayerInfo();
    if (playerInfo == null) {
      return null;
    }
    UUID uuid = playerInfo.profile().getUniqueId();
    SpawnProtectionComponent gen = components.get(uuid);
    if (gen == null) {
      return null;
    }

    Component component = gen.getComponent(System.currentTimeMillis());
    if (component == Component.empty()
        || ((TextComponent) component).getText().equals(Component.empty().getText())) {
      components.remove(uuid);
      return null;
    }

    return RenderableComponent.of(component);
  }

}
