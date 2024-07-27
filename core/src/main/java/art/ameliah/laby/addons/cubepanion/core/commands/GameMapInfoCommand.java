package art.ameliah.laby.addons.cubepanion.core.commands;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;

public class GameMapInfoCommand extends Command {

  private final Cubepanion addon;

  public GameMapInfoCommand(Cubepanion addon) {
    super("gamemap", "gm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CubeGame game = this.addon.getManager().getDivision();
    if (!this.addon.getManager().onCubeCraft() || !GameMapAPI.getInstance().hasMaps(game)) {
      return false;
    }
    CommandSystemSubConfig config = this.addon.configuration().getCommandSystemSubConfig();

    if (!config.getGameMapInfoCommand().get() || !config.getEnabled().get()) {
      return false;
    }

    String mapName;
    if (arguments.length == 0) {
      mapName = this.addon.getManager().getMapName();
    } else {
      mapName = String.join(" ", arguments);
    }

    boolean check = this.addon.getManager().getGameMapInfoManager()
        .doGameMapLayout(mapName);
    if (!check) {
      this.displayMessage(
          Component.translatable()
              .key(I18nNamespaces.commandNamespace + "GameMapInfoCommand." + "mapNotFound")
              .argument(Component.text(mapName))
              .build()
              .color(Colours.Error)
              .hoverEvent(HoverEvent.showText(
                  GameMapAPI.getInstance().getAllMapNames(game)
                      .color(Colours.Hover)
              )));
    }
    return true;
  }
}
