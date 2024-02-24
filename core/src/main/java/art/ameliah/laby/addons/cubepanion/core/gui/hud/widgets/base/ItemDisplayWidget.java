package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.background.BackgroundHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.render.ItemStackRenderer;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ItemDisplayWidget extends BackgroundHudWidget<ItemDisplayConfig> {

  private final List<DisplayItem> items = new ArrayList<>();
  private final List<DisplayItem> dummyItems = new ArrayList<>();

  public ItemDisplayWidget(String id, HudWidgetCategory hudWidgetCategory,
      DisplayItem... dummyItems) {
    super(id, ItemDisplayConfig.class);
    this.dummyItems.addAll(Arrays.asList(dummyItems));

    bindCategory(hudWidgetCategory);
  }

  protected void setItems(List<DisplayItem> items) {
    this.items.clear();
    this.items.addAll(items);
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
    float itemSize = 16.0F;
    float margin = config.background().getMargin();
    float padding = config.background().getPadding();
    float segmentSpacing = config.segmentSpacing().get();

    List<DisplayItem> toRender = items.isEmpty() ? dummyItems : items;

    boolean floatingPointPosition = Laby.references().themeService().currentTheme().metadata()
        .get("hud_widget_floating_point_position", false);
    float y = 0.0F;
    float segmentHeight = 0.0F;

    for (DisplayItem item : toRender) {
      super.renderBackgroundSegment(stack,
          margin,
          y + margin,
          itemSize + componentRenderer.height() + padding * 2.0F,
          itemSize);

      RenderableComponent text = item.getRenderableComponent();

      itemStackRenderer.renderItemStack(
          stack,
          item.backingItemStack(),
          (int) (padding + margin),
          (int) (y + segmentHeight / 2.0F - 8.0F));

      componentRenderer
          .builder()
          .pos((int) (padding + margin) + itemSize * 2.0F,
              y + segmentHeight / 2.0F - 4.0F  + (floatingPointPosition ? 0.5F : 0.0F))
          .useFloatingPointPosition(floatingPointPosition)
          .centered(true)
          .text(text)
          .render(stack);

      segmentHeight = itemSize + padding * 2.0F;
      if (segmentHeight % 2.0F != 0.0F) {
        ++segmentHeight;
      }
      y += segmentHeight + segmentSpacing;
    }

    size.set(itemSize  + segmentHeight + padding * 2.0F + margin * 2.0F,
        Math.max(y - segmentSpacing, segmentSpacing) + margin * 2.0F);
  }

  private void horizontalRender(Stack stack, MutableMouse mouse, HudSize size) {
    ItemStackRenderer itemStackRenderer = this.labyAPI.minecraft().itemStackRenderer();
    ComponentRenderer componentRenderer = this.labyAPI.renderPipeline().componentRenderer();
    float itemSize = 16.0F;
    float margin = config.background().getMargin();
    float padding = config.background().getPadding();
    float segmentSpacing = config.segmentSpacing().get();

    List<DisplayItem> toRender = items.isEmpty() ? dummyItems : items;

    boolean floatingPointPosition = Laby.references().themeService().currentTheme().metadata()
        .get("hud_widget_floating_point_position", false);
    float x = 0.0F;
    float segmentWidth = 0.0F;

    for (DisplayItem item : toRender) {
      super.renderBackgroundSegment(stack, x + margin, margin, segmentWidth,
          itemSize + componentRenderer.height() + padding * 2.0F);

      RenderableComponent text = item.getRenderableComponent();

      itemStackRenderer.renderItemStack(stack, item.backingItemStack(),
          (int) (x + segmentWidth / 2.0F - 8.0F + margin), (int) (padding + margin));
      componentRenderer
          .builder()
          .pos(x + segmentWidth / 2.0F + margin + (floatingPointPosition ? 0.5F : 0.0F),
              itemSize + padding + margin)
          .useFloatingPointPosition(floatingPointPosition)
          .centered(true)
          .text(text)
          .render(stack);
      segmentWidth = Math.max(itemSize, text.getWidth()) + padding * 2.0F;
      if (segmentWidth % 2.0F != 0.0F) {
        ++segmentWidth;
      }
      x += segmentWidth + segmentSpacing;
    }

    size.set(Math.max(x - segmentSpacing, segmentSpacing) + margin * 2.0F,
        itemSize + componentRenderer.height() + padding * 2.0F + margin * 2.0F);
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
    ItemStack backingItemStack();
  }
}
