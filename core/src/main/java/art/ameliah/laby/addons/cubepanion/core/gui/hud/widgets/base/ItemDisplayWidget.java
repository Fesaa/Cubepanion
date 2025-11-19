package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gfx.pipeline.renderer.text.TextRenderingOptions;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.state.ScreenCanvas;
import net.labymod.api.client.render.font.RenderableComponent;
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

  @Override
  public void render(RenderPhase phase, ScreenContext context, boolean isEditorContext,
      HudSize size) {
    if ((items.isEmpty() && !isEditorContext) || phase.equals(RenderPhase.UPDATE_SIZE)) {
      return;
    }

    switch (config.getOrientation().get()) {
      case HORIZONTAL:
        horizontalRender(context, isEditorContext, size);
        break;
      case VERTICAL:
        verticalRender(context, isEditorContext, size);
        break;
    }
  }

  private void verticalRender(ScreenContext context, boolean isEditorContext, HudSize size) {
    ScreenCanvas renderState = context.canvas();
    float segmentSpacing = config.segmentSpacing().get();
    float textOffSet = 4.0F;
    boolean renderName = config.getShowName().get() && shouldRenderName();

    List<DisplayItem> toRender = (items.isEmpty() || isEditorContext) ? dummyItems : items;

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
      this.labyAPI.minecraft()
          .itemStackRenderer()
          .renderItemStack(context.stack(),
              item.backingItemStack(),
              itemStackX,
              itemStackY);

      if (renderName) {
        float textX;
        if (anchor().isRight()) {
          textX = maxTextWidth - text.getWidth();
        } else {
          textX = itemStackX + itemSize + textOffSet + (floatingPointPosition ? 0.5F : 0.0F);
          textWidth = Math.max(textWidth, text.getWidth());
        }

        float textY = itemStackY + itemSize / 2.0F - text.getHeight() / 2.0F;
        renderState.submitRenderableComponent(
            text,
            textX, textY, -1, TextRenderingOptions.NONE
        );
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

  private void horizontalRender(ScreenContext context, boolean isEditorContext, HudSize size) {
    ScreenCanvas renderState = context.canvas();
    float segmentSpacing = config.segmentSpacing().get();
    boolean renderName = config.getShowName().get() && shouldRenderName();

    List<DisplayItem> toRender = (items.isEmpty() || isEditorContext) ? dummyItems : items;

    boolean floatingPointPosition = Laby.references().themeService().currentTheme().metadata()
        .get("hud_widget_floating_point_position", false);
    float x = 0.0F;
    float segmentWidth;

    for (DisplayItem item : toRender) {
      RenderableComponent text = item.getRenderableComponent();

      int itemStackX = (int) x;
      int itemStackY = 0;
      this.labyAPI.minecraft()
          .itemStackRenderer()
          .renderItemStack(
              context.stack(),
              item.backingItemStack(),
              itemStackX,
              itemStackY
          );

      if (renderName) {
        renderState.submitRenderableComponent(
            text,
            itemStackX + itemSize / 2.0F + (floatingPointPosition ? 0.5F : 0.0F),
            itemStackY + itemSize,
            -1,
            TextRenderingOptions.NONE
        );
      }

      segmentWidth = Math.max(itemSize, text.getWidth());
      if (segmentWidth % 2.0F != 0.0F) {
        ++segmentWidth;
      }
      x += segmentWidth + segmentSpacing;
    }

    float textOffSet = renderName ? renderState.getLineHeight() : 0.0F;
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
