package org.cubepanion.core.commands;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.chat.command.InjectedSubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.util.concurrent.task.Task;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;

public class PartyCommands extends InjectedSubCommand {

  private final Cubepanion addon;
  private final ChatExecutor chatExecutor = Laby.labyAPI().minecraft().chatExecutor();

  private final ArrayList<String> toRemake = new ArrayList<>();
  private final ArrayList<String> toReInv = new ArrayList<>();

  private final Function<String, Component> errorComponent = I18nNamespaces.commandNamespaceTransformer(
      "PartyCommands.error");
  private final Function<String, Component> helpComponent = I18nNamespaces.commandNamespaceTransformer(
      "PartyCommands.helpCommand");
  private final Component noPermissionComponent = this.errorComponent.apply("noPermissions")
      .color(Colours.Error);
  private final Component missingArgumentsComponent = this.errorComponent.apply("missingArguments")
      .color(Colours.Error);  private final Task remakeTask = Task.builder(() -> {
    if (!this.toRemake.isEmpty()) {
      String username = this.toRemake.get(0);
      this.toRemake.remove(0);
      this.chatExecutor.chat("/p invite " + username, false);
      this.remakeTask.execute();
    }
  }).delay(500, TimeUnit.MILLISECONDS).build();
  private final Component helpTitleComponent = this.helpComponent.apply("title")
      .color(Colours.Title);
  private final Component helpReInviteComponent = Component.text("\n/party reinvite <username*>",
          Colours.Primary)
      .clickEvent(ClickEvent.suggestCommand("/party reinvite "))
      .append(this.helpComponent.apply("reinv").color(Colours.Secondary));  private final Task reInviteTask = Task.builder(() -> {
    if (!this.toReInv.isEmpty()) {
      String username = this.toReInv.get(0);
      this.toReInv.remove(0);
      this.chatExecutor.chat("/p kick " + username, false);
      this.chatExecutor.chat("/p invite " + username, false);
      this.reInviteTask.execute();
    }
  }).delay(500, TimeUnit.MILLISECONDS).build();
  private final Component helpRemakeComponent = Component.text("\n/party remake [username*]",
          Colours.Primary)
      .clickEvent(ClickEvent.suggestCommand("/party remake "))
      .append(this.helpComponent.apply("remake.first").color(Colours.Secondary))
      .append(this.helpComponent.apply("remake.middle").color(Colours.Primary)
          .decorate(TextDecoration.BOLD))
      .append(this.helpComponent.apply("remake.last").color(Colours.Secondary));
  private final Component helpExtraComponent = Component.text("\n/party extra [command]",
          Colours.Primary)
      .clickEvent(ClickEvent.suggestCommand("/party extra "))
      .append(this.helpComponent.apply("extra").color(Colours.Secondary));
  public PartyCommands(String prefix, Cubepanion addon) {
    super(prefix, "remake", "extra", "reinv", "reinvite");

    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    CommandSystemSubConfig config = this.addon.configuration().getCommandSystemSubConfig();

    if (!config.getPartyCommands().get() || !config.getEnabled().get()) {
      return false;
    }

    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();
    boolean isPartyOwner = this.addon.getManager().getPartyManager().isPartyOwner();

    switch (prefix) {
      case "reinvite", "reinv" -> {
        if (isPartyOwner) {
          this.reInviteCommand(arguments);
        } else {
          this.noPermissions();
        }
        return true;
      }
      case "remake" -> {
        if (isPartyOwner) {
          this.reMakeCommand(chat, arguments);
        } else {
          this.noPermissions();
        }
        return true;
      }
      case "extra" -> {
        this.helpCommand(arguments.length == 0 ? "extra" : arguments[arguments.length - 1]);
        return true;
      }
    }
    return false;
  }

  private void noPermissions() {
    this.displayMessage(this.noPermissionComponent);
  }

  private void missingArguments() {
    this.displayMessage(this.missingArgumentsComponent);
  }

  private void helpCommand(String command) {
    Component helpComponent = this.helpTitleComponent.copy();

    boolean run = command.equals("extra");

    if (run || command.equals("reinvite") || command.equals("reinv")) {
      helpComponent = helpComponent.append(this.helpReInviteComponent);
    }

    if (run || command.equals("remake")) {
      helpComponent = helpComponent.append(this.helpRemakeComponent);
    }
    this.displayMessage(helpComponent.append(this.helpExtraComponent));
  }

  private void reMakeCommand(ChatExecutor chat, String[] excludedUsernames) {
    ClientPlayer p = this.addon.labyAPI().minecraft().getClientPlayer();
    if (p == null) {
      return;
    }
    chat.chat("/p disband", false);
    this.toRemake.clear();
    for (String username : this.addon.getManager().getPartyManager().getPartyMembers()) {
      if (!this.inArray(excludedUsernames, username) && !username.equals(p.getName())) {
        this.toRemake.add(username);
      }
    }
    this.remakeTask.execute();
  }

  private void reInviteCommand(String[] Usernames) {
    if (Usernames.length == 0) {
      this.missingArguments();
    }
    this.toReInv.clear();
    for (String username : Usernames) {
      if (this.addon.getManager().getPartyManager().isMemberInParty(username)) {
        this.toReInv.add(username);
      } else {
        this.displayMessage(Component.translatable(
            I18nNamespaces.commandNamespace + "PartyCommands.error.cannotReinvite",
            Component.text(username)));
      }
    }
    this.reInviteTask.execute();
  }

  private boolean inArray(String[] array, String member) {
    for (String element : array) {
      if (element.equals(member)) {
        return true;
      }
    }
    return false;
  }






}
