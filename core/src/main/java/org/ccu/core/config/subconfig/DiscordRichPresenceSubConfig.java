package org.ccu.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class DiscordRichPresenceSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(y = 2, x = 1)
  private final ConfigProperty<Boolean> map = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(y = 2)
  private final ConfigProperty<Boolean> players = new ConfigProperty<>(true);



  public boolean isEnabled() {return this.enabled.get();}

  public ConfigProperty<Boolean> map() {return this.map;}

  public ConfigProperty<Boolean> players() {return this.players;}


}
