package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base;

import net.labymod.api.client.gui.hud.hudwidget.background.BackgroundHudWidget;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class ItemDisplayConfig extends BackgroundHudWidget.BackgroundHudWidgetConfig {

  @KeyBindSetting
  private final ConfigProperty<Key> key = new ConfigProperty<>(Key.NONE);

  @SliderSetting(min = 0.0F, max = 20.0F)
  private final ConfigProperty<Float> segmentSpacing = new ConfigProperty<>(2.0F);
  @SliderSetting(min = 0.0F, max = 20.0F)
  private final ConfigProperty<Float> spacingHeight = new ConfigProperty<>(2.0F);

  public ConfigProperty<Float> segmentSpacing() {
    return this.segmentSpacing;
  }

  public ConfigProperty<Float> spacingHeight() {
    return this.spacingHeight;
  }

  public ConfigProperty<Key> getKey() {
    return key;
  }
}
