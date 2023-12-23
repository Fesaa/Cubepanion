package org.cubepanion.core;


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
import org.cubepanion.core.commands.LeaderboardAPICommands;
import org.cubepanion.core.commands.LeaderboardMappings;
import org.cubepanion.core.commands.MapCommand;
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
import org.cubepanion.core.listener.internal.InternalTrackers;
import org.cubepanion.core.listener.games.GameListeners;
import org.cubepanion.core.listener.hud.HudEvents;
import org.cubepanion.core.listener.misc.MiscListeners;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.WidgetManager;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.LOGGER;
import org.cubepanion.core.versionlinkers.ChestFinderLink;
import org.cubepanion.core.versionlinkers.FunctionLink;
import org.cubepanion.core.versionlinkers.LeaderboardTrackerLink;
import org.cubepanion.core.versionlinkers.QOLMapSelectorLink;
import org.cubepanion.core.versionlinkers.VotingLink;
import org.cubepanion.core.weave.ChestAPI;
import org.cubepanion.core.weave.EggWarsMapAPI;
import org.cubepanion.core.weave.LeaderboardAPI;
import org.jetbrains.annotations.Nullable;

@AddonMain
public class Cubepanion extends LabyAddon<CubepanionConfig> {
  private static Cubepanion instance;
  private CubepanionManager manager;

  private VotingLink votingLink;
  private ChestFinderLink chestFinderLink;
  private LeaderboardTrackerLink leaderboardTrackerLink;
  private QOLMapSelectorLink qolMapSelectorLink;
  private FunctionLink functionLink;

  public Cubepanion() {
    instance = this;
  }

  public static Cubepanion get() {
    return instance;
  }

  @Override
  protected void enable() {
    LOGGER.setLog(logger());
    LOGGER.info(getClass(), "Starting Cubepanion");
    this.registerSettingCategory();

    if (Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      LOGGER.debug(getClass(), "Set LeaderboardAPIConfig#errorInfo & CubepanionConfig#showDebug true");
      this.configuration().getLeaderboardAPIConfig().getErrorInfo().set(true);
      this.configuration().getShowDebug().set(true);
    }

    ChestAPI.Init();
    EggWarsMapAPI.Init();
    LeaderboardAPI.Init();

    DefaultReferenceStorage storage = this.referenceStorageAccessor();
    votingLink = storage.getVotingLink();
    leaderboardTrackerLink = storage.getLeaderboardTrackerLink();
    qolMapSelectorLink = storage.getQOLMapSelectorLink();
    chestFinderLink = storage.getChestFinderLink();
    functionLink = storage.getFunctionLink();
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
    if (functionLink == null) {
      LOGGER.warn(getClass(), "FunctionLink is null. Some features will not work.");
    }

    this.manager = new CubepanionManager(this);
    LOGGER.setManager(this.manager);

    this.registerCommand(new PartyCommands("party", this));
    this.registerCommand(new PartyCommands("p", this));
    this.registerCommand(new AppealSiteCommand(this));
    this.registerCommand(new EggWarsMapInfoCommand(this));
    this.registerCommand(new StatCommands(this));
    this.registerCommand(new FriendListCommand(this));
    this.registerCommand(new MapCommand(this));
    this.registerCommand(new TeamColourCommand(this));
    this.registerCommand(new DivisionCommand(this));
    this.registerCommand(new LeaderboardAPICommands(this));
    this.registerCommand(new FindChestCommand(this, chestFinderLink));
    this.registerCommand(new LeaderboardMappings(this));

    this.registerListener(new GameTickEventListener(this));
    this.registerListener(new GameShutdownEventListener(this));
    this.registerListener(new KeyEventListener(this, qolMapSelectorLink));
    this.registerListener(new Automations(this));
    this.registerListener(new ScreenListener(this, leaderboardTrackerLink, qolMapSelectorLink));
    this.registerListener(new HudEvents(this));

    GameListeners.register(this);
    MiscListeners.register(this);
    InternalTrackers.register(this);

    RespawnTags respawnTags = new RespawnTags(this);
    RankTag rankTag = new RankTag(this);

    this.registerListener(respawnTags);
    this.labyAPI().tagRegistry().register("respawn_timer", PositionType.ABOVE_NAME, respawnTags);
    this.labyAPI().tagRegistry().register("rank_tag", PositionType.ABOVE_NAME, rankTag);

    WidgetManager.register(this);

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

  @Nullable
  public VotingLink getVotingLink() {
    return votingLink;
  }

  @Nullable
  public ChestFinderLink getChestFinderLink() {
    return chestFinderLink;
  }

  @Nullable
  public LeaderboardTrackerLink getLeaderboardTrackerLink() {
    return leaderboardTrackerLink;
  }

  @Nullable
  public QOLMapSelectorLink getQolMapSelectorLink() {
    return qolMapSelectorLink;
  }

  @Nullable
  public FunctionLink getFunctionLink() {
    return functionLink;
  }

  public void registerCubepanionListener(Object listener) {
    this.registerListener(listener);
  }

  @Override
  protected Class<CubepanionConfig> configurationClass() {
    return CubepanionConfig.class;
  }
}
