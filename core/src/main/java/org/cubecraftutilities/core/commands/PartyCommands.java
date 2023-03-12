package org.cubecraftutilities.core.commands;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.ClientPlayer;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.CommandSystemSubConfig;
import org.cubecraftutilities.core.utils.Colours;

public class PartyCommands extends Command {

  private final CCU addon;

  public PartyCommands(CCU addon) {
    super("party", "p");

    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
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
          this.reInviteCommand(chat, this.removeFirstN(arguments));
        } else {
          this.noPermissions();
        }
        return true;
      }
      case "remake": {
        if (isPartyOwner) {
          this.reMakeCommand(chat, this.removeFirstN(arguments));
        } else {
          this.noPermissions();
        }
        return true;
      }
      case "kick": {
        if (arguments.length > 2 && isPartyOwner) {
          this.multiKickCommand(chat, this.removeFirstN(arguments));
          return true;
        }
        return false;
      }
      case "invite":
      case "add": {
        if (arguments.length > 2 && (isPartyOwner || !inParty)) {
          this.multiInviteCommand(chat, this.removeFirstN(arguments));
          return true;
        }
        return false;
      }
      case "extra": {
        this.helpCommand(arguments[arguments.length-1]);
        return true;
      }
    }
    return false;
  }

  private void noPermissions() {
    this.displayMessage(Component.text("You need to be party owner to use this command.", Colours.Error));
  }

  private void missingArguments() {
    this.displayMessage(Component.text("You are missing required arguments, please try again.", Colours.Error));
  }

  private void helpCommand(String command) {
    Component helpComponent = Component.text("------- Enhanced Party Commands -------", Colours.Title);

    boolean run = command.equals("extra");

    if (run || command.equals("reinvite") || command.equals("reinv")) {
      helpComponent = helpComponent
          .append(Component.text("\n/party reinvite <username*>", Colours.Primary)
              .clickEvent(ClickEvent.suggestCommand("/party reinvite ")))
          .append(Component.text(" Will kick and invite the passed usernames if they are in the party.", Colours.Secondary));
    }

    if (run || command.equals("remake")) {
      helpComponent = helpComponent
          .append(Component.text("\n/party remake [username*]", Colours.Primary)
              .clickEvent(ClickEvent.suggestCommand("/party remake ")))
          .append(Component.text(" Will disband the party and invite everyone ", Colours.Secondary))
          .append(Component.text(" except ", Colours.Secondary).decorate(TextDecoration.BOLD))
          .append(Component.text("the usernames passed.", Colours.Secondary));
    }

    if (run || command.equals("kick")) {
      helpComponent = helpComponent
          .append(Component.text("\n/party kick [username*]", Colours.Primary)
              .clickEvent(ClickEvent.suggestCommand("/party kick ")))
          .append(Component.text(" Will kick all the passed usernames if they are in the party.", Colours.Secondary));
    }

    if (run || command.equals("invite") || command.equals("add") ) {
      helpComponent = helpComponent
          .append(Component.text("\n/party invite [username*]", Colours.Primary)
              .clickEvent(ClickEvent.suggestCommand("/party invite ")))
          .append(Component.text(" Will invite all the passed usernames.", Colours.Secondary));
    }

    helpComponent = helpComponent
        .append(Component.text("\n/party extra [command]", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/party extra ")))
        .append(Component.text(" Displays the help message for a command. Omitting the command, displays for all commands.", Colours.Secondary));

    this.displayMessage(helpComponent);
  }

  private void multiInviteCommand(ChatExecutor chat, String[] usernames) {
    int multiplier = 1;

    for (String username : usernames) {
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(
          () -> chat.chat("/p invite " + username, false),
          100L * multiplier,
          TimeUnit.MILLISECONDS
      );
      multiplier++;
    }
  }

  private void multiKickCommand(ChatExecutor chat, String[] usernames) {
    int multiplier = 1;

    for (String username : usernames) {
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(
          () -> chat.chat("/p kick " + username, false),
          100L * multiplier,
          TimeUnit.MILLISECONDS
      );
      multiplier++;
    }
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

  private String[] removeFirstN(String[] array) {
    if (array.length <= 1) {
      return new String[0];
    }
    String[] slicedArray = new String[array.length - 1];
    System.arraycopy(array, 1, slicedArray, 0, array.length);

    return slicedArray;
  }

}
