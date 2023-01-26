package org.ccu.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;
import org.ccu.core.CCU;

public class GameShutdownEventListener {

  private final CCU addon;

  public GameShutdownEventListener(CCU addon) {this.addon = addon;}

  @Subscribe
  public void onGameShutdownEvent(GameShutdownEvent gameShutdownEvent) {
    this.addon.saveConfiguration();
  }

}
