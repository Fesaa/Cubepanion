package art.ameliah.laby.addons.cubepanion.core.managers.submanagers;

import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig.layoutEnum;
import art.ameliah.laby.addons.cubepanion.core.managers.Manager;
import art.ameliah.laby.addons.cubepanion.core.utils.Utils;
import java.util.HashMap;
import java.util.Map;

public class CooldownManager implements Manager {

  public static String FIREBALL = "fire_charge";
  public static long FIREBALL_COOLDOWN_TIME = 30 * 1000L;
  public static String FEATHER = "feather";
  public static long FEATHER_COOLDOWN_TIME = 10 * 1000L;

  private final Map<String, Long> lastUse = new HashMap<>();

  public CooldownManager() {
  }

  public long getCooldown(String id, long cooldown) {
    long c = cooldown - (System.currentTimeMillis() - this.lastUse.getOrDefault(id, 0L));
    return Math.max(c, 0L);
  }

  public long getLastUse(String id) {
    return this.lastUse.getOrDefault(id, 0L);
  }

  public void setLastUse(String id, long lastUse) {
    this.lastUse.put(id, lastUse);
  }

  public boolean canUse(String id, long cooldown) {
    return (System.currentTimeMillis() - this.getLastUse(id)) > cooldown;
  }

  public boolean onCooldown(String id, long cooldown) {
    return !this.canUse(id, cooldown);
  }

  public String getCooldownString(String id, long cooldown) {
    if (canUse(id, cooldown)) {
      return "Ready!";
    }
    long _cooldown = getCooldown(id, cooldown);
    if (_cooldown == 0) {
      return "Ready!";
    }
    return Utils.getFormattedString(_cooldown, layoutEnum.SECONDS);
  }

  @Override
  public void reset() {
    this.lastUse.clear();
  }
}
