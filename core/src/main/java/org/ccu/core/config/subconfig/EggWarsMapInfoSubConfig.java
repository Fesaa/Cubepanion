package org.ccu.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class EggWarsMapInfoSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> mapLayout = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> buildLimit = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> logInParty = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> isEnabled() {return this.enabled;}
  public ConfigProperty<Boolean> getMapLayout() {return this.mapLayout;}
  public ConfigProperty<Boolean> getBuildLimit() {return this.buildLimit;}
  public ConfigProperty<Boolean> getLogInParty() {return this.logInParty;}

}
