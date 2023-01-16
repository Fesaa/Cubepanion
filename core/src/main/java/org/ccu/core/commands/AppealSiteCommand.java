package org.ccu.core.commands;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.chat.command.Command;
import org.ccu.core.CCU;

public class AppealSiteCommand extends Command {

  private final CCU addon;

  @Inject
  private AppealSiteCommand(CCU addon) {
    super("appeal");

    this.addon = addon;
  }


  @Override
  public boolean execute(String prefix, String[] arguments) {

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
      Component appealSiteLink = Component.empty()
          .append(this.addon.prefix())
          .append(Component.text("Appeal site link for: ", NamedTextColor.GRAY))
          .append(Component.text(userName, NamedTextColor.AQUA)
              .clickEvent(ClickEvent.openUrl(URL)))
          .append(Component.text("\n"));
      this.displayMessage(appealSiteLink);
      return true;
    }

    return false;
  }
}
