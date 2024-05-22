package art.ameliah.laby.addons.cubepanion.core.commands;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink.VotePair;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AutoVoteCommand extends Command {

  private final Cubepanion addon;


  public AutoVoteCommand(Cubepanion addon) {
    super("autovote", "av");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] args) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }

    if (this.addon.getVotingLink() == null) {
      LOGGER.debug(getClass(), "AutoVoteLink is null, can't execute command.");
      return false;
    }

    if (args.length < 1) {
      this.displayHelp();
      return true;
    }

    String game = args[0];
    if (game.equalsIgnoreCase("help")) {
      this.displayHelp();
      return true;
    }

    CubeGame cubeGame = CubeGame.stringToGame(game);
    if (cubeGame == null) {
      this.displayMessage(Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.error.gameNotFound",
          Component.text(game)).color(Colours.Error));
      return true;
    }

    AutoVoteProvider provider = AutoVoteProvider.getProvider(cubeGame);
    if (provider == null) {
      this.displayMessage(Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.error.providerNotFound",
          Component.text(game)).color(Colours.Error));
      return true;
    }

    List<Supplier<VotePair>> pairs = provider.getVotePairSuppliers();
    if (args.length < pairs.size() + 1) {
      this.displayMessage(Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.error.notEnoughArguments",
          Component.text(game)).color(Colours.Error));
      return true;
    }

    List<Supplier<VotePair>> newPairs = new ArrayList<>(pairs.size());
    for (int i = 1; i < pairs.size()  +1; i++) {
      VotePair pair = pairs.get(i -1).get();
      String overRide = args[i];

      try {
        int slot = Integer.parseInt(overRide);
        newPairs.add(() -> VotePair.of(pair.choiceIndex(), slot));
      } catch (NumberFormatException e) {
        this.displayMessage(Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.error.invalidSlot",
            Component.text(overRide)).color(Colours.Error));
        return true;
      }
    }

    AutoVoteProvider newProvider = AutoVoteProvider.of(provider.getHotbarSlot(), newPairs);
    this.addon.getVotingLink().vote(newProvider);
    return true;
  }

  private void displayHelp() {
    Component help = Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.help.title")
        .color(Colours.Title)
        .append(Component.newline())
        .append(Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.help.info")
          .color(Colours.Primary))
        .append(Component.newline())
        .append(Component.translatable(I18nNamespaces.commandNamespace + "autoVoteCommand.help.disclaimer")
            .color(Colours.Secondary));

    this.displayMessage(help);
  }

}

