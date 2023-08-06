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
  private ResourceLocation helmetResourceLocation;
  private ResourceLocation chestplateResourceLocation;
  private ResourceLocation leggingsResourceLocation;
  private ResourceLocation bootsResourceLocation;

  public ArmourBreakSoundsSubConfig() {
    this.helmetResourceLocation = ResourceLocation.create("minecraft", this.soundIdHelmet.get());
    this.chestplateResourceLocation = ResourceLocation.create("minecraft",
        this.soundIdChestplate.get());
    this.leggingsResourceLocation = ResourceLocation.create("minecraft",
        this.soundIdLeggings.get());
    this.bootsResourceLocation = ResourceLocation.create("minecraft", this.soundIdBoots.get());

    this.soundIdHelmet.addChangeListener(
        (type, oldValue, newValue) -> this.helmetResourceLocation = ResourceLocation.create(
            "minecraft", newValue));
    this.soundIdChestplate.addChangeListener(
        (type, oldValue, newValue) -> this.chestplateResourceLocation = ResourceLocation.create(
            "minecraft", newValue));
    this.soundIdLeggings.addChangeListener(
        (type, oldValue, newValue) -> this.leggingsResourceLocation = ResourceLocation.create(
            "minecraft", newValue));
    this.soundIdBoots.addChangeListener(
        (type, oldValue, newValue) -> this.bootsResourceLocation = ResourceLocation.create(
            "minecraft", newValue));
  }

  public ResourceLocation getSoundIdBoots() {
    return helmetResourceLocation;
  }

  public ResourceLocation getSoundIdChestplate() {
    return chestplateResourceLocation;
  }

  public ResourceLocation getSoundIdHelmet() {
    return leggingsResourceLocation;
  }

  public ResourceLocation getSoundIdLeggings() {
    return bootsResourceLocation;
  }
}
