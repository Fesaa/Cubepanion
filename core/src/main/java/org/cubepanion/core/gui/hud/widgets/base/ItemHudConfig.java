package org.cubepanion.core.gui.hud.widgets.base;

import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.Color;

public class ItemHudConfig extends HudWidgetConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> onlyDisplayWhenHeld = new ConfigProperty<>(false);

  @ColorPickerSetting
  private final ConfigProperty<Color> textColour = new ConfigProperty<>(Color.WHITE);

  public ConfigProperty<Boolean> getOnlyDisplayWhenHeld() {
    return onlyDisplayWhenHeld;
  }

  public ConfigProperty<Color> getTextColour() {
    return textColour;
  }

  public TextColor getTextColor() {
    Color colour = textColour.get();
    return TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue());
  }
}
