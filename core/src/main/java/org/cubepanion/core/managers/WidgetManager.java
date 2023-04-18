package org.cubepanion.core.managers;

import java.util.function.BooleanSupplier;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.gui.hud.HudWidgetRegistry;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import org.cubepanion.core.gui.hud.widgets.CounterItemHudWidget;
import org.cubepanion.core.gui.hud.widgets.DurabilityItemHudWidget;
import org.cubepanion.core.gui.hud.widgets.GameTimerWidget;
import org.cubepanion.core.gui.hud.widgets.NextArmourBuyTextWidget;
import org.cubepanion.core.gui.hud.widgets.TextTrackerHudWidget;
import org.cubepanion.core.gui.hud.widgets.base.CubepanionWidgetCategory;

//TODO: Maybe! Add ...
// Custom text widget
// Deaths, Kills, etc. per game tracker
// Party information
public class WidgetManager {
  
  private final Cubepanion addon;
  
  public WidgetManager(Cubepanion addon) {this.addon = addon;}
  
  public void register() {
    CubepanionManager manager = this.addon.getManager();
    HudWidgetRegistry hudWidgetRegistry = this.addon.labyAPI().hudWidgetRegistry();

    HudWidgetCategory category = new CubepanionWidgetCategory("Cubepanion");
    hudWidgetRegistry.categoryRegistry().register(category);
    
    hudWidgetRegistry.register(new CounterItemHudWidget(category, "emerald_counter","emerald", "emerald", 3, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"diamond_counter","diamond", "diamond", 1, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"iron_ingot_counter","iron_ingot", "iron_ingot", 0, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"gold_ingot_counter","gold_ingot", "gold_ingot", 2, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category,"terracotta_counter","(\\w{0,10}\\_{0,1}){0,2}terracotta", "white_terracotta", 4, 0));

    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"helmet_durability_counter", "helmet", EquipmentSpot.HEAD, 5, 0, manager));
    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"chestplate_durability_counter", "chestplate", EquipmentSpot.CHEST, 6, 0, manager));
    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"leggings_durability_counter", "leggings", EquipmentSpot.LEGS, 7, 0, manager));
    hudWidgetRegistry.register(new DurabilityItemHudWidget(category,"boots_durability_counter", "boots", EquipmentSpot.FEET, 0, 1, manager));

    hudWidgetRegistry.register(new NextArmourBuyTextWidget(category,"nextArmourDurability", manager));

    BooleanSupplier statsTrackerEnabled = () -> this.addon.configuration().getStatsTrackerSubConfig().isEnabled();

    // Wins / Played
    hudWidgetRegistry.register(new TextTrackerHudWidget(category,"daily_wins_tracker", "Wins/Games", "7/9",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivision());
          if (gameStatsTracker != null) {
            return gameStatsTracker.getDailyWins() + "/" + gameStatsTracker.getDailyPlayed();
          }
          return "";
        },
        this::booleanSupplier, 2, 1, statsTrackerEnabled));

    // Win Streak
    hudWidgetRegistry.register(new TextTrackerHudWidget(category,"all_time_winstreak_tracker", "Win Streak", "0",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivision());
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getWinStreak());
          }
          return "";
        },
        this::booleanSupplier, 3, 1, statsTrackerEnabled));

    // Daily Win Streak
    hudWidgetRegistry.register(new TextTrackerHudWidget(category,"daily_winstreak_tracker", "Daily Win Streak", "0",
        () -> {
          StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
          GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivision());
          if (gameStatsTracker != null) {
            return String.valueOf(gameStatsTracker.getDailyWinStreak());
          }
          return "";
        },
        this::booleanSupplier, 2, 1, statsTrackerEnabled));

    // Game Timer
    hudWidgetRegistry.register(new GameTimerWidget(category, "elapsed_time_tracker",5, 1));
  }

  private boolean booleanSupplier() {
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(this.addon.getManager().getDivision());
    return gameStatsTracker != null;
  }

}
