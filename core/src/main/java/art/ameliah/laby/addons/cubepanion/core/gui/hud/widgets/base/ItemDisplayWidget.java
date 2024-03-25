package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.render.ItemStackRenderer;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class ItemDisplayWidget<T extends ItemDisplayConfig> extends SimpleHudWidget<T> {

  private final List<DisplayItem> items = new ArrayList<>();
  private final List<DisplayItem> dummyItems = new ArrayList<>();
  private final float itemSize = 16.0F;

  public ItemDisplayWidget(String id, HudWidgetCategory hudWidgetCategory, Class<T> configClass,
      DisplayItem... dummyItems) {
    super(id, configClass);
    this.dummyItems.addAll(Arrays.asList(dummyItems));

    bindCategory(hudWidgetCategory);
  }

  protected abstract boolean shouldRenderName();

  protected void setDummyItems(List<DisplayItem> items) {
    this.dummyItems.clear();
    this.dummyItems.addAll(items);
  }

  protected void setItems(List<DisplayItem> items) {
    this.items.clear();
    this.items.addAll(items);
  }

  protected void clearItems() {
    this.items.clear();
  }

  public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext,
      HudSize size) {
    if ((items.isEmpty() && !isEditorContext) || stack == null) {
      return;
    }

    switch (config.getOrientation().get()) {
      case HORIZONTAL:
        horizontalRender(stack, mouse, size);
        break;
      case VERTICAL:
        verticalRender(stack, mouse, size);
        break;
    }
  }

  private void verticalRender(Stack stack, MutableMouse mouse, HudSize size) {
    ItemStackRenderer itemStackRenderer = this.labyAPI.minecraft().itemStackRenderer();
    ComponentRenderer componentRenderer = this.labyAPI.renderPipeline().componentRenderer();
    float segmentSpacing = config.segmentSpacing().get();
    float textOffSet = 4.0F;
    boolean renderName = config.getShowName().get() && shouldRenderName();

    List<DisplayItem> toRender = items.isEmpty() ? dummyItems : items;

    boolean floatingPointPosition = Laby.references().themeService().currentTheme().metadata()
        .get("hud_widget_floating_point_position", false);
    float y = 0.0F;
    float segmentHeight;
    float textWidth = 0.0F;
    float maxTextWidth = 0.0F;

    if (anchor().isRight() && renderName) {
      for (DisplayItem item : toRender) {
        RenderableComponent text = item.getRenderableComponent();
        maxTextWidth = Math.max(maxTextWidth, text.getWidth());
      }
      textWidth = maxTextWidth;
    }

    for (DisplayItem item : toRender) {
      RenderableComponent text = item.getRenderableComponent();

      int itemStackX = anchor().isRight() && renderName ? (int) (maxTextWidth + textOffSet) : 0;
      int itemStackY = (int) y;
      itemStackRenderer.renderItemStack(stack, item.backingItemStack(), itemStackX, itemStackY);

      if (renderName) {
        float textX;
        if (anchor().isRight()) {
          textX = maxTextWidth - text.getWidth();
        } else {
          textX = itemStackX + itemSize + textOffSet + (floatingPointPosition ? 0.5F : 0.0F);
          textWidth = Math.max(textWidth, text.getWidth());
        }

        float textY = itemStackY + itemSize / 2.0F - text.getHeight() / 2.0F;
        componentRenderer
            .builder()
            .pos(textX, textY)
            .useFloatingPointPosition(floatingPointPosition)
            .centered(false)
            .text(text)
            .render(stack);
      }
      segmentHeight = Math.max(itemSize, text.getHeight());
      if (segmentHeight % 2.0F != 0.0F) {
        ++segmentHeight;
      }
      y += segmentHeight + segmentSpacing;
    }
    float textSize = renderName ? textWidth + textOffSet : 0.0F;
    size.set(itemSize + textSize, Math.max(y - segmentSpacing, segmentSpacing));
  }

  private void horizontalRender(Stack stack, MutableMouse mouse, HudSize size) {
    ItemStackRenderer itemStackRenderer = this.labyAPI.minecraft().itemStackRenderer();
    ComponentRenderer componentRenderer = this.labyAPI.renderPipeline().componentRenderer();
    float segmentSpacing = config.segmentSpacing().get();
    boolean renderName = config.getShowName().get() && shouldRenderName();

    List<DisplayItem> toRender = items.isEmpty() ? dummyItems : items;

    boolean floatingPointPosition = Laby.references().themeService().currentTheme().metadata()
        .get("hud_widget_floating_point_position", false);
    float x = 0.0F;
    float segmentWidth;

    for (DisplayItem item : toRender) {
      RenderableComponent text = item.getRenderableComponent();

      int itemStackX = (int) x;
      int itemStackY = 0;
      itemStackRenderer.renderItemStack(stack, item.backingItemStack(), itemStackX, itemStackY);

      if (renderName) {
        componentRenderer
            .builder()
            .pos(itemStackX + itemSize / 2.0F + (floatingPointPosition ? 0.5F : 0.0F),
                itemStackY + itemSize)
            .useFloatingPointPosition(floatingPointPosition)
            .centered(true)
            .text(text)
            .render(stack);
      }

      segmentWidth = Math.max(itemSize, text.getWidth());
      if (segmentWidth % 2.0F != 0.0F) {
        ++segmentWidth;
      }
      x += segmentWidth + segmentSpacing;
    }

    float textOffSet = renderName ? componentRenderer.height() : 0.0F;
    size.set(Math.max(x - segmentSpacing, segmentSpacing), itemSize + textOffSet);
  }


  @Override
  public boolean isVisibleInGame() {
    Key hotKey = config.getKey().get();
    boolean vis = hotKey == Key.NONE || hotKey.isPressed();
    return !items.isEmpty() && vis;
  }

  public interface DisplayItem {

    @NotNull
    RenderableComponent getRenderableComponent();

    @NotNull
    Component getComponent();

    @NotNull
    ItemStack backingItemStack();
  }
}
