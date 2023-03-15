package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.TextDecoration;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.CommandSystemSubConfig;
import org.cubecraftutilities.core.utils.Colours;

public class AppealSiteCommand extends Command {

  private final CCU addon;

  public AppealSiteCommand(CCU addon) {
    super("appeal");

    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }


  @Override
  public boolean execute(String prefix, String[] arguments) {
    CommandSystemSubConfig config = this.addon.configuration().getCommandSystemSubConfig();

    if (!config.getAppealSiteCommand().get() || !config.getEnabled().get()) {
      return false;
    }

    if (arguments.length == 1) {
      String URL;
      String userName;
      if (!arguments[0].startsWith("mco/")) {
        userName = arguments[0];
        URL = "https://appeals.cubecraft.net/find_appeals/" + userName + "/JAVA";
      } else {
        userName = arguments[0].replace("mco/", "");
        URL = "https://appeals.cubecraft.net/find_appeals/" + userName + "/MCO";
      }
      Component appealSiteLink = Component.newline()
          .append(Component.text("Appeal site link for: ", Colours.Primary))
          .append(Component.text(userName, Colours.Secondary).decorate(TextDecoration.BOLD)
              .clickEvent(ClickEvent.openUrl(URL))
              .hoverEvent(HoverEvent.showText(Component.text("Click to open URL", Colours.Hover))))
          .append(Component.newline());

      this.displayMessage(appealSiteLink);
      return true;
    }

    return false;
  }
}
