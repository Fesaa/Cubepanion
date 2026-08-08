package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategory;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategoryOption;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.configuration.settings.type.SettingElement;
import java.util.Collection;

public class AutoVoteCategoryElement extends SettingElement {

  private final String displayName;

  public AutoVoteCategoryElement(AutoVoteConfig config, AutoVoteCategory category) {
    super(category.id(), null, category.name(), new String[] {
        category.name()
    });

    displayName = category.name();

    setWidgets(new Widget[]{
        createCategoryWidget(config, category)
    });
  }

  private Widget createCategoryWidget(AutoVoteConfig config, AutoVoteCategory category) {
    var widget = new DropdownWidget<AutoVoteCategoryOption>();

    widget.addAll((Collection<AutoVoteCategoryOption>) category.options());
    widget.setEntryRenderer(new AutoVoteCategoryOptionRenderer());
    widget.setChangeListener(new AutoVoteCategoryOptionChangeListener(config, category));

    var slot = config.getSlots().get(category.id());

    AutoVoteCategoryOption selected = null;
    for (var option : category.options()) {
      if (slot != null && option.slot() == slot) {
        selected = option;
        break;
      } else if (option.defaultSelected()) {
        selected = option;
        break;
      }
    }

    if (selected != null) {
      widget.setSelected(selected);
    }

    return widget;
  }

  @Override
  public Component displayName() {
    return Component.text(displayName);
  }
}
