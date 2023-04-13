package org.cubepanion.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("setting_icons.png")
public class ArmourBreakSoundsSubConfig extends Config {

  @TextFieldSetting
  private final ConfigProperty<String> soundIdHelmet = new ConfigProperty<>("entity.lightning_bolt.impact");

  @TextFieldSetting
  private final ConfigProperty<String> soundIdChestplate = new ConfigProperty<>("entity.lightning_bolt.impact");

  @TextFieldSetting
  private final ConfigProperty<String> soundIdLeggings = new ConfigProperty<>("entity.lightning_bolt.impact");

  @TextFieldSetting
  private final ConfigProperty<String> soundIdBoots = new ConfigProperty<>("entity.lightning_bolt.impact");

  public ConfigProperty<String> getSoundIdBoots() {
    return soundIdBoots;
  }

  public ConfigProperty<String> getSoundIdChestplate() {
    return soundIdChestplate;
  }

  public ConfigProperty<String> getSoundIdHelmet() {
    return soundIdHelmet;
  }

  public ConfigProperty<String> getSoundIdLeggings() {
    return soundIdLeggings;
  }
}
