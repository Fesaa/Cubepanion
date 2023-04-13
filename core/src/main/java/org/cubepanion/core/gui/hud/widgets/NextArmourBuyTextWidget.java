package org.cubepanion.core.gui.hud.widgets;

import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownEntryTranslationPrefix;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.cubepanion.core.gui.hud.widgets.NextArmourBuyTextWidget.NextArmourBuyHudConfig;
import org.cubepanion.core.gui.hud.widgets.NextArmourBuyTextWidget.NextArmourBuyHudConfig.whereToDisplay;
import org.cubepanion.core.managers.CCUManager;

public class NextArmourBuyTextWidget extends TextHudWidget<NextArmourBuyHudConfig> {

  private final CCUManager manager;
  private TextLine nextArmourBuy;

  public NextArmourBuyTextWidget(HudWidgetCategory category, String id, CCUManager manager) {
    super(id, NextArmourBuyHudConfig.class);
    this.manager = manager;

    this.bindCategory(category);
  }

  public void load(NextArmourBuyHudConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("cubepanion", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, 1, 1);
    this.setIcon(icon);
    this.nextArmourBuy = super.createLine("Next to run out", "");
  }

  public void onTick(boolean inEditor) {
    this.nextArmourBuy.updateAndFlush(this.manager.getDurabilityManager().nextToBreakWidgetString(inEditor, this.config.getShowDifference().get()));
    this.nextArmourBuy.setState(this.shouldBeVisible() || inEditor ? State.VISIBLE : State.HIDDEN);
  }

  @SuppressWarnings("RedundantIfStatement")
  private boolean shouldBeVisible() {
    whereToDisplay whereToDisplay = this.config.getWheretoDisplayType().get();
    if (whereToDisplay == null) {
      return false;
    }
    if (whereToDisplay.everywhere) {
      return true;
    }
    if (whereToDisplay.games && !this.manager.isInPreLobby()) {
      return true;
    }
    if (this.manager.getDivision().getString().equals(whereToDisplay.gameName) && !this.manager.isInPreLobby()) {
      return true;
    }
    return false;
  }

  public static class NextArmourBuyHudConfig extends TextHudWidgetConfig {

    @DropdownSetting
    @DropdownEntryTranslationPrefix("cubepanion.settings.hudWidget.nextArmourDurability.wheretoDisplayType.entries")
    private final ConfigProperty<whereToDisplay> wheretoDisplayType = new ConfigProperty<>(whereToDisplay.EGGWARS);

    @SwitchSetting
    private final ConfigProperty<Boolean> showDifference = new ConfigProperty<>(false);

    public ConfigProperty<whereToDisplay> getWheretoDisplayType() {
      return wheretoDisplayType;
    }
    public ConfigProperty<Boolean> getShowDifference() {
      return showDifference;
    }

    public enum whereToDisplay {
      EGGWARS(false, false, "Team EggWars"),
      SKYWARS(false, false, "Solo SkyWars"),
      LUCKYISLANDS(false, false, "Lucky Islands"),
      FFA(false, false, "Free For All"),
      ALLGAMES(false, true,""),
      EVERYWHERE(true, true, "");

      public final boolean everywhere;
      public final boolean games;
      public final String gameName;

      whereToDisplay(boolean everywhere, boolean games, String gameName) {
        this.gameName = gameName;
        this.everywhere = everywhere;
        this.games = games;
      }
    }
  }
}
