package art.ameliah.laby.addons.cubepanion.core.config.subconfig;

import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class EggWarsMapInfoSubConfig extends Config {

  @SwitchSetting
  @ShowSettingInParent
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @KeyBindSetting
  private final ConfigProperty<Key> key = new ConfigProperty<>(Key.NONE);

  @SwitchSetting
  @SpriteSlot(y = 2, x = 1)
  private final ConfigProperty<Boolean> mapLayout = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(x = 6, y = 2)
  private final ConfigProperty<Boolean> buildLimit = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(y = 2, x = 1)
  private final ConfigProperty<Boolean> genLayout = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> isEnabled() {
    return this.enabled;
  }

  public ConfigProperty<Key> getKey() {
    return this.key;
  }

  public ConfigProperty<Boolean> getMapLayout() {
    return this.mapLayout;
  }

  public ConfigProperty<Boolean> getBuildLimit() {
    return this.buildLimit;
  }

  public ConfigProperty<Boolean> getGenLayout() {
    return genLayout;
  }

}
