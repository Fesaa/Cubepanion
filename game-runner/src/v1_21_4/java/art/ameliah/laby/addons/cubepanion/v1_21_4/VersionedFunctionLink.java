package art.ameliah.laby.addons.cubepanion.v1_21_4;

import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.labymod.api.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
@Implements(FunctionLink.class)
public class VersionedFunctionLink extends FunctionLink {

  @Override
  public void setCoolDown(@NotNull net.labymod.api.client.world.item.ItemStack itemStack,
      int duration) {
    if (Minecraft.getInstance().player == null) {
      return;
    }
    ItemStack mcItem = (ItemStack) (Object) itemStack;
    if (mcItem == null) {
      return;
    }
    Minecraft.getInstance().player.getCooldowns().addCooldown(mcItem, duration);
  }

  @Override
  public CompletableFuture<Pair<PerkCategory, List<net.labymod.api.client.world.item.ItemStack>>> loadPerks() {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) {
      return CompletableFuture.completedFuture(null);
    }

    CompletableFuture<Pair<PerkCategory, List<net.labymod.api.client.world.item.ItemStack>>> future = new CompletableFuture<>();

    Timer timer = new Timer("loadPerks#onScreenOpen");
    timer.schedule(new TimerTask() {
      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          timer.cancel();
        }
        count++;
        Screen currenScreen = minecraft.screen;
        if (!(currenScreen instanceof ContainerScreen)) {
          return;
        }
        Component title = currenScreen.getTitle();
        if (!title.toString().contains("Perk Shop")) {
          return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if ((!(menu instanceof ChestMenu))) {
          return;
        }

        PerkCategory category = null;
        for (int i : perkCategorySlots) {
          Slot slot = menu.getSlot(i);
          if (!slot.hasItem()) {
            continue;
          }
          ItemStack itemStack = slot.getItem();
          var data = itemStack.get(DataComponents.CUSTOM_DATA);
          if (data == null) {
            continue;
          }

          String var = data.copyTag().getString("cubetap:item_variant");
          if (!var.endsWith("_selected")) {
            continue;
          }

          category = PerkCategory.fromCubeTapItemVariant(var.replace("_selected", ""));
          if (category != null) {
            break;
          }
        }

        if (category == null) {
          return;
        }

        List<net.labymod.api.client.world.item.ItemStack> perks = new ArrayList<>();
        for (int i : perkSlots) {
          Slot slot = menu.getSlot(i);
          if (slot.hasItem()) {
            perks.add((net.labymod.api.client.world.item.ItemStack) (Object) slot.getItem());
          }
        }

        future.complete(Pair.of(category, perks));
        timer.cancel();
      }

    }, 100, 100);
    return future;
  }

  @Override
  public CompletableFuture<@Nullable HashMap<CubeGame, Integer>> loadPlayerCounts() {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) {
      return CompletableFuture.completedFuture(null);
    }

    CompletableFuture<HashMap<CubeGame, Integer>> future = new CompletableFuture<>();
    Timer timer = new Timer("loadPlayerCounts#onScreenOpen");
    timer.schedule(new TimerTask() {
      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          timer.cancel();
        }
        count++;
        Screen currenScreen = minecraft.screen;
        if (!(currenScreen instanceof ContainerScreen)) {
          return;
        }
        Component title = currenScreen.getTitle();
        if (!title.toString().contains("Games")) {
          return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if ((!(menu instanceof ChestMenu))) {
          return;
        }

        HashMap<CubeGame, Integer> games = new HashMap<>();
        menu.getItems().forEach(item -> {
            if (item.getHoverName().getSiblings().isEmpty()) {
              return;
            }
            CubeGame game = CubeGame.stringToGame(item.getHoverName().getSiblings().getFirst().getString());
            if (game.equals(CubeGame.NONE)) {
              return;
            }
            List<Component> lines = item.getTooltipLines(TooltipContext.of(Minecraft.getInstance().level), player, TooltipFlag.NORMAL);
            if (lines.size() < 2) {
              return;
            }

            for (Component line : lines) {
              String content = line.getString();
              if (content.contains("Players: ")) {
                String playerCountString = content.replace("Players: ", "");
                try {
                  int playerCount = Integer.parseInt(playerCountString);
                  games.put(game, playerCount);
                  break;
                } catch (NumberFormatException e) {
                  LOGGER.error(getClass(), e,
                      "Failed to parse playercount from scoreboard: " + playerCountString);
                }
              }
            }
        });
        future.complete(games);
        timer.cancel();
      }
    }, 100, 100);
    return future;
  }
}
