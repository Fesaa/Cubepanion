package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent.UseType;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.FireballManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.NotNull;

public class FireballCooldown {

  private final Cubepanion addon;
  private final @NotNull FunctionLink functionLink;

  public FireballCooldown(Cubepanion addon, @NotNull FunctionLink functionLink) {
    this.addon = addon;
    this.functionLink = functionLink;
  }

  @Subscribe
  public void onItemUse(ItemUseEvent e) {
    if (addon.getManager().isPlaying(CubeGame.TEAM_EGGWARS)
        && e.getUseType() == UseType.USE
        && e.getItemStack().getAsItem().getIdentifier().getPath().equals("fire_charge")
        && !addon.getManager().getFireballManager().onCooldown()) {
      addon.getManager().getFireballManager().setLastUse(System.currentTimeMillis());
      if (addon.configuration().getQolConfig().getFireBallCoolDown().get()) {
        functionLink.setCoolDown(e.getItemStack(),
            (int) ((FireballManager.COOLDOWN_TIME / 1000) * 20));
      }
    }
  }

}
