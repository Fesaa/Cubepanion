package org.cubepanion.core.gui.activity.widgets;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import org.cubepanion.core.config.imp.StatsTracker;

@AutoWidget
@Link("stat-widget.lss")
public class StatWidget extends SimpleWidget {

  private final String stat;
  private final StatsTracker tracker;

  public StatWidget(String stat, StatsTracker tracker) {
    this.stat = stat;
    this.tracker = tracker;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget label = ComponentWidget.text(stat);
    label.addId("label");
    addChild(label);

    ComponentWidget max = ComponentWidget.i18n("cubepanion.gui.stat_widget.max",
        tracker.getAllTimeMax());
    max.addId("max");
    addChild(max);

    ComponentWidget allTime = ComponentWidget.i18n("cubepanion.gui.stat_widget.allTime",
        tracker.getAllTime());
    allTime.addId("allTime");
    addChild(allTime);

    ComponentWidget dailyMax = ComponentWidget.i18n("cubepanion.gui.stat_widget.daily",
        tracker.getDailyMax());
    dailyMax.addId("dailyMax");
    addChild(dailyMax);

    ComponentWidget daily = ComponentWidget.i18n("cubepanion.gui.stat_widget.daily",
        tracker.getDaily());
    daily.addId("daily");
    addChild(daily);

    ComponentWidget allTimeDailyMax = ComponentWidget.i18n("cubepanion.gui.stat_widget.weekly",
        tracker.getAllTimeDailyMax());
    allTimeDailyMax.addId("allTimeDailyMax");
    addChild(allTimeDailyMax);
  }


}
