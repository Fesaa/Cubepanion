package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.labynet.LabyNetController;
import net.labymod.api.labynet.models.NameHistory;
import net.labymod.api.util.io.web.result.Result;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.Colours;
import java.util.List;

public class NameHistoryCommand extends Command {

  private final CCU addon;
  private Result<List<NameHistory>> nameHistory;

  public NameHistoryCommand(CCU addon) {
    super("namehistory", "nh");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (!this.addon.configuration().getCommandSystemSubConfig().getNameHistoryCommand().get()) {
      return false;
    }

    if (arguments.length != 1) {
      this.displayMessage(
          this.addon.prefix()
              .append(Component.text("Please provide a valid username.", Colours.Error)));
      return true;
    }

    LabyNetController labyNetController = this.addon.labyAPI().labyNetController();
    labyNetController.loadNameHistory(arguments[0], (result) -> this.nameHistory = result);

    if (this.nameHistory == null || this.nameHistory.hasException()) {
      this.displayMessage(
          this.addon.prefix()
              .append(Component.text(" An error occurred. Try again later", Colours.Error)));
      return true;
    }

    List<NameHistory> nameHistories = (List)this.nameHistory.get();
    Component output = this.addon.prefix()
        .append(Component.text("------ Name history ------", Colours.Title))
        .append(Component.newline());

    for (NameHistory history : nameHistories) {
      output = output.append(this.componentify(history));
    }

    this.displayMessage(output);
    return true;
  }

  private Component componentify(NameHistory nameHistory) {
    String name = nameHistory.getName();
    String changed_at = nameHistory.getChangedAtString();

    Component out = Component.empty();
    if (changed_at != null && !changed_at.equals("")) {
      out = out.append(Component.text(changed_at.split("T")[0], Colours.Primary));
    } else {
      out = out.append(Component.text("0000-00-00"));
    }

    out = out
        .append(Component.text(": ", NamedTextColor.GRAY))
        .append(Component.text(
            name.matches("[a-zA-Z0-9_]{2,16}") ? name : "hidden"
            ,Colours.Secondary));

    return out.append(Component.newline());

  }
}
