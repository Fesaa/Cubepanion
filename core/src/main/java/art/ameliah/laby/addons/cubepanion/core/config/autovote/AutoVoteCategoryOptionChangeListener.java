package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategory;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategoryOption;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.ChangeListener;

public class AutoVoteCategoryOptionChangeListener implements ChangeListener<AutoVoteCategoryOption> {

  private final AutoVoteConfig config;
  private final AutoVoteCategory category;

  public AutoVoteCategoryOptionChangeListener(AutoVoteConfig config, AutoVoteCategory category) {
    this.config = config;
    this.category = category;
  }

  @Override
  public void onChange(AutoVoteCategoryOption option) {
    config.getSlots().put(category.id(), option.slot());
  }
}
