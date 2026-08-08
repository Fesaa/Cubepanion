package art.ameliah.laby.addons.cubepanion.core.config;

import art.ameliah.laby.addons.cubepanion.core.config.autovote.AutoVoteConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.DiscordRichPresenceSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.GameMapInfoSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.jetbrains.annotations.NotNull;

@ConfigName("settings")
@SpriteTexture("setting_icons.png")
public class CubepanionConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  private final AutoVoteConfig autoVoteConfig = new AutoVoteConfig();
  private final AutomationConfig automationConfig = new AutomationConfig();
  private final QOLConfig qolConfig = new QOLConfig();
  @SpriteSlot()
  private final GameMapInfoSubConfig gameMapInfoSubConfig = new GameMapInfoSubConfig();
  @SpriteSlot(x = 1)
  private final DiscordRichPresenceSubConfig discordRichPresenceSubConfig = new DiscordRichPresenceSubConfig();
  @SpriteSlot(x = 3)
  private final StatsTrackerSubConfig statsTrackerSubConfig = new StatsTrackerSubConfig();
  private final CommandSystemSubConfig commandSystemSubConfig = new CommandSystemSubConfig();
  private final LeaderboardAPIConfig leaderboardAPIConfig = new LeaderboardAPIConfig();

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  @NotNull
  public AutoVoteConfig getAutoVoteConfig() {
    return autoVoteConfig;
  }

  public AutomationConfig getAutomationConfig() {
    return automationConfig;
  }

  public QOLConfig getQolConfig() {
    return qolConfig;
  }

  public CommandSystemSubConfig getCommandSystemSubConfig() {
    return commandSystemSubConfig;
  }

  public GameMapInfoSubConfig getGameMapInfoSubConfig() {
    return this.gameMapInfoSubConfig;
  }

  public DiscordRichPresenceSubConfig getDiscordRichPresenceSubConfig() {
    return this.discordRichPresenceSubConfig;
  }

  public StatsTrackerSubConfig getStatsTrackerSubConfig() {
    return this.statsTrackerSubConfig;
  }

  public LeaderboardAPIConfig getLeaderboardAPIConfig() {
    return leaderboardAPIConfig;
  }

}
