package org.cubepanion.core.listener;

import net.labymod.api.event.Subscribe;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.ItemUseEvent;
import org.cubepanion.core.events.ItemUseEvent.UseType;
import org.cubepanion.core.utils.CubeGame;

public class FireballCooldown {

  private final Cubepanion addon;

  public FireballCooldown(Cubepanion addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onItemUse(ItemUseEvent e) {
    if ( this.addon.getManager().isPlaying(CubeGame.TEAM_EGGWARS)
        && e.getUseType() == UseType.USE
        && e.getItemStack().getAsItem().getIdentifier().getPath().equals("fire_charge")
        && !this.addon.getManager().getFireballManager().onCooldown()) {
      this.addon.getManager().getFireballManager().setLastUse(System.currentTimeMillis());
    }
  }

}
