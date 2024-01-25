package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class TextTrackerHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private final Supplier<String> valueSupplier;
  private final BooleanSupplier visibleSupplier;
  private final BooleanSupplier availableSupplier;
  private final int posX;
  private final int posY;
  private final String text;
  private final String placeholder;
  private TextLine HUDLine;

  public TextTrackerHudWidget(HudWidgetCategory category, String id, String text,
      String placeholder, Supplier<String> valueSupplier, BooleanSupplier visibleSupplier,
      int posX, int posY, BooleanSupplier availableSupplier) {
    super(id);
    this.text = text;
    this.placeholder = placeholder;
    this.valueSupplier = valueSupplier;
    this.visibleSupplier = visibleSupplier;
    this.availableSupplier = availableSupplier;
    this.posX = posX;
    this.posY = posY;

    this.bindCategory(category);
  }

  @Override
  public boolean isHolderEnabled() {
    return this.availableSupplier.getAsBoolean();
  }

  public void load(TextHudWidgetConfig config) {
    super.load(config);

    ResourceLocation resourceLocation = ResourceLocation.create("cubepanion", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, this.posX, this.posY);
    this.setIcon(icon);
    this.HUDLine = super.createLine(this.text, this.placeholder);
  }

  public void onTick(boolean inEditor) {
    if (inEditor) {
      this.HUDLine.updateAndFlush(this.placeholder);
      return;
    }
    this.HUDLine.updateAndFlush(this.valueSupplier.get());
    this.HUDLine.setState(this.visibleSupplier.getAsBoolean() ? State.VISIBLE : State.HIDDEN);
  }

}
