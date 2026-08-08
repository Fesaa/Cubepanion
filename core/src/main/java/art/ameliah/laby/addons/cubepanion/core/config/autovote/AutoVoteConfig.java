package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class AutoVoteConfig extends Config {

  @SwitchSetting
  @ShowSettingInParent
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);

  @SliderSetting(min = 0, max = 1000, steps = 10f)
  private final ConfigProperty<Integer> delay = new ConfigProperty<>(100);

  public boolean isEnabled() {
    return this.enabled.get();
  }

  public ConfigProperty<Integer> getDelay() {
    return delay;
  }

  private final Map<String, Integer> selectedAutoVoteSlots = new HashMap<>();

  @NotNull
  public Map<String, Integer> getSlots() {
    return selectedAutoVoteSlots;
  }

}
