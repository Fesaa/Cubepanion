package org.cubepanion.core.gui.activity.widgets;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import org.cubepanion.core.utils.CubeGame;

@AutoWidget
public class GameWidget extends SimpleWidget {

  private final CubeGame game;
  private final boolean selected;

  public GameWidget(CubeGame game, boolean selected) {
    this.game = game;
    this.selected = selected;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if (selected) {
      addId("selected");
    }

    IconWidget icon = new IconWidget(Icon.url(game.getUrl()));
    icon.addId("game-icon");
    addChild(icon);

    ComponentWidget w = ComponentWidget.text(game.getString());
    w.addId("game-name");
    addChild(w);
  }

  public CubeGame getGame() {
    return game;
  }
}
