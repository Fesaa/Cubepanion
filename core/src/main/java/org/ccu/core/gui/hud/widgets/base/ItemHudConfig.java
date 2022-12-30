package org.ccu.core.gui.hud.widgets.base;

import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class ItemHudConfig extends HudWidgetConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> onlyDisplayWhenHeld = new ConfigProperty<>(false);

  public ConfigProperty<Boolean> getOnlyDisplayWhenHeld() {
    return onlyDisplayWhenHeld;
  }
}
