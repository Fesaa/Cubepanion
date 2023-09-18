package org.cubepanion.core;

import art.ameliah.libs.weave.ChestAPI.ChestLocation;
import art.ameliah.libs.weave.Weave;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.cubepanion.core.commands.AppealSiteCommand;
import org.cubepanion.core.commands.DivisionCommand;
import org.cubepanion.core.commands.EggWarsMapInfoCommand;
import org.cubepanion.core.commands.FindChestCommand;
import org.cubepanion.core.commands.FriendListCommand;
import org.cubepanion.core.commands.FriendLocationCommand;
import org.cubepanion.core.commands.LeaderboardAPICommands;
import org.cubepanion.core.commands.MapCommand;
import org.cubepanion.core.commands.OnlineFriendTrackerCommand;
import org.cubepanion.core.commands.PartyCommands;
import org.cubepanion.core.commands.StatCommands;
import org.cubepanion.core.commands.TeamColourCommand;
import org.cubepanion.core.config.CubepanionConfig;
import org.cubepanion.core.generated.DefaultReferenceStorage;
import org.cubepanion.core.gui.hud.nametags.RankTag;
import org.cubepanion.core.gui.hud.nametags.RespawnTags;
import org.cubepanion.core.listener.GameShutdownEventListener;
import org.cubepanion.core.listener.GameTickEventListener;
import org.cubepanion.core.listener.KeyEventListener;
import org.cubepanion.core.listener.ScreenListener;
import org.cubepanion.core.listener.chat.Automations;
import org.cubepanion.core.listener.chat.PartyTracker;
import org.cubepanion.core.listener.chat.StatsTracker;
import org.cubepanion.core.listener.hud.HudEvents;
import org.cubepanion.core.listener.network.PlayerInfo;
import org.cubepanion.core.listener.network.ScoreboardListener;
import org.cubepanion.core.listener.network.ServerNavigation;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.DiscordRPCManager;
import org.cubepanion.core.managers.WidgetManager;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.LOGGER;
import org.cubepanion.core.versionlinkers.ChestFinderLink;
import org.cubepanion.core.versionlinkers.LeaderboardTrackerLink;
import org.cubepanion.core.versionlinkers.QOLMapSelectorLink;
import org.cubepanion.core.versionlinkers.VotingLink;

@AddonMain
public class Cubepanion extends LabyAddon<CubepanionConfig> {

  public static Weave weave;
  public static List<ChestLocation> chestLocations = new ArrayList<>();
  public static String season = "";
  private static Cubepanion instance;
  public DiscordRPCManager rpcManager;
  public WidgetManager widgetManager;
  private CubepanionManager manager;

  public Cubepanion() {
    instance = this;
  }

  public static Cubepanion get() {
    return instance;
  }

  public static void updateRPC() {
    if (instance != null && instance.rpcManager != null) {
      instance.rpcManager.updateRPC();
    }
  }

  @Override
  protected void enable() {
    LOGGER.setLog(logger());
    LOGGER.info(getClass(), "Starting Cubepanion");
    this.registerSettingCategory();

    if (Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      LOGGER.debug(getClass(), "Set LeaderboardAPIConfig#errorInfo true");
      this.configuration().getLeaderboardAPIConfig().getErrorInfo().set(true);
    }

    if (System.getProperty("weave.dev") != null && System.getProperty("weave.dev").equals("true")) {
      weave = Weave.Dev(false);
    } else {
      weave = Weave.Production();
    }

    DefaultReferenceStorage storage = this.referenceStorageAccessor();
    VotingLink votingLink = storage.getVotingLink();
    LeaderboardTrackerLink leaderboardTrackerLink = storage.getLeaderboardTrackerLink();
    QOLMapSelectorLink qolMapSelectorLink = storage.getQOLMapSelectorLink();
    ChestFinderLink chestFinderLink = storage.getChestFinderLink();
    if (votingLink == null) {
      LOGGER.warn(getClass(), "VotingLink is null. Some features will not work.");
    }
    if (leaderboardTrackerLink == null) {
      LOGGER.warn(getClass(), "LeaderboardTrackerLink is null. Some features will not work.");
    }
    if (qolMapSelectorLink == null) {
      LOGGER.warn(getClass(), "QOLMapSelectorLink is null. Some features will not work.");
    }
    if (chestFinderLink == null) {
      LOGGER.warn(getClass(), "ChestFinderLink is null. Some features will not work.");
    }

    this.manager = new CubepanionManager(this);
    this.rpcManager = new DiscordRPCManager(this);
    this.widgetManager = new WidgetManager(this);

    LOGGER.setManager(this.manager);

    this.registerCommand(new PartyCommands("party", this));
    this.registerCommand(new PartyCommands("p", this));
    this.registerCommand(new AppealSiteCommand(this));
    this.registerCommand(new EggWarsMapInfoCommand(this));
    this.registerCommand(new StatCommands(this));
    this.registerCommand(new FriendListCommand(this));
    this.registerCommand(new OnlineFriendTrackerCommand());
    this.registerCommand(new MapCommand(this));
    this.registerCommand(new TeamColourCommand(this));
    this.registerCommand(new DivisionCommand(this));
    this.registerCommand(new FriendLocationCommand(this));
    this.registerCommand(new LeaderboardAPICommands(this));
    this.registerCommand(new FindChestCommand(this, chestFinderLink));

    this.registerListener(new PlayerInfo(this));
    this.registerListener(new ServerNavigation(this));
    this.registerListener(new GameTickEventListener(this));
    this.registerListener(new GameShutdownEventListener(this));
    this.registerListener(new KeyEventListener(this, qolMapSelectorLink));
    this.registerListener(new Automations(this, votingLink, chestFinderLink));
    this.registerListener(new PartyTracker(this));
    this.registerListener(new StatsTracker(this));
    this.registerListener(new ScoreboardListener(this));
    this.registerListener(new ScreenListener(this, leaderboardTrackerLink, qolMapSelectorLink));
    this.registerListener(new HudEvents(this));

    this.labyAPI().tagRegistry()
        .register("respawn_timer", PositionType.ABOVE_NAME, new RespawnTags(this));
    this.labyAPI().tagRegistry()
        .register("rank_tag", PositionType.ABOVE_NAME, new RankTag(this));

    this.widgetManager.register();

    LOGGER.info(getClass(), "Cubepanion has successfully registered all her components.");
  }

  public CubepanionManager getManager() {
    return this.manager;
  }

  public Component prefix() {
    return Component.text("[", Colours.Title)
        .append(Component.text("Cubepanion", Colours.Primary))
        .append(Component.text("]", Colours.Title));
  }

  @Override
  protected Class<CubepanionConfig> configurationClass() {
    return CubepanionConfig.class;
  }
}
