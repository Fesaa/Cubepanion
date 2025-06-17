package art.ameliah.laby.addons.cubepanion.core;


import art.ameliah.laby.addons.cubepanion.core.commands.AppealSiteCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.FindChestCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.GameMapInfoCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.LeaderboardCommand;
import art.ameliah.laby.addons.cubepanion.core.commands.debug.Debug;
import art.ameliah.laby.addons.cubepanion.core.config.CubepanionConfig;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.generated.DefaultReferenceStorage;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags.LevelTag;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags.RankTag;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags.RespawnTags;
import art.ameliah.laby.addons.cubepanion.core.listener.ConfigFixes;
import art.ameliah.laby.addons.cubepanion.core.listener.GameTickEventListener;
import art.ameliah.laby.addons.cubepanion.core.listener.KeyEventListener;
import art.ameliah.laby.addons.cubepanion.core.listener.games.GameListeners;
import art.ameliah.laby.addons.cubepanion.core.listener.hud.HudEvents;
import art.ameliah.laby.addons.cubepanion.core.listener.internal.InternalTrackers;
import art.ameliah.laby.addons.cubepanion.core.listener.misc.MiscListeners;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.managers.WidgetManager;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.ChestFinderLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.CodecLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AddonMain
public class Cubepanion extends LabyAddon<CubepanionConfig> {

  private static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private static Cubepanion instance;
  private CubepanionManager manager;
  private CubeSocket socket;

  private ChestFinderLink chestFinderLink;
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
    // Registering this first, just in case.
    this.registerCubepanionListener(new ConfigFixes());

    log.info("Starting Cubepanion");
    this.registerSettingCategory();

    CubepanionAPI.Init();
    AutoVoteProvider.init(this.configuration().getAutoVoteSubConfig());

    socket = new CubeSocket(
        this,
        labyAPI().minecraft().sessionAccessor(),
        labyAPI().eventBus(),
        labyAPI().notificationController());
    registerListener(socket);

    DefaultReferenceStorage storage = this.referenceStorageAccessor();
    chestFinderLink = storage.getChestFinderLink();
    functionLink = storage.getFunctionLink();
    codecLink = storage.getCodecLink();
    if (chestFinderLink == null) {
      log.warn("ChestFinderLink is null. Some features may not work.");
    }
    if (functionLink == null) {
      log.warn("FunctionLink is null. Some features may not work.");
    }
    if (codecLink == null) {
      log.warn("CodecLink is null. Some features may not work.");
    }

    this.manager = new CubepanionManager(this);

    this.registerCommand(new AppealSiteCommand(this));
    this.registerCommand(new GameMapInfoCommand(this));
    this.registerCommand(new LeaderboardCommand(this));
    this.registerCommand(new FindChestCommand(this, chestFinderLink));
    this.registerCommand(new Debug(this));

    this.registerListener(new GameTickEventListener(this));
    this.registerListener(new KeyEventListener(this));
    this.registerListener(new HudEvents(this));

    GameListeners.register(this);
    MiscListeners.register(this);
    InternalTrackers.register(this, functionLink);

    RespawnTags respawnTags = new RespawnTags(this);
    RankTag rankTag = new RankTag(this);
    LevelTag levelTag = new LevelTag(this);

    this.registerListener(respawnTags);
    this.labyAPI().tagRegistry().register("respawn_timer", PositionType.ABOVE_NAME, respawnTags);
    this.labyAPI().tagRegistry().register("rank_tag", PositionType.ABOVE_NAME, rankTag);
    this.labyAPI().tagRegistry().register("level_tag", PositionType.RIGHT_TO_NAME, levelTag);

    WidgetManager.register(this);

    log.info("Cubepanion has successfully registered all her components.");
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
  public ChestFinderLink getChestFinderLink() {
    return chestFinderLink;
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
