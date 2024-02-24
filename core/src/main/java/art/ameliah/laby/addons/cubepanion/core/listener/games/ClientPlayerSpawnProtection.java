package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerRespawnEvent;
import art.ameliah.laby.addons.cubepanion.core.gui.imp.SpawnProtectionComponent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

public class ClientPlayerSpawnProtection {

  private final ChatExecutor chatExecutor;

  private final SpawnProtectionComponent actionBarComponent;

  private int tick = 0;

  public ClientPlayerSpawnProtection(Cubepanion addon) {
    this.chatExecutor = addon.labyAPI().minecraft().chatExecutor();
    this.actionBarComponent = new SpawnProtectionComponent(addon);
  }

  @Subscribe
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    if (!Cubepanion.get().getManager().isPlaying(CubeGame.TEAM_EGGWARS)) {
      return;
    }

    if (!e.isClientPlayer()) {
      return;
    }

    this.actionBarComponent.enable(true);
  }

  @Subscribe
  public void onGameTick(GameTickEvent e) {
    if (e.phase() == Phase.POST) {
      return;
    }

    tick++;

    if (tick % 2 != 0) {
      return;
    }

    if (this.actionBarComponent.isEnabled()) {
      this.chatExecutor.displayClientMessage(
          this.actionBarComponent.getComponent(System.currentTimeMillis()), true);
    }
  }

}
