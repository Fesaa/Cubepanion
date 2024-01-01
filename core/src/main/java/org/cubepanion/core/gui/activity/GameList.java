package org.cubepanion.core.gui.activity;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.util.logging.Logging;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.gui.activity.widgets.GameStats;
import org.cubepanion.core.gui.activity.widgets.GameWidget;
import org.cubepanion.core.utils.CubeGame;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

@AutoActivity
@Link("game-list.lss")
@Link("game-stats.lss")
public class GameList extends SimpleActivity {

  private static final Logging log = Logging.create(GameList.class);

  @Nullable
  private final CubeGame cur;

  private final HashMap<CubeGame, GameStatsTracker> gameStatsTrackers;

  private final VerticalListWidget<GameWidget> list = new VerticalListWidget<>();

  public GameList(@Nullable CubeGame cur, HashMap<CubeGame, GameStatsTracker> trackers) {
    this.cur = cur;
    this.gameStatsTrackers = trackers;

    list.addId("game-list");
    list.setDoubleClickCallback((w) -> {
      log.info("Double click on game widget " + w.getGame().getString());
      GameStats s = new GameStats(w.getGame(), gameStatsTrackers.get(w.getGame()));
      // Figure out how to properly do this. This opens on top of everything I think?
      Laby.labyAPI().screenOverlayHandler().displayInOverlay(s);
    });
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget();
    container.addId("container");

    for (CubeGame game : gameStatsTrackers.keySet()) {
      list.addChild(new GameWidget(game, game == cur));
    }
    container.addFlexibleContent(new ScrollWidget(list));

    document().addChild(container);
  }



}
