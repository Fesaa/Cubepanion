package org.ccu.core.gui.imp;

import org.ccu.core.CCU;

public class ClientPlayerSpawnProtection {

  private final CCU addon;

  private final SpawnProtectionComponent actionBarComponent;

  public ClientPlayerSpawnProtection(CCU addon) {
    this.addon = addon;
    this.actionBarComponent = new SpawnProtectionComponent(addon);
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
