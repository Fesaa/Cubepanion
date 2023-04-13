package org.cubepanion.core.gui.hud.widgets.base;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import org.jetbrains.annotations.NotNull;

public class CubepanionWidgetCategory extends HudWidgetCategory {

  public CubepanionWidgetCategory(String id) {
    super(id);
  }

  @NotNull
  public Component title() {
    return Component.translatable("cubepanion.hudWidgetCategory." + this.id + ".name");
  }

  @NotNull
  public Component description() {
    return Component.translatable("cubepanion.hudWidgetCategory." + this.id + ".description");
  }
}
