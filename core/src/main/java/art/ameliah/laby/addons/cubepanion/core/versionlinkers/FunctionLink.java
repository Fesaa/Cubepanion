package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.weave.APIGame;
import art.ameliah.laby.addons.cubepanion.core.weave.GamesAPI;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class FunctionLink {

  protected List<Integer> perkSlots = List.of(20, 22, 24);
  protected List<Integer> perkCategorySlots = List.of(0, 1, 2);

  public abstract void setCoolDown(@NotNull ItemStack itemStack, int duration);

  public abstract CompletableFuture<Pair<PerkCategory, List<ItemStack>>> loadPerks();

  public abstract CompletableFuture<@Nullable HashMap<APIGame, Integer>> loadPlayerCounts();

  public void readPlayerCount(HashMap<APIGame, Integer> games,  ItemStack item) {
    if (item.getDisplayName().getChildren().isEmpty() || item.getDisplayName().getChildren().getFirst().getChildren().isEmpty()) {
      return;
    }

    String name = ((TextComponent)item.getDisplayName().getChildren().getFirst().getChildren().getFirst()).getText();
    APIGame game = GamesAPI.I().getGame(name.toLowerCase().replace(" ", "_"));
    if (game == null) {
      return;
    }

    List<String> toolTips = ((CCItemStack) item).getToolTips();
    if (toolTips.size() < 2) {
      return;
    }

    for (String content : toolTips) {
      if (content.contains("Players: ")) {
        String playerCountString = content.replace("Players: ", "");
        try {
          int playerCount = Integer.parseInt(playerCountString);
          games.put(game, playerCount);
          break;
        } catch (NumberFormatException e) {
          LOGGER.warn(getClass(), e,
              "Failed to parse playercount from item in menu: " + playerCountString);
        }
      }
    }
  }

}
