package org.cubecraftutilities.core.managers.submanagers;

import java.util.HashMap;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import org.cubecraftutilities.core.utils.Colours;

public class DurabilityManager {

  private final int BIG_INTEGER = 999999999;

  private String lowestString;
  private int lowestInt;
  private int secondLowestInt;

  private final HashMap<EquipmentSpot, ArmourInfo> map;

  public DurabilityManager() {

    this.lowestInt = BIG_INTEGER;
    this.secondLowestInt = BIG_INTEGER;

    this.map = new HashMap<>();
    this.map.put(EquipmentSpot.HEAD, new ArmourInfo(false, 0));
    this.map.put(EquipmentSpot.CHEST, new ArmourInfo(false, 0));
    this.map.put(EquipmentSpot.LEGS, new ArmourInfo(false, 0));
    this.map.put(EquipmentSpot.FEET, new ArmourInfo(false, 0));
  }

  public void reset() {
    this.lowestInt = BIG_INTEGER;
    this.secondLowestInt = BIG_INTEGER;

    this.map.put(EquipmentSpot.HEAD, new ArmourInfo(false, 0));
    this.map.put(EquipmentSpot.CHEST, new ArmourInfo(false, 0));
    this.map.put(EquipmentSpot.LEGS, new ArmourInfo(false, 0));
    this.map.put(EquipmentSpot.FEET, new ArmourInfo(false, 0));
  }

  public Component getWarningComponent(EquipmentSpot spot) {
    return Component.text("Your ", Colours.Error)
        .append(Component.text(this.spotToString(spot), Colours.Title, TextDecoration.BOLD))
        .append(Component.text(" will break soon!", Colours.Error).undecorate(TextDecoration.BOLD));
  }
  
  public String nextToBreakWidgetString(boolean inEditor, boolean showDifference) {
    if (inEditor) {
      return "Chestplate" + (showDifference ? " (73)" : "");
    }
    return this.lowestString + (showDifference ? " (" + (this.secondLowestInt - this.lowestInt) + ")" : "");
  }

  private String spotToString(EquipmentSpot spot) {
    switch (spot) {
      case HEAD:
        return "Helmet";
      case CHEST:
        return "Chestplate";
      case LEGS:
        return "Leggings";
      case FEET:
        return "Boots";
    }
    return "Unknown";
  }

  public void resetCache() {
    this.lowestInt = BIG_INTEGER;
    this.secondLowestInt = BIG_INTEGER;
  }

  public void updateInfo(EquipmentSpot spot, boolean warned) {
    this.map.get(spot).setWarned(warned);
  }

  public void updateInfo(EquipmentSpot spot, int durability) {
    this.map.get(spot).setDurability(durability);
    this.updateLowestCache(this.spotToString(spot), durability);
  }

  public int getDurability(EquipmentSpot spot) {
    return this.map.get(spot).getDurability();
  }

  public boolean getWarned(EquipmentSpot spot) {
    return this.map.get(spot).isWarned();
  }
  
  private void updateLowestCache(String key, int value) {
    if (value < this.lowestInt) {
      this.lowestString = key;
      this.lowestInt = value;
    } else if (value < this.secondLowestInt) {
      this.secondLowestInt = value;
    }
  }
  public static class ArmourInfo {
    private boolean warned;
    private int durability;

    public int getDurability() {
      return durability;
    }

    public void setDurability(int durability) {
      this.durability = durability;
    }

    public boolean isWarned() {
      return warned;
    }

    public void setWarned(boolean warned) {
      this.warned = warned;
    }

    public ArmourInfo(boolean warned, int durability) {
      this.warned = warned;
      this.durability = durability;
    }
  }
}

