package org.ccu.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.ccu.core.config.subconfig.AutoVoteSubConfig;
import org.ccu.core.config.subconfig.CommandSystemSubConfig;
import org.ccu.core.config.subconfig.DiscordRichPresenceSubConfig;
import org.ccu.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.ccu.core.config.subconfig.EndGameSubConfig;
import org.ccu.core.config.subconfig.StatsTrackerSubConfig;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
@SpriteTexture("setting_icons.png")
public class CCUconfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(x = 6)
  private final ConfigProperty<Boolean> friendMessageSound = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(x = 3, y = 1)
  private final ConfigProperty<Boolean> respawnTimer = new ConfigProperty<>(true);

  @SliderSetting(min = 0, max = 20)
  private final ConfigProperty<Integer> durabilityWarning = new ConfigProperty<>(10);

  @SpriteSlot(x = 2)
  private final EndGameSubConfig endGameSubConfig = new EndGameSubConfig();

  private final CommandSystemSubConfig commandSystemSubConfig = new CommandSystemSubConfig();

  @SpriteSlot(x = 0)
  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig = new EggWarsMapInfoSubConfig();

  @SpriteSlot(x = 1)
  private final DiscordRichPresenceSubConfig discordRichPresenceSubConfig = new DiscordRichPresenceSubConfig();

  @SpriteSlot(x = 4)
  private final AutoVoteSubConfig autoVoteSubConfig = new AutoVoteSubConfig();

  @SpriteSlot(x = 3)
  private final StatsTrackerSubConfig statsTrackerSubConfig = new StatsTrackerSubConfig();

  @SwitchSetting
  @SpriteSlot(x = 5)
  private final ConfigProperty<Boolean> whereAmI = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {return this.enabled;}
  public ConfigProperty<Boolean> getRespawnTimer() {return this.respawnTimer;}
  public ConfigProperty<Integer> getDurabilityWarning() {return durabilityWarning;}
  public CommandSystemSubConfig getCommandSystemSubConfig() {return commandSystemSubConfig;}
  public EndGameSubConfig getEndGameSubConfig() {return this.endGameSubConfig;}
  public EggWarsMapInfoSubConfig getEggWarsMapInfoSubConfig() {return this.eggWarsMapInfoSubConfig;}
  public DiscordRichPresenceSubConfig getDiscordRichPresenceSubConfig() {return this.discordRichPresenceSubConfig;}
  public AutoVoteSubConfig getAutoVoteSubConfig() {return this.autoVoteSubConfig;}
  public StatsTrackerSubConfig getStatsTrackerSubConfig() {return this.statsTrackerSubConfig;}
  public ConfigProperty<Boolean> displayWhereAmI() {return this.whereAmI;}
  public ConfigProperty<Boolean> friendMessageSound() {return this.friendMessageSound;}

}
