package org.cubecraftutilities.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownEntryTranslationPrefix;
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
  @SpriteSlot(x = 3, y = 2)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.automationConfig.endGameSubConfig.gameEndMessage.entries")
  private final ConfigProperty<GameEndMessage> gameEndMessage = new ConfigProperty<>(GameEndMessage.GG);
  @TextFieldSetting
  @SpriteSlot(x = 3, y = 2)
  private final ConfigProperty<String> customMessage = new ConfigProperty<>("");


  public ConfigProperty<Boolean> isEnabled() {return this.enabled;}

  public ConfigProperty<Boolean> getOnElimination() {return this.onElimination;}

  public ConfigProperty<GameEndMessage> getGameEndMessage() {return this.gameEndMessage;}

  public ConfigProperty<String> getCustomMessage() {return this.customMessage;}

  public enum GameEndMessage {
    GG, WP, GOOD_GAME, WELL_PLAYED, NONE
  }
}

