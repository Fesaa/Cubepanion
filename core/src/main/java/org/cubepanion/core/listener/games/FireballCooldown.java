package org.cubepanion.core.listener.games;

import net.labymod.api.event.Subscribe;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.ItemUseEvent;
import org.cubepanion.core.events.ItemUseEvent.UseType;
import org.cubepanion.core.managers.submanagers.FireballManager;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.versionlinkers.FunctionLink;
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
