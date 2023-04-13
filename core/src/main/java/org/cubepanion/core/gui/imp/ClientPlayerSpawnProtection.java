package org.cubepanion.core.gui.imp;

import org.cubepanion.core.Cubepanion;

public class ClientPlayerSpawnProtection {

  private final Cubepanion addon;

  private final SpawnProtectionComponent actionBarComponent;

  public ClientPlayerSpawnProtection(Cubepanion addon) {
    this.addon = addon;
    this.actionBarComponent = new SpawnProtectionComponent(addon, 7, 5);
  }

  public void registerDeath() {
    this.actionBarComponent.enable();
  }

  public void update(boolean endOfSecond) {
    if (this.actionBarComponent.isEnabled()) {
      this.actionBarComponent.update(endOfSecond);
      this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(this.actionBarComponent.getComponent(), true);
    }
  }


}
