package org.cubecraftutilities.core.gui.imp;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import org.cubecraftutilities.core.CCU;

public class SpawnProtectionComponent {

  private final CCU addon;
  private int second;
  private int precisionSecond;
  private boolean inUse;

  private final int startSecond = 7;
  private final int startPrecisionSecond = 5;

  public SpawnProtectionComponent(CCU addon) {
    this.addon = addon;
    this.second = this.startSecond;
    this.precisionSecond = this.startPrecisionSecond;
    this.inUse = false;
  }

  public SpawnProtectionComponent(CCU addon, int startSecond, int startPrecisionSecond) {
    this.addon = addon;
    this.second = startSecond;
    this.precisionSecond = startPrecisionSecond;
    this.inUse = false;
  }

  public boolean isEnabled() {
    return this.inUse;
  }

  public void enable() {
    if (!this.addon.configuration().getRespawnTimer().get()) {
      return;
    }
    this.inUse = true;
  }

  private void tryToDisable() {
    if (this.second == 0 && this.precisionSecond == 0) {
      this.second = this.startSecond;
      this.precisionSecond = this.startPrecisionSecond;
      this.inUse = false;
    }
  }

  private void lowerCount(boolean endOfSecond) {
    if (endOfSecond) {
      this.second--;
      this.precisionSecond = 9;
    } else {
      this.precisionSecond--;
    }
  }

  public void update(boolean endOfSecond) {
    this.tryToDisable();
    this.lowerCount(endOfSecond);
  }

  public Component getComponent() {
    if (!this.inUse) {
      return Component.empty();
    }
    return Component.text(second + ":" + precisionSecond, this.getColour(this.second));
  }

  private TextColor getColour(int i) {
    switch (i) {
      case 7:
      case 6:
      case 5:{
        return NamedTextColor.DARK_GREEN;
      }
      case 4:
      case 3:
      case 2:{
        return NamedTextColor.RED;
      }
      case 1:
      case 0:{
        return NamedTextColor.DARK_RED;
      }
      default: {
        return NamedTextColor.GREEN;
      }
    }
  }

}
