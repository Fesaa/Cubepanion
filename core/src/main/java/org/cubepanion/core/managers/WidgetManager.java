package org.cubepanion.core.managers;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.HudWidgetRegistry;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.resources.ResourceLocation;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import org.cubepanion.core.gui.hud.widgets.CounterItemHudWidget;
import org.cubepanion.core.gui.hud.widgets.DurabilityItemHudWidget;
import org.cubepanion.core.gui.hud.widgets.GameTimerWidget;
import org.cubepanion.core.gui.hud.widgets.NextArmourBuyTextWidget;
import org.cubepanion.core.gui.hud.widgets.TextTrackerHudWidget;
import org.cubepanion.core.gui.hud.widgets.base.CubepanionWidgetCategory;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;

//TODO: Maybe! Add ...
// Custom text widget
// Deaths, Kills, etc. per game tracker
// Party information
public class WidgetManager {

  private final Cubepanion addon;

  public WidgetManager(Cubepanion addon) {
    this.addon = addon;
  }

  public void register() {
    CubepanionManager manager = this.addon.getManager();
    HudWidgetRegistry hudWidgetRegistry = this.addon.labyAPI().hudWidgetRegistry();

    HudWidgetCategory category = new CubepanionWidgetCategory("Cubepanion");
    hudWidgetRegistry.categoryRegistry().register(category);

    hudWidgetRegistry.register(
        new CounterItemHudWidget(category, "emerald_counter", "emerald", 3, 0));
    hudWidgetRegistry.register(
        new CounterItemHudWidget(category, "diamond_counter", "diamond", 1, 0));
    hudWidgetRegistry.register(
        new CounterItemHudWidget(category, "iron_ingot_counter", "iron_ingot", 0, 0));
    hudWidgetRegistry.register(
        new CounterItemHudWidget(category, "gold_ingot_counter", "gold_ingot", 2, 0));
    hudWidgetRegistry.register(new CounterItemHudWidget(category, "concrete_counter",
        "(\\w{0,10}\\_{0,1}){0,2}concrete", "white_concrete", 4, 0));

    hudWidgetRegistry.register(
        new DurabilityItemHudWidget(category, "helmet_durability_counter", "helmet",
            EquipmentSpot.HEAD, 5, 0, manager));
    hudWidgetRegistry.register(
        new DurabilityItemHudWidget(category, "chestplate_durability_counter", "chestplate",
            EquipmentSpot.CHEST, 6, 0, manager));
    hudWidgetRegistry.register(
        new DurabilityItemHudWidget(category, "leggings_durability_counter", "leggings",
            EquipmentSpot.LEGS, 7, 0, manager));
    hudWidgetRegistry.register(
        new DurabilityItemHudWidget(category, "boots_durability_counter", "boots",
            EquipmentSpot.FEET, 0, 1, manager));

    hudWidgetRegistry.register(
        new NextArmourBuyTextWidget(category, "nextArmourDurability", manager));

    BooleanSupplier statsTrackerEnabled = () -> this.addon.configuration()
        .getStatsTrackerSubConfig().isEnabled();

    // Wins / Played
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration()
        .getStatsTrackerSubConfig();
    hudWidgetRegistry.register(
        new TextTrackerHudWidget(category, "daily_wins_tracker", "Wins/Games", "7/9",
            () -> {
              GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers()
                  .get(manager.getDivision());
              if (gameStatsTracker != null) {
                return gameStatsTracker.getDailyWins() + "/" + gameStatsTracker.getDailyPlayed();
              }
              return "";
            },
            this::booleanSupplier, 2, 1, statsTrackerEnabled));

    // Win Streak
    hudWidgetRegistry.register(
        new TextTrackerHudWidget(category, "all_time_winstreak_tracker", "Win Streak", "0",
            () -> {
              GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers()
                  .get(manager.getDivision());
              if (gameStatsTracker != null) {
                return String.valueOf(gameStatsTracker.getWinStreak());
              }
              return "";
            },
            this::booleanSupplier, 3, 1, statsTrackerEnabled));

    // Daily Win Streak
    hudWidgetRegistry.register(
        new TextTrackerHudWidget(category, "daily_winstreak_tracker", "Daily Win Streak", "0",
            () -> {
              GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers()
                  .get(manager.getDivision());
              if (gameStatsTracker != null) {
                return String.valueOf(gameStatsTracker.getDailyWinStreak());
              }
              return "";
            },
            this::booleanSupplier, 2, 1, statsTrackerEnabled));

    // Party chat
    hudWidgetRegistry.register(
        new TextTrackerHudWidget(category, "party_chat_tracker", "Party Chat", "Off",
            () -> {
              if (!addon.getManager().onCubeCraft()) {
                return "";
              }
              return addon.getManager().getPartyManager().isPartyChat() ? "On" : "Off";
            },
            () -> {
              if (!addon.getManager().onCubeCraft()) {
                return false;
              }
              return addon.getManager().getPartyManager().isInParty();
            }, 5, 1, () -> true));
    // Distance to build limit
    AtomicBoolean done = new AtomicBoolean(false);
    ResourceLocation sound = ResourceLocation.create("minecraft", "entity.experience_orb.pickup");
    hudWidgetRegistry.register(
        new TextTrackerHudWidget(category, "distance_to_build_limit", "Build limit in", "0",
            () -> {
              EggWarsMap map = this.addon.getManager().getCurrentEggWarsMap();
              if (map == null) {
                return "";
              }
              Minecraft mc = this.addon.labyAPI().minecraft();
              ClientPlayer p = mc.getClientPlayer();
              if (p == null) {
                return "";
              }

              int d = (int) (map.getBuildLimit() - p.getPosY());
              if (d < 3 && d > 0) {
                if (!done.get() && !this.addon.getManager().isInPreLobby()
                    && !this.addon.getManager().isEliminated()) {
                  mc.sounds().playSound(sound, 1.0F, 1.0F);
                  mc.chatExecutor().displayClientMessage(
                      Component.translatable("cubepanion.messages.build_limit_reached",
                          Colours.Error, Component.text(d)));
                  done.set(true);
                }
              } else if (d > 5) {
                done.set(false);
              }

              return String.valueOf(d);
            },
            () -> this.addon.getManager().getDivision().equals(CubeGame.TEAM_EGGWARS)
                && !this.addon.getManager().isInPreLobby(), 5, 1, () -> true));

    // Game Timer
    hudWidgetRegistry.register(new GameTimerWidget(category, "elapsed_time_tracker", 5, 1));
  }

  private boolean booleanSupplier() {
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration()
        .getStatsTrackerSubConfig();
    GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers()
        .get(this.addon.getManager().getDivision());
    return gameStatsTracker != null;
  }

}
