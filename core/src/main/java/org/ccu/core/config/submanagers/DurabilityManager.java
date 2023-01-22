package org.ccu.core.config.submanagers;

public class DurabilityManager {

  private int totalHelmetDurability;
  private int totalChestPlateDurability;
  private int totalLeggingsDurability;
  private int totalBootsDurability;

  private boolean warnedHelmet;
  private boolean warnedChestplate;
  private boolean warnedLeggings;
  private boolean warnedBoots;

  public DurabilityManager() {
    this.totalHelmetDurability = 0;
    this.totalChestPlateDurability = 0;
    this.totalLeggingsDurability = 0;
    this.totalBootsDurability = 0;

    this.warnedHelmet = false;
    this.warnedChestplate = false;
    this.warnedLeggings = false;
    this.warnedBoots = false;
  }

  public void reset() {
    this.totalHelmetDurability = 0;
    this.totalChestPlateDurability = 0;
    this.totalLeggingsDurability = 0;
    this.totalBootsDurability = 0;

    this.warnedHelmet = false;
    this.warnedChestplate = false;
    this.warnedLeggings = false;
    this.warnedBoots = false;
  }

  public void setTotalLeggingsDurability(int totalLeggingsDurability) {
    this.totalLeggingsDurability = totalLeggingsDurability;
  }

  public void setTotalHelmetDurability(int totalHelmetDurability) {
    this.totalHelmetDurability = totalHelmetDurability;
  }

  public void setTotalChestPlateDurability(int totalChestPlateDurability) {
    this.totalChestPlateDurability = totalChestPlateDurability;
  }

  public void setTotalBootsDurability(int totalBootsDurability) {
    this.totalBootsDurability = totalBootsDurability;
  }

  public int getTotalLeggingsDurability() {
    return totalLeggingsDurability;
  }

  public int getTotalHelmetDurability() {
    return totalHelmetDurability;
  }

  public int getTotalBootsDurability() {
    return totalBootsDurability;
  }

  public int getTotalChestPlateDurability() {
    return totalChestPlateDurability;
  }

  public boolean isWarnedBoots() {
    return warnedBoots;
  }

  public boolean isWarnedChestplate() {
    return warnedChestplate;
  }

  public boolean isWarnedHelmet() {
    return warnedHelmet;
  }

  public boolean isWarnedLeggings() {
    return warnedLeggings;
  }

  public void setWarnedBoots(boolean warnedBoots) {
    this.warnedBoots = warnedBoots;
  }

  public void setWarnedChestplate(boolean warnedChestplate) {
    this.warnedChestplate = warnedChestplate;
  }

  public void setWarnedHelmet(boolean warnedHelmet) {
    this.warnedHelmet = warnedHelmet;
  }

  public void setWarnedLeggings(boolean warnedLeggings) {
    this.warnedLeggings = warnedLeggings;
  }
}

