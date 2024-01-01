package org.cubepanion.core.gui.activity.widgets;

import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.utils.CubeGame;

@AutoActivity
@Link("game-stats.lss")
public class GameStats extends SimpleWidget {

  private final CubeGame game;
  private final GameStatsTracker tracker;

  private final VerticalListWidget<StatWidget> list = new VerticalListWidget<>();

  public GameStats(CubeGame game, GameStatsTracker t) {
    this.game = game;
    if (t == null) {
      throw new IllegalArgumentException("GameStatsTracker cannot be null");
    }
    this.tracker = t;
  }


  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget();
    container.addId("container");
    container.addFlexibleContent(new ScrollWidget(list));

    addChild(container);
  }

}
