package org.cubepanion.core;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.cubepanion.core.commands.AppealSiteCommand;
import org.cubepanion.core.commands.DivisionCommand;
import org.cubepanion.core.commands.EggWarsMapInfoCommand;
import org.cubepanion.core.commands.FriendListCommand;
import org.cubepanion.core.commands.FriendLocationCommand;
import org.cubepanion.core.commands.LeaderboardAPICommands;
import org.cubepanion.core.commands.MapCommand;
import org.cubepanion.core.commands.OnlineFriendTrackerCommand;
import org.cubepanion.core.commands.PartyCommands;
import org.cubepanion.core.commands.StatCommands;
import org.cubepanion.core.commands.TeamColourCommand;
import org.cubepanion.core.config.Cubepanionconfig;
import org.cubepanion.core.generated.DefaultReferenceStorage;
import org.cubepanion.core.gui.hud.nametags.RespawnTags;
import org.cubepanion.core.listener.GameShutdownEventListener;
import org.cubepanion.core.listener.GameTickEventListener;
import org.cubepanion.core.listener.KeyEventListener;
import org.cubepanion.core.listener.ScreenListener;
import org.cubepanion.core.listener.chat.Automations;
import org.cubepanion.core.listener.chat.PartyTracker;
import org.cubepanion.core.listener.chat.StatsTracker;
import org.cubepanion.core.listener.network.PlayerInfo;
import org.cubepanion.core.listener.network.ScoreboardListener;
import org.cubepanion.core.listener.network.ServerNavigation;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.DiscordRPCManager;
import org.cubepanion.core.managers.WidgetManager;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.versionlinkers.LeaderboardTrackerLink;
import org.cubepanion.core.versionlinkers.QOLMapSelectorLink;
import org.cubepanion.core.versionlinkers.VotingLink;

@AddonMain
public class Cubepanion extends LabyAddon<Cubepanionconfig> {
  public DiscordRPCManager rpcManager;
  public WidgetManager widgetManager;
  private CubepanionManager manager;

  private static Cubepanion instance;

  public Cubepanion() {
    instance = this;
  }

  public static Cubepanion get() {
    return instance;
  }

  public final static String leaderboardAPI = "http://ameliah.art:7070/";

  public static void updateRPC() {
    if (instance != null) {
      instance.rpcManager.updateRPC();
    }
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();

    DefaultReferenceStorage storage = this.referenceStorageAccessor();
    VotingLink votingLink = storage.getVotingLink();
    LeaderboardTrackerLink leaderboardTrackerLink = storage.getLeaderboardTrackerLink();
    QOLMapSelectorLink qolMapSelectorLink = storage.getQOLMapSelectorLink();
    if (votingLink == null) {
      this.logger().warn("VotingLink is null. Some features will not work.");
    }
    if (leaderboardTrackerLink == null) {
      this.logger().warn("LeaderboardTrackerLink is null. Some features will not work.");
    }
    if (qolMapSelectorLink == null) {
      this.logger().warn("QOLMapSelectorLink is null. Some features will not work.");
    }

    this.manager = new CubepanionManager(this);

    this.rpcManager = new DiscordRPCManager(this);
    this.widgetManager = new WidgetManager(this);

    this.registerCommand(new PartyCommands(this));
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

    this.registerListener(new PlayerInfo(this));
    this.registerListener(new ServerNavigation(this));
    this.registerListener(new GameTickEventListener(this));
    this.registerListener(new GameShutdownEventListener(this));
    this.registerListener(new KeyEventListener(this, qolMapSelectorLink));
    this.registerListener(new Automations(this, votingLink));
    this.registerListener(new PartyTracker(this));
    this.registerListener(new StatsTracker(this));
    this.registerListener(new ScoreboardListener(this));
    this.registerListener(new ScreenListener(this, leaderboardTrackerLink, qolMapSelectorLink));

    this.labyAPI().tagRegistry().register("respawn_timer", PositionType.ABOVE_NAME, new RespawnTags(this));

    this.widgetManager.register();

    this.logger().info("Cubepanion has successfully registered all her components.");
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
  protected Class<Cubepanionconfig> configurationClass() {
    return Cubepanionconfig.class;
  }
}
