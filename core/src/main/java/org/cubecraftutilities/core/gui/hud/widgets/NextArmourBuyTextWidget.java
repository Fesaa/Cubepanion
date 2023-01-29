package org.cubecraftutilities.core.gui.hud.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.cubecraftutilities.core.config.CCUManager;
import org.cubecraftutilities.core.config.submanagers.DurabilityManager;
import org.cubecraftutilities.core.gui.hud.widgets.NextArmourBuyTextWidget.NextArmourBuyHudConfig;
import org.cubecraftutilities.core.gui.hud.widgets.NextArmourBuyTextWidget.NextArmourBuyHudConfig.whereToDisplay;

public class NextArmourBuyTextWidget extends TextHudWidget<NextArmourBuyHudConfig> {

  private final CCUManager manager;
  private TextLine nextArmourBuy;

  public NextArmourBuyTextWidget(String id, CCUManager manager) {
    super(id, NextArmourBuyHudConfig.class);
    this.manager = manager;
  }

  public void load(NextArmourBuyHudConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("ccu", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, 1, 1);
    this.setIcon(icon);
    this.nextArmourBuy = super.createLine("Next armour to run out", "");
  }

  public void onTick() {
    DurabilityManager durabilityManager = this.manager.getDurabilityManager();
    int totalHelmetDurability = durabilityManager.getTotalHelmetDurability();
    int totalChestPlateDurability = durabilityManager.getTotalChestPlateDurability();
    int totalLeggingsDurability = durabilityManager.getTotalLeggingsDurability();
    int totalBootsDurability = durabilityManager.getTotalBootsDurability();
    
    
    if (totalHelmetDurability < totalChestPlateDurability
        && totalHelmetDurability < totalLeggingsDurability
        && totalHelmetDurability < totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush("Helmet");
    }
    else if (totalChestPlateDurability < totalHelmetDurability
        && totalChestPlateDurability < totalLeggingsDurability
        && totalChestPlateDurability < totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush("Chestplate");
    }
    else if (totalLeggingsDurability < totalHelmetDurability
        && totalLeggingsDurability < totalChestPlateDurability
        && totalLeggingsDurability < totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush("Leggings");
    }
    else if (totalBootsDurability < totalHelmetDurability
        && totalBootsDurability < totalChestPlateDurability
        && totalBootsDurability < totalLeggingsDurability) {
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
    if (whereToDisplay.games && !this.manager.isInPreLobby()) {
      return true;
    }
    if (this.manager.getDivisionName().equals(whereToDisplay.gameName) && !this.manager.isInPreLobby()) {
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
