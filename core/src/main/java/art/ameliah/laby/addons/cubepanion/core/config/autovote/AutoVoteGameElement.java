package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteConfiguration;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.configuration.settings.type.SettingElement;

public class AutoVoteGameElement extends SettingElement {

  private final String displayName;

  public AutoVoteGameElement(AutoVoteConfig config, AutoVoteConfiguration configuration, Icon icon) {
    super(String.format("auto_vote_game_%d", configuration.gameId()), icon, null, new String[] {
        configuration.gameName()
    });

    displayName = configuration.gameName();

    for (var category : configuration.categories()) {
      addSetting(new AutoVoteCategoryElement(config, configuration, category));
    }
  }

  @Override
  public Component displayName() {
    return Component.text(displayName);
  }
}
