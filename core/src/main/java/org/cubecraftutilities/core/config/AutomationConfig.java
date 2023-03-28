package org.cubecraftutilities.core.config;

import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import org.cubecraftutilities.core.config.subconfig.EndGameSubConfig;

@SpriteTexture("setting_icons.png")
public class AutomationConfig extends Config {

  @SpriteSlot(x = 2)
  private final EndGameSubConfig endGameSubConfig = new EndGameSubConfig();

  @SpriteSlot(x = 6)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendMessageSound = new ConfigProperty<>(true);

  @SpriteSlot(x = 6)
  @TextFieldSetting
  private final ConfigProperty<String> friendMessageSoundId = new ConfigProperty<>("entity.experience_orb.pickup");

  @SpriteSlot(x = 7, y = 2)
  @SliderSetting(min = 0, max = 20)
  private final ConfigProperty<Integer> durabilityWarning = new ConfigProperty<>(10);

  @SpriteSlot(x = 5)
  @SwitchSetting
  private final ConfigProperty<Boolean> whereAmI = new ConfigProperty<>(false);

  @SpriteSlot(x = 5)
  @KeyBindSetting
  @SettingRequires("whereAmI")
  private final ConfigProperty<Key> copyServerID = new ConfigProperty<>(Key.NONE);

  @SpriteSlot(x = 5)
  @KeyBindSetting
  @SettingRequires("whereAmI")
  private final ConfigProperty<Key> copyBungeecord = new ConfigProperty<>(Key.NONE);

  public ConfigProperty<Boolean> displayWhereAmI() {return this.whereAmI;}
  public ConfigProperty<Key> getCopyBungeecord() {return copyBungeecord;}
  public ConfigProperty<Key> getCopyServerID() {return copyServerID;}
  public ConfigProperty<Boolean> friendMessageSound() {return this.friendMessageSound;}
  public ConfigProperty<String> getFriendMessageSoundId() {return friendMessageSoundId;}
  public ConfigProperty<Integer> getDurabilityWarning() {return durabilityWarning;}
  public EndGameSubConfig getEndGameSubConfig() {return this.endGameSubConfig;}



}
