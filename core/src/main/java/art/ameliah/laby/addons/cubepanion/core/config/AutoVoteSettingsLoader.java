package art.ameliah.laby.addons.cubepanion.core.config;

import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategory;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategoryOption;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteConfig;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.type.SettingElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AutoVoteSettingsLoader {

  private final AutoVoteConfig EggWarsConfig = new AutoVoteConfig(11, 0, List.of(
      new AutoVoteCategory("team_eggwars_perk_voting", "", 11, "Perk Voting", List.of(
          new AutoVoteCategoryOption(12, "No Perk"),
          new AutoVoteCategoryOption(14, "Perk"),
          new AutoVoteCategoryOption(-1, "Don't vote")
      ))
  ));

  public void loadAndRegisterSettings() {
    var settings = Laby.labyAPI().coreSettingRegistry().getById("cubepanion").getById("autoVote");

    settings.register(createSettingElements(EggWarsConfig));
  }

  private List<SettingElement> createSettingElements(AutoVoteConfig config) {
    var elements = new ArrayList<SettingElement>(config.categories().size());

    for (var category : config.categories()) {
      var element = new SettingElement(category.id(), null, null, new String[0]);

      element.setWidgets(new DropdownWidget[] {
          categoryOptionDropdownWidget(category)
      });

      elements.add(element);
    }

    return elements;
  }

  private DropdownWidget<AutoVoteCategoryOption> categoryOptionDropdownWidget(AutoVoteCategory category) {
    var dropDownWidget = new DropdownWidget<AutoVoteCategoryOption>();

    dropDownWidget.addAll((Collection<AutoVoteCategoryOption>) category.options());
    dropDownWidget.setEntryRenderer(new AutoVoteCategoryOptionRenderer());

    return dropDownWidget;
  }

}
