package art.ameliah.laby.addons.cubepanion.core.managers.submanagers;

import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig.layoutEnum;
import art.ameliah.laby.addons.cubepanion.core.managers.Manager;
import art.ameliah.laby.addons.cubepanion.core.utils.Utils;

public class FireballManager implements Manager {

  public static long COOLDOWN_TIME = 30 * 1000L;

  private long lastUse;

  public FireballManager() {
    this.lastUse = 0L;
  }

  public long getCooldown() {
    long c = COOLDOWN_TIME - (System.currentTimeMillis() - this.lastUse);
    return Math.max(c, 0L);
  }

  public long getLastUse() {
    return this.lastUse;
  }

  public void setLastUse(long lastUse) {
    this.lastUse = lastUse;
  }

  public boolean canUse() {
    return (System.currentTimeMillis() - this.lastUse) > COOLDOWN_TIME;
  }

  public boolean onCooldown() {
    return !this.canUse();
  }

  public String getCooldownString() {
    if (canUse()) {
      return "Ready!";
    }
    long cooldown = getCooldown();
    if (cooldown == 0) {
      return "Ready!";
    }
    return Utils.getFormattedString(cooldown, layoutEnum.SECONDS);
  }

  @Override
  public void reset() {
    this.lastUse = 0L;
  }
}
