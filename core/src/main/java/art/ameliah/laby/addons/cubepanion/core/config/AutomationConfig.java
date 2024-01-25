package art.ameliah.laby.addons.cubepanion.core.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.ArmourBreakWarningSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.EndGameSubConfig;

@SpriteTexture("setting_icons.png")
public class AutomationConfig extends Config {

  @SpriteSlot(x = 2)
  private final EndGameSubConfig endGameSubConfig = new EndGameSubConfig();
  @SpriteSlot(x = 6)
  @SwitchSetting
  private final ConfigProperty<Boolean> friendMessageSound = new ConfigProperty<>(true);
  @SpriteSlot(x = 6)
  @TextFieldSetting
  @SettingRequires(value = "friendMessageSound")
  private final ConfigProperty<String> friendMessageSoundId = new ConfigProperty<>(
      "entity.experience_orb.pickup");
  @SpriteSlot(x = 7, y = 2)
  private final ArmourBreakWarningSubConfig armourBreakWarningSubConfig = new ArmourBreakWarningSubConfig();

  public ConfigProperty<Boolean> friendMessageSound() {
    return this.friendMessageSound;
  }

  public ResourceLocation getFriendMessageSoundId() {
    return ResourceLocation.create("minecraft", this.friendMessageSoundId.get());
  }

  public EndGameSubConfig getEndGameSubConfig() {
    return this.endGameSubConfig;
  }

  public ArmourBreakWarningSubConfig getArmourBreakWarningSubConfig() {
    return armourBreakWarningSubConfig;
  }
}
