package org.ccu.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.ccu.core.config.subconfig.DiscordRichPresenceSubConfig;
import org.ccu.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.ccu.core.config.subconfig.EndGameSubConfig;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class CCUconfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  private final EndGameSubConfig endGameSubConfig = new EndGameSubConfig();

  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig = new EggWarsMapInfoSubConfig();

  private final DiscordRichPresenceSubConfig discordRichPresenceSubConfig = new DiscordRichPresenceSubConfig();

  @SwitchSetting
  private final ConfigProperty<Boolean> friendMessageSound = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> whereAmI = new ConfigProperty<>(false);

  @Override
  public ConfigProperty<Boolean> enabled() {return this.enabled;}
  public EndGameSubConfig getEndGameSubConfig() {return this.endGameSubConfig;}
  public EggWarsMapInfoSubConfig getEggWarsMapInfoSubConfig() {return this.eggWarsMapInfoSubConfig;}
  public DiscordRichPresenceSubConfig getDiscordRichPresenceSubConfig() {return this.discordRichPresenceSubConfig;}
  public ConfigProperty<Boolean> displayWhereAmI() {return this.whereAmI;}
  public ConfigProperty<Boolean> friendMessageSound() {return this.friendMessageSound;}

}
