package org.cubepanion.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("setting_icons.png")
public class ArmourBreakSoundsSubConfig extends Config {

  @TextFieldSetting
  private final ConfigProperty<String> soundIdHelmet = new ConfigProperty<>(
      "entity.lightning_bolt.impact");
  @TextFieldSetting
  private final ConfigProperty<String> soundIdChestplate = new ConfigProperty<>(
      "entity.lightning_bolt.impact");
  @TextFieldSetting
  private final ConfigProperty<String> soundIdLeggings = new ConfigProperty<>(
      "entity.lightning_bolt.impact");
  @TextFieldSetting
  private final ConfigProperty<String> soundIdBoots = new ConfigProperty<>(
      "entity.lightning_bolt.impact");

  public ResourceLocation getSoundIdBoots() {
    return ResourceLocation.create("minecraft", this.soundIdHelmet.get());
  }

  public ResourceLocation getSoundIdChestplate() {
    return  ResourceLocation.create("minecraft",this.soundIdChestplate.get());
  }

  public ResourceLocation getSoundIdHelmet() {
    return ResourceLocation.create("minecraft", this.soundIdLeggings.get());
  }

  public ResourceLocation getSoundIdLeggings() {
    return  ResourceLocation.create("minecraft", this.soundIdBoots.get());
  }
}
