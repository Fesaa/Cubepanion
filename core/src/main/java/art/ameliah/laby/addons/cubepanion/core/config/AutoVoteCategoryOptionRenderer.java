package art.ameliah.laby.addons.cubepanion.core.config;

import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategoryOption;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.EntryRenderer;
import net.labymod.api.client.render.font.RenderableComponent;
import org.jetbrains.annotations.NotNull;

public class AutoVoteCategoryOptionRenderer implements EntryRenderer<AutoVoteCategoryOption> {

  @Override
  public float getWidth(AutoVoteCategoryOption entry, float maxWidth) {
    return this.toRenderableComponent(entry, maxWidth).getWidth();
  }

  @Override
  public float getHeight(AutoVoteCategoryOption entry, float maxWidth) {
    return this.toRenderableComponent(entry, maxWidth).getHeight();
  }

  @Override
  public @NotNull Widget createEntryWidget(AutoVoteCategoryOption entry) {
    return ComponentWidget.component(Component.text(entry.name()));
  }

  private RenderableComponent toRenderableComponent(AutoVoteCategoryOption entry, float maxWidth) {
    return RenderableComponent.builder()
        .maxWidth(maxWidth)
        .disableCache()
        .format(Component.text(entry.name()));
  }
}
