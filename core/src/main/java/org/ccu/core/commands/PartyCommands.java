package org.ccu.core.commands;

import com.google.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.chat.command.Command;
import org.ccu.core.CCU;

public class PartyCommands extends Command {

  private final CCU addon;
  private final String userNameRegex = "([a-zA-Z0-9_]{2,16})";

  @Inject
  private PartyCommands(CCU addon) {
    super("party");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (!this.addon.getManager().getPartyManager().isPartyOwner()) {
      return false;
    }

    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();

    switch (arguments.length) {
      case 2: {
        if (arguments[0].equals("reinvite") && arguments[1].matches(this.userNameRegex)) {
          chat.chat("/p kick " + arguments[1], false);
          chat.chat("/p invite " + arguments[1], false);
          return true;
        }
        break;
      }
      case 1: {
        if (arguments[0].equals("remake")) {
          chat.chat("/p disband", false);
          int multiplier = 1;
          for (String userName : this.addon.getManager().getPartyManager().getPartyMembers()) {
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(
                () -> chat.chat("/p invite " + userName, false),
                100L * multiplier,
                TimeUnit.MILLISECONDS
            );
            multiplier++;
          }
          return  true;
        }
        break;
      }
    }

    return false;
  }

}
