package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategory;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategoryOption;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import java.util.Collection;

public class AutoVoteCategoryDropdownWidget extends DropdownWidget<AutoVoteCategoryOption> {

  public AutoVoteCategoryDropdownWidget(AutoVoteConfig config, AutoVoteCategory category) {
    addAll((Collection<AutoVoteCategoryOption>) category.options());
    setEntryRenderer(new AutoVoteCategoryOptionRenderer());
    setChangeListener(new AutoVoteCategoryOptionChangeListener(config, category));

    var slot = config.getSlots().getOrDefault(category.id(), -1);

    AutoVoteCategoryOption selected = null;
    for (var option : category.options()) {
      if (option.slot() == slot) {
        selected = option;
        break;
      }
    }

    if (selected != null) {
      setSelected(selected);
    } else {
      setSelected(AutoVoteCategoryOption.DontVoteOption);
    }
  }

}
