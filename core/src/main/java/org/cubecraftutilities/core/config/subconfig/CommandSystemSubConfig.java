package org.cubecraftutilities.core.config.subconfig;


import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SuppressWarnings("FieldMayBeFinal")
public class CommandSystemSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> appealSiteCommand = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> EggWarsMapInfoCommand = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> PartyCommands = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> StatsCommand = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> getAppealSiteCommand() {
    return appealSiteCommand;
  }

  public ConfigProperty<Boolean> getPartyCommands() {
    return PartyCommands;
  }

  public ConfigProperty<Boolean> getStatsCommand() {
    return StatsCommand;
  }

  public ConfigProperty<Boolean> getEggWarsMapInfoCommand() {
    return EggWarsMapInfoCommand;
  }

  public ConfigProperty<Boolean> getEnabled() {
    return enabled;
  }
}
