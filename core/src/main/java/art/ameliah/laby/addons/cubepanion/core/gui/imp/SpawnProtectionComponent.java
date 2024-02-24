package art.ameliah.laby.addons.cubepanion.core.gui.imp;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class SpawnProtectionComponent {

  private final Cubepanion addon;
  private final int totalSecond;
  private final int totalMilliSecond;
  private boolean inUse;
  private long creation;

  public SpawnProtectionComponent(Cubepanion addon) {
    this.addon = addon;
    this.inUse = false;
    this.creation = System.currentTimeMillis();
    this.totalSecond = 7;
    this.totalMilliSecond = 5;
  }

  public boolean isEnabled() {
    return this.inUse;
  }

  public void enable(boolean resetCreation) {
    if (!this.addon.configuration().getQolConfig().getRespawnTimer().get()) {
      return;
    }
    this.inUse = true;
    this.creation = System.currentTimeMillis();
  }

  public Component getComponent(long currentTime) {
    long difference = currentTime - this.creation;

    long actualDifference = this.totalSecond * 1000L + this.totalMilliSecond * 100L - difference;

    long milliSeconds = Math.floorMod(actualDifference, 1000);
    long seconds = (actualDifference - milliSeconds) / 1000;

    if (!this.inUse || seconds < 0) {
      this.inUse = false;
      return Component.empty();
    }

    int actualSeconds = (int) seconds;
    int actualMilliSeconds = (int) (milliSeconds / 100);

    return Component.text(actualSeconds + ":" + actualMilliSeconds, this.getColour(actualSeconds));
  }

  private TextColor getColour(int i) {
    switch (i) {
      case 7, 6, 5 -> {
        return NamedTextColor.DARK_GREEN;
      }
      case 4, 3, 2 -> {
        return NamedTextColor.RED;
      }
      case 1, 0 -> {
        return NamedTextColor.DARK_RED;
      }
      default -> {
        return NamedTextColor.GREEN;
      }
    }
  }

}
