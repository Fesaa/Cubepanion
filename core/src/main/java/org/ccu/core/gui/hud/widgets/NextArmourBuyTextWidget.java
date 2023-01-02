package org.ccu.core.gui.hud.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.ccu.core.config.internal.CCUinternalConfig;
import org.ccu.core.gui.hud.widgets.NextArmourBuyTextWidget.NextArmourBuyHudConfig;
import org.ccu.core.gui.hud.widgets.NextArmourBuyTextWidget.NextArmourBuyHudConfig.whereToDisplay;

public class NextArmourBuyTextWidget extends TextHudWidget<NextArmourBuyHudConfig> {

  private TextLine nextArmourBuy;

  public NextArmourBuyTextWidget(String id) {
    super(id, NextArmourBuyHudConfig.class);
  }

  public void load(NextArmourBuyHudConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("ccu", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, 1, 1);
    this.setIcon(icon);
    this.nextArmourBuy = super.createLine("Next armour break", "");
  }

  public void onTick() {
    if (CCUinternalConfig.totalHelmetDurability < CCUinternalConfig.totalChestPlateDurability
        && CCUinternalConfig.totalHelmetDurability < CCUinternalConfig.totalLeggingsDurability
        && CCUinternalConfig.totalHelmetDurability < CCUinternalConfig.totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush("Helmet");
    }
    else if (CCUinternalConfig.totalChestPlateDurability < CCUinternalConfig.totalHelmetDurability
        && CCUinternalConfig.totalChestPlateDurability < CCUinternalConfig.totalLeggingsDurability
        && CCUinternalConfig.totalChestPlateDurability < CCUinternalConfig.totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush("Chestplate");
    }
    else if (CCUinternalConfig.totalLeggingsDurability < CCUinternalConfig.totalHelmetDurability
        && CCUinternalConfig.totalLeggingsDurability < CCUinternalConfig.totalChestPlateDurability
        && CCUinternalConfig.totalLeggingsDurability < CCUinternalConfig.totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush("Leggings");
    }
    else if (CCUinternalConfig.totalBootsDurability < CCUinternalConfig.totalHelmetDurability
        && CCUinternalConfig.totalBootsDurability < CCUinternalConfig.totalChestPlateDurability
        && CCUinternalConfig.totalBootsDurability < CCUinternalConfig.totalLeggingsDurability) {
      this.nextArmourBuy.updateAndFlush("Boots");
    } else {
      this.nextArmourBuy.updateAndFlush("N/A");
    }

    this.nextArmourBuy.setVisible(this.shouldBeVisible());
  }

  private boolean shouldBeVisible() {
    whereToDisplay whereToDisplay = this.config.getWheretoDisplayType().get();
    if (whereToDisplay.everywhere) {
      return true;
    }
    if (whereToDisplay.games && !CCUinternalConfig.inPreLobby) {
      return true;
    }
    if (CCUinternalConfig.name.equals(whereToDisplay.gameName) && !CCUinternalConfig.inPreLobby) {
      return true;
    }
    return false;
  }

  public static class NextArmourBuyHudConfig extends TextHudWidgetConfig {

    @DropdownSetting
    private final ConfigProperty<whereToDisplay> wheretoDisplayType = new ConfigProperty<>(whereToDisplay.EGG_WARS);

    public ConfigProperty<whereToDisplay> getWheretoDisplayType() {
      return wheretoDisplayType;
    }

    public enum whereToDisplay {
      EGG_WARS(false, false, "Team EggWars"),
      SKY_WARS(false, false, "Solo SkyWars"),
      LUCKY_ISLANDS(false, false, "Lucky Islands"),
      FFA(false, false, "FFA"),
      ALL_GAMES(false, true,""),
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
