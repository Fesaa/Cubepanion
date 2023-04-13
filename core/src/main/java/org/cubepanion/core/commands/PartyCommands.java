package org.cubepanion.core.commands;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.ClientPlayer;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;

public class PartyCommands extends Command {

  private final Cubepanion addon;

  private final Function<String, Component> errorComponent;
  private final Function<String, Component> helpComponent;

  public PartyCommands(Cubepanion addon) {
    super("party", "p");

    this.addon = addon;
    this.messagePrefix = addon.prefix();

    this.errorComponent = I18nNamespaces.commandNamespaceTransformer("PartyCommands.error");
    this.helpComponent = I18nNamespaces.commandNamespaceTransformer("PartyCommands.helpCommand");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    CommandSystemSubConfig config = this.addon.configuration().getCommandSystemSubConfig();

    if (!config.getPartyCommands().get() || !config.getEnabled().get() || arguments.length == 0) {
      return false;
    }

    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();
    boolean isPartyOwner = this.addon.getManager().getPartyManager().isPartyOwner();
    boolean inParty = this.addon.getManager().getPartyManager().isInParty();

    switch (arguments[0]) {
      case "reinvite":
      case "reinv": {
        if (isPartyOwner) {
          this.reInviteCommand(chat, this.removeFirst(arguments));
        } else {
          this.noPermissions();
        }
        return true;
      }
      case "remake": {
        if (isPartyOwner) {
          this.reMakeCommand(chat, this.removeFirst(arguments));
        } else {
          this.noPermissions();
        }
        return true;
      }
      case "extra": {
        this.helpCommand(arguments[arguments.length-1]);
        return true;
      }
    }
    return false;
  }

  private void noPermissions() {
    this.displayMessage(this.errorComponent.apply("noPermissions").color(Colours.Error));
  }

  private void missingArguments() {
    this.displayMessage(this.errorComponent.apply("missingArguments").color(Colours.Error));
  }

  private void helpCommand(String command) {
    Component helpComponent = this.helpComponent.apply("title")
        .color(Colours.Title);

    boolean run = command.equals("extra");

    if (run || command.equals("reinvite") || command.equals("reinv")) {
      helpComponent = helpComponent
          .append(Component.text("\n/party reinvite <username*>", Colours.Primary)
              .clickEvent(ClickEvent.suggestCommand("/party reinvite ")))
          .append(this.helpComponent.apply("reinv").color(Colours.Secondary));
    }

    if (run || command.equals("remake")) {
      helpComponent = helpComponent
          .append(Component.text("\n/party remake [username*]", Colours.Primary)
              .clickEvent(ClickEvent.suggestCommand("/party remake ")))
          .append(this.helpComponent.apply("remake.first").color(Colours.Secondary))
          .append(this.helpComponent.apply("remake.middle").color(Colours.Primary).decorate(TextDecoration.BOLD))
          .append(this.helpComponent.apply("remake.last").color(Colours.Secondary));
    }

    helpComponent = helpComponent
        .append(Component.text("\n/party extra [command]", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/party extra ")))
        .append(this.helpComponent.apply("extra").color(Colours.Secondary));

    this.displayMessage(helpComponent);
  }

  private void reMakeCommand(ChatExecutor chat, String[] excludedUsernames) {
    chat.chat("/p disband", false);
    int multiplier = 1;

    for (String username : this.addon.getManager().getPartyManager().getPartyMembers()) {
      ClientPlayer p = this.addon.labyAPI().minecraft().getClientPlayer();
      if (p == null) {
        return;
      }
      if (!this.inArray(excludedUsernames, username) && !username.equals(p.getName())) {
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(
            () -> chat.chat("/p invite " + username, false),
            100L * multiplier,
            TimeUnit.MILLISECONDS
        );
        multiplier++;
      }
    }
  }

  private void reInviteCommand(ChatExecutor chat, String[] Usernames) {

    if (Usernames.length == 0) {
      this.missingArguments();
    }

    for (String username : Usernames) {
      if (this.addon.getManager().getPartyManager().isMemberInParty(username)) {
        chat.chat("/p kick " + username, false);
        chat.chat("/p invite " + username, false);
      }
    }
  }

  private boolean inArray(String[] array, String member) {
    for (String element : array) {
      if (element.equals(member)) {
        return true;
      }
    }
    return false;
  }

  private String[] removeFirst(String[] array) {
    if (array.length <= 1) {
      return new String[0];
    }
    String[] slicedArray = new String[array.length - 1];
    System.arraycopy(array, 1, slicedArray, 0, array.length);
    return slicedArray;
  }

}
