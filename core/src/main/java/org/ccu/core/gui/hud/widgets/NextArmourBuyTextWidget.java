package org.ccu.core.gui.hud.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.TextFormat;
import org.ccu.core.config.internal.CCUinternalConfig;

public class NextArmourBuyTextWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine nextArmourBuy;

  public NextArmourBuyTextWidget(String id) {
    super(id, TextHudWidgetConfig.class);
  }

  public void load(TextHudWidgetConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("ccu", "themes/vanilla/textures/settings/hud/hud.png");
    Icon icon = Icon.sprite16(resourceLocation, 1, 1);
    this.setIcon(icon);
    this.nextArmourBuy = super.createLine("Next armour break", "");
  }

  public void onTick() {
    if (CCUinternalConfig.totalHelmetDurability < CCUinternalConfig.totalChestPlateDurability
        && CCUinternalConfig.totalHelmetDurability < CCUinternalConfig.totalLeggingsDurability
        && CCUinternalConfig.totalHelmetDurability < CCUinternalConfig.totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush(TextFormat.SNAKE_CASE.toUpperCamelCase("Helmet"));
    }
    else if (CCUinternalConfig.totalChestPlateDurability < CCUinternalConfig.totalHelmetDurability
        && CCUinternalConfig.totalChestPlateDurability < CCUinternalConfig.totalLeggingsDurability
        && CCUinternalConfig.totalChestPlateDurability < CCUinternalConfig.totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush(TextFormat.SNAKE_CASE.toUpperCamelCase("Chestplate"));
    }
    else if (CCUinternalConfig.totalLeggingsDurability < CCUinternalConfig.totalHelmetDurability
        && CCUinternalConfig.totalLeggingsDurability < CCUinternalConfig.totalChestPlateDurability
        && CCUinternalConfig.totalLeggingsDurability < CCUinternalConfig.totalBootsDurability) {
      this.nextArmourBuy.updateAndFlush(TextFormat.SNAKE_CASE.toUpperCamelCase("Leggings"));
    }
    else if (CCUinternalConfig.totalBootsDurability < CCUinternalConfig.totalHelmetDurability
        && CCUinternalConfig.totalBootsDurability < CCUinternalConfig.totalChestPlateDurability
        && CCUinternalConfig.totalBootsDurability < CCUinternalConfig.totalLeggingsDurability) {
      this.nextArmourBuy.updateAndFlush(TextFormat.SNAKE_CASE.toUpperCamelCase("Boots"));
    } else {
      this.nextArmourBuy.updateAndFlush(TextFormat.SNAKE_CASE.toUpperCamelCase("N/A"));
    }

    this.nextArmourBuy.setVisible(CCUinternalConfig.name.equals("Team EggWars")
                                        && !CCUinternalConfig.inPreLobby);
  }
}
