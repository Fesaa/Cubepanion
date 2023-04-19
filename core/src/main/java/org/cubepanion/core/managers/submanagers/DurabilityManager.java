package org.cubepanion.core.managers.submanagers;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;

public class DurabilityManager {

  private final int BIG_INTEGER = 999999999;
  private final String mainKey;

  private String lowestString;
  private int lowestInt;
  private int secondLowestInt;

  private ArmourInfo helmetInfo;
  private ArmourInfo chestplateInfo;
  private ArmourInfo leggignsInfo;
  private ArmourInfo bootsInfo;

  public DurabilityManager() {
    this.mainKey = I18nNamespaces.managerNameSpace + "DurabilityManager.";

    this.lowestInt = BIG_INTEGER;
    this.secondLowestInt = BIG_INTEGER;

    this.helmetInfo = new ArmourInfo(false, 0);
    this.chestplateInfo = new ArmourInfo(false, 0);
    this.leggignsInfo = new ArmourInfo(false, 0);
    this.bootsInfo = new ArmourInfo(false, 0);
  }

  public void reset() {
    this.lowestInt = BIG_INTEGER;
    this.secondLowestInt = BIG_INTEGER;

    this.helmetInfo = new ArmourInfo(false, 0);
    this.chestplateInfo = new ArmourInfo(false, 0);
    this.leggignsInfo = new ArmourInfo(false, 0);
    this.bootsInfo = new ArmourInfo(false, 0);
  }

  public Component getWarningComponent(EquipmentSpot spot) {
    return Component.translatable(this.mainKey + "warning",
        Component.text(this.spotToString(spot), Colours.Title, TextDecoration.BOLD))
        .color(Colours.Error).undecorate(TextDecoration.BOLD);
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
    switch (spot) {
      case HEAD:
        this.helmetInfo.setWarned(warned);
      case CHEST:
        this.chestplateInfo.setWarned(warned);
      case LEGS:
        this.leggignsInfo.setWarned(warned);
      case FEET:
        this.bootsInfo.setWarned(warned);
    }
  }

  public void updateInfo(EquipmentSpot spot, int durability) {
    switch (spot) {
      case HEAD:
        this.helmetInfo.setDurability(durability);
      case CHEST:
        this.chestplateInfo.setDurability(durability);
      case LEGS:
        this.leggignsInfo.setDurability(durability);
      case FEET:
        this.bootsInfo.setDurability(durability);
    }
    this.updateLowestCache(this.spotToString(spot), durability);
  }

  public int getDurability(EquipmentSpot spot) {
    switch (spot) {
      case HEAD:
        return this.helmetInfo.getDurability();
      case CHEST:
        return this.chestplateInfo.getDurability();
      case LEGS:
        return this.leggignsInfo.getDurability();
      case FEET:
        return this.bootsInfo.getDurability();
    }
    return 0;
  }

  public boolean getWarned(EquipmentSpot spot) {
    switch (spot) {
      case HEAD:
        return this.helmetInfo.isWarned();
      case CHEST:
        return this.chestplateInfo.isWarned();
      case LEGS:
        return this.leggignsInfo.isWarned();
      case FEET:
        return this.bootsInfo.isWarned();
    }
    return false;
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

