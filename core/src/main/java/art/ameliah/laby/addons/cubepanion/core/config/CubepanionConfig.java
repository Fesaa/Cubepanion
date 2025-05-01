package art.ameliah.laby.addons.cubepanion.core.config;

import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.DiscordRichPresenceSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.GameMapInfoSubConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
@SpriteTexture("setting_icons.png")
public class CubepanionConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  private final AutomationConfig automationConfig = new AutomationConfig();
  private final QOLConfig qolConfig = new QOLConfig();
  private final AutoVoteSubConfig autoVoteSubConfig = new AutoVoteSubConfig();
  @SpriteSlot()
  private final GameMapInfoSubConfig gameMapInfoSubConfig = new GameMapInfoSubConfig();
  @SpriteSlot(x = 1)
  private final DiscordRichPresenceSubConfig discordRichPresenceSubConfig = new DiscordRichPresenceSubConfig();
  private final CommandSystemSubConfig commandSystemSubConfig = new CommandSystemSubConfig();
  private final LeaderboardAPIConfig leaderboardAPIConfig = new LeaderboardAPIConfig();

  @SwitchSetting
  private final ConfigProperty<Boolean> showDebug = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public AutomationConfig getAutomationConfig() {
    return automationConfig;
  }

  public QOLConfig getQolConfig() {
    return qolConfig;
  }

  public AutoVoteSubConfig getAutoVoteSubConfig() {
    return autoVoteSubConfig;
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

  public LeaderboardAPIConfig getLeaderboardAPIConfig() {
    return leaderboardAPIConfig;
  }

  public ConfigProperty<Boolean> getShowDebug() {
    return showDebug;
  }
}
