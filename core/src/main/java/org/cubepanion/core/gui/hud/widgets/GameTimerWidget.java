package org.cubepanion.core.gui.hud.widgets;

import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.Utils;

public class GameTimerWidget extends TextHudWidget<GameTimerConfig> {

  private final int posX;
  private final int posY;
  private final CubepanionManager manager;

  private TextLine HUDLine;

  public GameTimerWidget(HudWidgetCategory category, String id, int postX, int posY) {
    super(id, GameTimerConfig.class);

    this.posX = postX;
    this.posY = posY;
    this.manager = Cubepanion.get().getManager();

    this.bindCategory(category);
  }

  public void onTick(boolean inEditor) {
    if (inEditor) {
      this.HUDLine.updateAndFlush(
          Utils.getFormattedString((45 + 32 * 60 + 60 * 60) * 1000, this.config.layout.get()));
      return;
    }
    long timeDifference = System.currentTimeMillis() - this.manager.getGameStartTime();
    this.HUDLine.updateAndFlush(Utils.getFormattedString(timeDifference, this.config.layout.get()));
    this.HUDLine.setState(
        !this.manager.isInPreLobby() && this.manager.onCubeCraft() ? State.VISIBLE : State.HIDDEN);
  }

  public void load(GameTimerConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("cubepanion", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, this.posX, this.posY);
    this.setIcon(icon);
    this.HUDLine = super.createLine("Game Timer", "5 hours 4 minutes 1 second");
  }

  public static class GameTimerConfig extends TextHudWidgetConfig {

    @DropdownSetting
    private final ConfigProperty<layoutEnum> layout = new ConfigProperty<>(layoutEnum.WORDS);

    public enum layoutEnum {
      WORDS, COLON, SECONDS, WORDS_SHORT
    }

  }

}
