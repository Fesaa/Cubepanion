package org.ccu.core;

import org.ccu.core.config.imp.GameStatsTracker;
import org.ccu.core.config.internal.CCUinternalConfig;
import org.ccu.core.config.subconfig.StatsTrackerSubConfig;
import org.ccu.core.gui.hud.widgets.CounterItemHudWidget;
import org.ccu.core.gui.hud.widgets.DurabilityItemHudWidget;
import org.ccu.core.gui.hud.widgets.NextArmourBuyTextWidget;
import org.ccu.core.gui.hud.widgets.TextTrackerHudWidget;

public class WidgetManager {
  
  private final CCU addon;
  
  public WidgetManager(CCU addon) {this.addon = addon;}
  
  public void register() {
    this.addon.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("emerald_counter","emerald", 3, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("diamond_counter","diamond", 1, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("iron_ingot_counter","iron_ingot", 0, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("gold_ingot_counter","gold_ingot", 2, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("terracotta_counter","(\\w{0,10}\\_{0,1}){0,2}terracotta", 4, 0));

    this.addon.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("helmet_durability_counter", "\\w{0,10}_helmet", 5, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("chestplate_durability_counter", "\\w{0,10}_chestplate", 6, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("leggings_durability_counter", "\\w{0,10}_leggings", 7, 0));
    this.addon.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("boots_durability_counter", "\\w{0,10}_boots", 0, 1));

    this.addon.labyAPI().hudWidgetRegistry().register(new NextArmourBuyTextWidget("nextArmourDurability"));
    this.addon.labyAPI().hudWidgetRegistry().register(new TextTrackerHudWidget("current_winstreak_tracker", "Win Streak",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(
              CCUinternalConfig.name);
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getCurrentWinStreak());
          }
          return "";
        },
        this::booleanSupplier,
        2,
        1));
    this.addon.labyAPI().hudWidgetRegistry().register(new TextTrackerHudWidget("alltime_max_winstreak_tracker", "All Time High Win Streak",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(CCUinternalConfig.name);
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getAllTimeHighestWinStreak());
          }
          return "";
        },
        this::booleanSupplier,
        3,
        1));
    this.addon.labyAPI().hudWidgetRegistry().register(new TextTrackerHudWidget("daily_max_winstreak_tracker", "Daily Highest Win Streak",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(CCUinternalConfig.name);
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getDailyHighestWinStreak());
          }
          return "";
        },
        this::booleanSupplier,
        4,
        1));
    this.addon.labyAPI().hudWidgetRegistry().register(new TextTrackerHudWidget("daily_winstreak_tracker", "Daily Win Streak",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(CCUinternalConfig.name);
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getDailyWinStreak());
          }
          return "";
        },
        this::booleanSupplier,
        2,
        1));
  }

  private boolean booleanSupplier() {
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(CCUinternalConfig.name);
    return gameStatsTracker != null;
  }

}
