package art.ameliah.laby.addons.cubepanion.core.config.subconfig;


import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

public class CommandSystemSubConfig extends Config {

  @SwitchSetting
  @ShowSettingInParent
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  @SettingRequires(value = "enabled")
  private final ConfigProperty<Boolean> appealSiteCommand = new ConfigProperty<>(true);

  @SwitchSetting
  @SettingRequires(value = "enabled")
  private final ConfigProperty<Boolean> gameMapInfoCommand = new ConfigProperty<>(true);

  @SwitchSetting
  @SettingRequires(value = "enabled")
  private final ConfigProperty<Boolean> PartyCommands = new ConfigProperty<>(true);

  @SwitchSetting
  @SettingRequires(value = "enabled")
  private final ConfigProperty<Boolean> StatsCommand = new ConfigProperty<>(true);

  @SwitchSetting
  @SettingRequires(value = "enabled")
  private final ConfigProperty<Boolean> chestFinderCommand = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> getAppealSiteCommand() {
    return appealSiteCommand;
  }

  public ConfigProperty<Boolean> getPartyCommands() {
    return PartyCommands;
  }

  public ConfigProperty<Boolean> getStatsCommand() {
    return StatsCommand;
  }

  public ConfigProperty<Boolean> getGameMapInfoCommand() {
    return gameMapInfoCommand;
  }

  public ConfigProperty<Boolean> getChestFinderCommand() {
    return chestFinderCommand;
  }

  public ConfigProperty<Boolean> getEnabled() {
    return enabled;
  }
}
