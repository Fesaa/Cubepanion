package org.cubepanion.core.commands;

import java.util.function.Function;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.TextDecoration;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;

public class AppealSiteCommand extends Command {

  private final Cubepanion addon;

  private final Function<String, Component> componentGetter;

  public AppealSiteCommand(Cubepanion addon) {
    super("appeal", "appeals", "ap", "aps");

    this.addon = addon;
    this.messagePrefix = addon.prefix();

    this.componentGetter = I18nNamespaces.commandNamespaceTransformer("AppealSiteCommand");
  }


  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
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
      Component appealSiteLink = Component.empty()
          .append(this.componentGetter.apply("response").color(Colours.Primary))
          .append(Component.text(userName, Colours.Secondary).decorate(TextDecoration.BOLD)
              .clickEvent(ClickEvent.openUrl(URL))
              .hoverEvent(
                  HoverEvent.showText(this.componentGetter.apply("hover").color(Colours.Hover))))
          .append(Component.newline());

      this.displayMessage(appealSiteLink);
      return true;
    }

    return false;
  }
}
