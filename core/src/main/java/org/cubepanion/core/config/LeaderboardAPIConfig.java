package org.cubepanion.core.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class LeaderboardAPIConfig extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> contributeToDB = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> userCommands = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> partyCommands = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> errorInfo = new ConfigProperty<>(false);

  public ConfigProperty<Boolean> getContributeToDB() {
    return contributeToDB;
  }

  public ConfigProperty<Boolean> getPartyCommands() {
    return partyCommands;
  }

  public ConfigProperty<Boolean> getUserCommands() {
    return userCommands;
  }

  public ConfigProperty<Boolean> getErrorInfo() {
    return errorInfo;
  }
}
