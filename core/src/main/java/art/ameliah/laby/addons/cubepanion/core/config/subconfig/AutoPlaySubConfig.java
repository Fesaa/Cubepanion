package art.ameliah.laby.addons.cubepanion.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class AutoPlaySubConfig extends Config {

  @SwitchSetting
  @ShowSettingInParent
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);

  @SliderSetting(min = 0, max = 1000, steps = 50f)
  private final ConfigProperty<Integer> delay = new ConfigProperty<>(100);

  @Override
  public int getConfigVersion() {
    return 2;
  }

  public boolean isEnabled() {
    return this.enabled.get();
  }

  public ConfigProperty<Integer> getDelay() {
    return delay;
  }

}