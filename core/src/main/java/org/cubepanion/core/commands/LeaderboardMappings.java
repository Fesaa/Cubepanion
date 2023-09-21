package org.cubepanion.core.commands;

import art.ameliah.libs.weave.LeaderboardAPI.Leaderboard;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.Utils;
import java.util.Map.Entry;
import java.util.Set;

public class LeaderboardMappings extends Command {


  public LeaderboardMappings(Cubepanion addon) {
    super("lbmappings");

    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    Component toDisplay = Component.translatable("cubepanion.messages.leaderboardAPI.commands.help.lbmappings", Colours.Title);

    for (Entry<Leaderboard, Set<String>> e : Cubepanion.weave.getLeaderboardAPI().getAliases().entrySet()) {
      toDisplay = toDisplay
          .append(Component.text(e.getKey().displayName(), Colours.Primary))
          .append(Component.text(" => ", Colours.Secondary))
          .append(Utils.join(Component.text(", ", NamedTextColor.GRAY),
              e.getValue().stream().map(s -> Component.text(s, Colours.Secondary).decorate(TextDecoration.ITALIC)).toList()))
          .append(Component.newline());
    }

    displayMessage(toDisplay);
    return true;
  }
}
