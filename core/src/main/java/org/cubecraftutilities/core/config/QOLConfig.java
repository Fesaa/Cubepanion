package org.cubecraftutilities.core.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("setting_icons.png")
public class QOLConfig extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> shortFriendsList = new ConfigProperty<>(false);

  @SpriteSlot(x = 3, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> respawnTimer = new ConfigProperty<>(false);

  public ConfigProperty<Boolean> getRespawnTimer() {return this.respawnTimer;}
  public ConfigProperty<Boolean> getShortFriendsList() {return shortFriendsList;}
}
