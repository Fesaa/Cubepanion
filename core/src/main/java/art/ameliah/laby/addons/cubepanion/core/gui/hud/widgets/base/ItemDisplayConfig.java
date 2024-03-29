package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base;

import net.labymod.api.client.gui.Orientation;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class ItemDisplayConfig extends HudWidgetConfig {

  @KeyBindSetting
  private final ConfigProperty<Key> key = new ConfigProperty<>(Key.NONE);

  @DropdownSetting
  private final ConfigProperty<Orientation> orientation = new ConfigProperty<>(
      Orientation.HORIZONTAL);

  @SwitchSetting
  private final ConfigProperty<Boolean> showName = new ConfigProperty<>(true);

  @SliderSetting(min = 0.0F, max = 20.0F)
  private final ConfigProperty<Float> segmentSpacing = new ConfigProperty<>(2.0F);

  public ConfigProperty<Float> segmentSpacing() {
    return this.segmentSpacing;
  }

  public ConfigProperty<Key> getKey() {
    return key;
  }

  public ConfigProperty<Orientation> getOrientation() {
    return orientation;
  }

  public ConfigProperty<Boolean> getShowName() {
    return showName;
  }
}
