package org.cubecraftutilities.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class EndGameSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @SwitchSetting
  @SpriteSlot(x = 4, y = 2)
  private final ConfigProperty<Boolean> onElimination = new ConfigProperty<>(false);
  @DropdownSetting
  @SpriteSlot(x = 2, y = 2)
  private final ConfigProperty<gameEndMessages> gameEndMessage = new ConfigProperty<>(gameEndMessages.GG);
  @TextFieldSetting
  @SpriteSlot(x = 3, y = 2)
  private final ConfigProperty<String> customMessage = new ConfigProperty<>("");


  public ConfigProperty<Boolean> isEnabled() {return this.enabled;}

  public ConfigProperty<Boolean> getOnElimination() {return this.onElimination;}

  public ConfigProperty<gameEndMessages> getGameEndMessage() {return this.gameEndMessage;}

  public ConfigProperty<String> getCustomMessage() {return this.customMessage;}

  public enum gameEndMessages {
    GG("gg"),
    WP("wp"),
    GOOD_GAME("Good game"),
    WELL_PLAYED("Well played")
    ;
    public final String msg;
    gameEndMessages(String s) {
      this.msg = s;
    }
  }
}

