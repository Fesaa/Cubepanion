package org.cubecraftutilities.core.gui.hud.widgets;

import java.util.function.Supplier;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class TextTrackerHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private final Supplier<String> valueSupplier;
  private final Supplier<Boolean> visibleSupplier;
  private final int posX;
  private final int posY;
  private final String text;
  private TextLine HUDLine;

  public TextTrackerHudWidget(HudWidgetCategory category,String id, String text, Supplier<String> valueSupplier, Supplier<Boolean> visibleSupplier, int posX, int posY) {
    super(id);
    this.text = text;
    this.valueSupplier = valueSupplier;
    this.visibleSupplier = visibleSupplier;
    this.posX = posX;
    this.posY = posY;

    this.bindCategory(category);
  }

  public void load(TextHudWidgetConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("cubecraftutilities", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, this.posX, this.posY);
    this.setIcon(icon);
    this.HUDLine = super.createLine(this.text, "");
  }

  public void onTick(boolean inEditor) {
    if (inEditor) {
      return;
    }
    this.HUDLine.updateAndFlush(this.valueSupplier.get());
    this.HUDLine.setVisible(this.visibleSupplier.get());
  }

}
