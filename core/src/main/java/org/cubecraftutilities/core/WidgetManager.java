package org.cubecraftutilities.core;

import net.labymod.api.client.gui.hud.HudWidgetRegistry;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import org.cubecraftutilities.core.config.CCUManager;
import org.cubecraftutilities.core.config.imp.GameStatsTracker;
import org.cubecraftutilities.core.config.subconfig.StatsTrackerSubConfig;
import org.cubecraftutilities.core.gui.hud.widgets.CounterItemHudWidget;
import org.cubecraftutilities.core.gui.hud.widgets.DurabilityItemHudWidget;
import org.cubecraftutilities.core.gui.hud.widgets.HeldItemTracker;
import org.cubecraftutilities.core.gui.hud.widgets.NextArmourBuyTextWidget;
import org.cubecraftutilities.core.gui.hud.widgets.TextTrackerHudWidget;
import org.cubecraftutilities.core.gui.hud.widgets.base.CCUWidgetCategory;

//TODO: Maybe! Add ...
// Custom text widget
// Deaths, Kills, etc. per game tracker
// Party information
public class WidgetManager {
  
  private final CCU addon;
  
  public WidgetManager(CCU addon) {this.addon = addon;}
  
  public void register() {
    CCUManager manager = this.addon.getManager();
    HudWidgetRegistry hudWidgetRegistry = this.addon.labyAPI().hudWidgetRegistry();

    HudWidgetCategory category = new CCUWidgetCategory("CCU");
    hudWidgetRegistry.categoryRegistry().register(category);
    
    hudWidgetRegistry.register(new CounterItemHudWidget(category, "emerald_counter","emerald", 3, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"diamond_counter","diamond", 1, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"iron_ingot_counter","iron_ingot", 0, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"gold_ingot_counter","gold_ingot", 2, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"terracotta_counter","(\\w{0,10}\\_{0,1}){0,2}terracotta", 4, 0));
    hudWidgetRegistry.register(new HeldItemTracker(category));

    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"helmet_durability_counter", "\\w{0,10}_helmet", 5, 0, manager));
    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"chestplate_durability_counter", "\\w{0,10}_chestplate", 6, 0, manager));
    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"leggings_durability_counter", "\\w{0,10}_leggings", 7, 0, manager));
    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"boots_durability_counter", "\\w{0,10}_boots", 0, 1, manager));

    hudWidgetRegistry.register(new NextArmourBuyTextWidget(category,"nextArmourDurability", manager));

    // Wins / Played
    hudWidgetRegistry.register(new TextTrackerHudWidget(category,"daily_wins_tracker", "Wins/Games",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivisionName());
          if (gameStatsTracker != null) {
            return gameStatsTracker.getDailyWins() + "/" + gameStatsTracker.getDailyPlayed();
          }
          return "";
        },
        this::booleanSupplier, 2, 1));

    // Win Streak
    hudWidgetRegistry.register(new TextTrackerHudWidget(category,"all_time_winstreak_tracker", "Win Streak",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(
              manager.getDivisionName());
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getWinStreak());
          }
          return "";
        },
        this::booleanSupplier, 3, 1));

    // Daily Win Streak
    hudWidgetRegistry.register(new TextTrackerHudWidget(category,"daily_winstreak_tracker", "Daily Win Streak",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivisionName());
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getDailyWinStreak());
          }
          return "";
        },
        this::booleanSupplier, 2, 1));
  }

  private boolean booleanSupplier() {
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(this.addon.getManager().getDivisionName());
    return gameStatsTracker != null;
  }

}
