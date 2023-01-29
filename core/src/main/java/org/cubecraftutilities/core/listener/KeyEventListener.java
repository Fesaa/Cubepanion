package org.cubecraftutilities.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.EggWarsMapInfoSubConfig;

public class KeyEventListener {

  private final CCU addon;


  public KeyEventListener(CCU addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onKeyEvent(KeyEvent keyEvent) {
    if (!this.addon.labyAPI().minecraft().isMouseLocked()) {
      return;
    }

    // EggWars Map Info KeyBind
    EggWarsMapInfoSubConfig subConfig = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (keyEvent.key().equals(subConfig.getKey().get()) && subConfig.isEnabled().get()) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.getManager().getEggWarsMapInfoManager().doEggWarsMapLayout(this.addon.getManager().getMapName(), true);
      }
    }

    // Copy Server ID
    if (keyEvent.key().equals(this.addon.configuration().getCopyServerID().get())) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.labyAPI().minecraft().setClipboard(this.addon.getManager().getServerID());
      }
    }

    // Copy Bungeecord
    if (keyEvent.key().equals(this.addon.configuration().getCopyBungeecord().get())) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.labyAPI().minecraft().setClipboard(this.addon.getManager().getBungeecord());
      }
    }


  }

}
