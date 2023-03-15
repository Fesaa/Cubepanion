package org.cubecraftutilities.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.cubecraftutilities.core.config.subconfig.CommandSystemSubConfig;
import org.cubecraftutilities.core.config.subconfig.DiscordRichPresenceSubConfig;
import org.cubecraftutilities.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.cubecraftutilities.core.config.subconfig.StatsTrackerSubConfig;

@ConfigName("settings")
@SpriteTexture("setting_icons.png")
public class CCUconfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  private final AutomationConfig automationConfig = new AutomationConfig();

  private final QOLConfig qolConfig = new QOLConfig();

  @SpriteSlot()
  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig = new EggWarsMapInfoSubConfig();

  @SpriteSlot(x = 1)
  private final DiscordRichPresenceSubConfig discordRichPresenceSubConfig = new DiscordRichPresenceSubConfig();

  @SpriteSlot(x = 3)
  private final StatsTrackerSubConfig statsTrackerSubConfig = new StatsTrackerSubConfig();

  private final CommandSystemSubConfig commandSystemSubConfig = new CommandSystemSubConfig();

  @Override
  public ConfigProperty<Boolean> enabled() {return this.enabled;}

  public AutomationConfig getAutomationConfig() {return automationConfig;}
  public QOLConfig getQolConfig() {return qolConfig;}
  public CommandSystemSubConfig getCommandSystemSubConfig() {return commandSystemSubConfig;}
  public EggWarsMapInfoSubConfig getEggWarsMapInfoSubConfig() {return this.eggWarsMapInfoSubConfig;}
  public DiscordRichPresenceSubConfig getDiscordRichPresenceSubConfig() {return this.discordRichPresenceSubConfig;}
  public StatsTrackerSubConfig getStatsTrackerSubConfig() {return this.statsTrackerSubConfig;}

}
