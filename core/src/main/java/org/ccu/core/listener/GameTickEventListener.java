package org.ccu.core.listener;

import com.google.inject.Inject;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import org.ccu.core.CCU;

public class GameTickEventListener {

  private final CCU addon;
  private int counter = 0;

  @Inject
  public GameTickEventListener(CCU addon) {this.addon = addon;}

  @Subscribe
  public void onGameTickEvent(GameTickEvent gameTickEvent) {
    if (gameTickEvent.phase() == Phase.POST) {
      return;
    }

    int ticksInAMinute = 20 * 60;
    if (this.counter % ticksInAMinute == 0) {
      this.addon.configuration().getStatsTrackerSubConfig().checkForResets();
      this.addon.saveConfiguration();
    }
    if (this.counter % 20 == 0) {
      this.addon.clientPlayerSpawnProtection.update(true);
      this.addon.getManager().updateSpawnProtectionComponentHashMap(true);
    } else if (this.counter % 2 == 0) {
      this.addon.clientPlayerSpawnProtection.update(false);
      this.addon.getManager().updateSpawnProtectionComponentHashMap(false);
    }
    this.counter++;
  }

}
