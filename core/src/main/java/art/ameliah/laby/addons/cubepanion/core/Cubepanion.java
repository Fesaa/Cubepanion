package art.ameliah.laby.addons.cubepanion.core;


import art.ameliah.laby.addons.cubepanion.core.commands.AppealSiteCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.EggWarsMapInfoCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.FindChestCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.LeaderboardAPICommands;
import art.ameliah.laby.addons.cubepanion.core.commands.LeaderboardMappings;
import art.ameliah.laby.addons.cubepanion.core.commands.PartyCommands;
import art.ameliah.laby.addons.cubepanion.core.commands.StatCommands;
import art.ameliah.laby.addons.cubepanion.core.commands.debug.Debug;
import art.ameliah.laby.addons.cubepanion.core.config.CubepanionConfig;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.generated.DefaultReferenceStorage;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags.RankTag;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags.RespawnTags;
import art.ameliah.laby.addons.cubepanion.core.listener.GameShutdownEventListener;
import art.ameliah.laby.addons.cubepanion.core.listener.GameTickEventListener;
import art.ameliah.laby.addons.cubepanion.core.listener.KeyEventListener;
import art.ameliah.laby.addons.cubepanion.core.listener.ScreenListener;
import art.ameliah.laby.addons.cubepanion.core.listener.games.GameListeners;
import art.ameliah.laby.addons.cubepanion.core.listener.hud.HudEvents;
import art.ameliah.laby.addons.cubepanion.core.listener.internal.InternalTrackers;
import art.ameliah.laby.addons.cubepanion.core.listener.misc.MiscListeners;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.managers.WidgetManager;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.ChestFinderLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.CodecLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.LeaderboardTrackerLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.QOLMapSelectorLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import art.ameliah.laby.addons.cubepanion.core.weave.ChestAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.EggWarsMapAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AddonMain
public class Cubepanion extends LabyAddon<CubepanionConfig> {

  private static Cubepanion instance;
  private CubepanionManager manager;
  private CubeSocket socket;

  private VotingLink votingLink;
  private ChestFinderLink chestFinderLink;
  private LeaderboardTrackerLink leaderboardTrackerLink;
  private QOLMapSelectorLink qolMapSelectorLink;
  private FunctionLink functionLink;
  private CodecLink codecLink;

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
      LOGGER.debug(getClass(),
          "Set LeaderboardAPIConfig#errorInfo & CubepanionConfig#showDebug true");
      this.configuration().getLeaderboardAPIConfig().getErrorInfo().set(true);
      this.configuration().getShowDebug().set(true);
    }

    ChestAPI.Init();
    EggWarsMapAPI.Init();
    LeaderboardAPI.Init();

    socket = new CubeSocket(
        this,
        labyAPI().minecraft().sessionAccessor(),
        labyAPI().eventBus(),
        labyAPI().notificationController());
    registerListener(socket);

    DefaultReferenceStorage storage = this.referenceStorageAccessor();
    votingLink = storage.getVotingLink();
    leaderboardTrackerLink = storage.getLeaderboardTrackerLink();
    qolMapSelectorLink = storage.getQOLMapSelectorLink();
    chestFinderLink = storage.getChestFinderLink();
    functionLink = storage.getFunctionLink();
    codecLink = storage.getCodecLink();
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
    if (codecLink == null) {
      LOGGER.warn(getClass(), "CodecLink is null. Some features will not work.");
    }

    this.manager = new CubepanionManager(this);
    LOGGER.setManager(this.manager);

    this.registerCommand(new PartyCommands("party", this));
    this.registerCommand(new PartyCommands("p", this));
    this.registerCommand(new AppealSiteCommand(this));
    this.registerCommand(new EggWarsMapInfoCommand(this));
    this.registerCommand(new StatCommands(this));
    this.registerCommand(new LeaderboardAPICommands(this));
    this.registerCommand(new FindChestCommand(this, chestFinderLink));
    this.registerCommand(new LeaderboardMappings(this));
    this.registerCommand(new Debug(this));

    this.registerListener(new GameTickEventListener(this));
    this.registerListener(new GameShutdownEventListener(this));
    this.registerListener(new KeyEventListener(this, qolMapSelectorLink));
    this.registerListener(new ScreenListener(this, leaderboardTrackerLink, qolMapSelectorLink));
    this.registerListener(new HudEvents(this));

    GameListeners.register(this);
    MiscListeners.register(this);
    InternalTrackers.register(this, functionLink);

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

  @Nullable
  public CodecLink getCodecLink() {
    return codecLink;
  }

  @NotNull
  public CubeSocket getSocket() {
    return socket;
  }

  public void registerCubepanionListener(Object listener) {
    this.registerListener(listener);
  }

  @Override
  protected Class<CubepanionConfig> configurationClass() {
    return CubepanionConfig.class;
  }
}
