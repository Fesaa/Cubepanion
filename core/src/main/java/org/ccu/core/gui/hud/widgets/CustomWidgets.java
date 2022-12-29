package org.ccu.core.gui.hud.widgets;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import org.ccu.core.CCU;

public class CustomWidgets {

  public static void RegisterWidgets(CCU addon) {
    addon.labyAPI().hudWidgetRegistry().register(new EmeraldCounter());
    addon.labyAPI().hudWidgetRegistry().register(new DiamondCounter());
    addon.labyAPI().hudWidgetRegistry().register(new GoldCounter());
    addon.labyAPI().hudWidgetRegistry().register(new IronCounter());
    addon.labyAPI().hudWidgetRegistry().register(new TerracottaCounter());
  }

  @SpriteSlot(x = 0,  y = 4)
  private static class EmeraldCounter extends CounterItemHudWidget {

    public EmeraldCounter() {
      super("emerald");
    }
  }

  @SpriteSlot(x = 0,  y = 3)
  private static class DiamondCounter extends CounterItemHudWidget {

    public DiamondCounter() {
      super("diamond");
    }
  }

  @SpriteSlot(x = 0,  y = 0)
  private static class GoldCounter extends CounterItemHudWidget {

    public GoldCounter() {
      super("gold_ingot");
    }

    public Icon createPlaceholderIcon() {
      ResourceLocation resourceLocation = ResourceLocation.create("ccu", "settings/hud/hud");
      return Icon.sprite16(resourceLocation, 0, 0);
    }

  }

  @SpriteSlot(x = 0,  y = 5)
  private static class IronCounter extends CounterItemHudWidget {

    public IronCounter() {
      super("iron_ingot");
    }
  }

  @SpriteSlot(x = 0,  y = 1)
  private static class TerracottaCounter extends CounterItemHudWidget {

    public TerracottaCounter() {
      super("terracotta");
    }
  }


}
