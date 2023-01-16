package org.ccu.core;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CommandManager {

  private final CCU addon;
  private final List<Pair<Supplier<ConfigProperty<Boolean>>, Class<? extends Command>>> allCommands;
  private final List<Class<? extends Command>> activeCommands;
  private final List<Class<? extends Command>> inActiveCommands;

  @SafeVarargs
  public CommandManager(CCU addon, Pair<Supplier<ConfigProperty<Boolean>>, Class<? extends Command>>... commands) {
    this.addon = addon;

    this.allCommands = List.of(commands);
    this.activeCommands = new ArrayList<>();
    this.inActiveCommands = new ArrayList<>();

    for (Pair<Supplier<ConfigProperty<Boolean>>, Class<? extends Command>> pair : this.allCommands) {
      this.inActiveCommands.add(pair.getSecond());
    }
  }

  public void updateCommandRegistration() {
    for (Pair<Supplier<ConfigProperty<Boolean>>, Class<? extends Command>> pair : this.allCommands) {
      if (pair.getFirst().get().get()) {
        this.activateCommand(pair.getSecond());
      } else {
        this.deActivateCommand(pair.getSecond());
      }
    }
  }

  private void deActivateCommand(Class<? extends Command> command) {
    if (this.activeCommands.remove(command)) {
      try {
        this.addon.labyAPI().commandService().unregister(command);
        this.inActiveCommands.add(command);
      } catch (Exception e) {
        this.addon.logger().error(e.getMessage());
      }
    }
  }

  private void activateCommand(Class<? extends Command> command) {

    if (this.inActiveCommands.remove(command)) {
      try {
        this.addon.labyAPI().commandService().register(command);
        this.activeCommands.add(command);
      } catch(Exception e)  {
        this.addon.logger().error(e.getMessage());
      }
    }
  }
}
