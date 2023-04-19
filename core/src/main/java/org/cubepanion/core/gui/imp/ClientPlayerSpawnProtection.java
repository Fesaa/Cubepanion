package org.cubepanion.core.gui.imp;

import net.labymod.api.client.chat.ChatExecutor;
import org.cubepanion.core.Cubepanion;

public class ClientPlayerSpawnProtection {

  private final ChatExecutor chatExecutor;

  private final SpawnProtectionComponent actionBarComponent;

  public ClientPlayerSpawnProtection(Cubepanion addon) {
    this.chatExecutor = addon.labyAPI().minecraft().chatExecutor();
    this.actionBarComponent = new SpawnProtectionComponent(addon);
  }

  public void registerDeath() {
    this.actionBarComponent.enable(true);
  }

  public void update() {
    if (this.actionBarComponent.isEnabled()) {
      this.chatExecutor.displayClientMessage(this.actionBarComponent.getComponent(System.currentTimeMillis()), true);
    }
  }


}
