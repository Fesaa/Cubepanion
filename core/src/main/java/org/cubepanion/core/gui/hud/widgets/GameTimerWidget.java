package org.cubepanion.core.gui.hud.widgets;

import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig;
import org.cubepanion.core.managers.CCUManager;

public class GameTimerWidget extends TextHudWidget<GameTimerConfig> {

  private final int posX;
  private final int posY;
  private final CCUManager manager;

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
      this.HUDLine.updateAndFlush(this.config.getFormattedString((45 + 32*60 + 60*60) * 1000));
      return;
    }
    long timeDifference = System.currentTimeMillis() -  this.manager.getGameStartTime();
    this.HUDLine.updateAndFlush(this.config.getFormattedString(timeDifference));
    this.HUDLine.setState(!this.manager.isInPreLobby() && this.manager.onCubeCraft() ? State.VISIBLE : State.HIDDEN);
  }

  public void load(GameTimerConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("cubepanion", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, this.posX, this.posY);
    this.setIcon(icon);
    this.HUDLine = super.createLine("Game Timer", "");
  }

  public static class GameTimerConfig extends TextHudWidgetConfig {

    @TextFieldSetting
    private final ConfigProperty<String> layoutString = new ConfigProperty<>("%h %H %m %M %s %S");

    private String getFormattedString(long timeDifference) {
      int seconds = (int) (timeDifference / 1000L);
      int minutes = Math.floorDiv(seconds, 60);
      seconds = seconds - minutes * 60;
      int hours = Math.floorDiv(minutes, 60);
      minutes = minutes - hours * 60;

      String s = String.format("%s", seconds);
      String m = String.format("%s", minutes);
      String h = String.format("%s", hours);
      String readableString = this.layoutString.get();

      if (hours > 0) {
        readableString = readableString.replace("%h", h);
        readableString = readableString.replace("%hh", h);
        readableString = readableString.replace("%H", "hour" + (hours != 1? "s": ""));
      } else if (readableString.contains("%hh")) {
        readableString = readableString.replace("%hh", "00");
        readableString = readableString.replace("%H", "hours");
      } else {
        readableString = readableString.replace("%H", "");
        readableString = readableString.replace("%h", "");
        readableString = readableString.replace("%hh", "");
      }

      if (minutes > 0) {
        readableString = readableString.replace("%m", m);
        readableString = readableString.replace("%mm", m);
        readableString = readableString.replace("%M", "minute" + (minutes != 1? "s": ""));
      } else if (readableString.contains("%ss")) {
        readableString = readableString.replace("%mm", "00");
        readableString = readableString.replace("%M", "minutes");
      } else {
        readableString = readableString.replace("%M", "");
        readableString = readableString.replace("%m", "");
        readableString = readableString.replace("%mm", "");
      }

      if (seconds > 0) {
        readableString = readableString.replace("%s", s);
        readableString = readableString.replace("%ss", h);
        readableString = readableString.replace("%S", "second" + (seconds != 1? "s": ""));
      } else if (readableString.contains("%ss")) {
        readableString = readableString.replace("%ss", "00");
        readableString = readableString.replace("%S", "seconds");
      } else {
        readableString = readableString.replace("%S", "");
        readableString = readableString.replace("%s", "");
        readableString = readableString.replace("%ss", "");
      }

      return readableString.trim();
    }

  }

}
