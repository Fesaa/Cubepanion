package art.ameliah.laby.addons.cubepanion.v1_21_5;


import art.ameliah.laby.addons.cubepanion.core.versionlinkers.LeaderboardTrackerLink;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI.LeaderboardRow;
import com.mojang.authlib.properties.Property;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.Nullable;

@Singleton
@Implements(LeaderboardTrackerLink.class)
public class VersionedLeaderboardTrackerLink extends LeaderboardTrackerLink {

  @Inject
  public VersionedLeaderboardTrackerLink() {
  }

  @Override
  public void onScreenOpen() {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) {
      return;
    }
    VersionedLeaderboardTrackerLink lbTracker = this;

    Timer timer = new Timer("onScreenOpen");
    timer.schedule(new TimerTask() {

      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          lbTracker.currentLeaderboard = null;
          lbTracker.recordedPageNumbers.clear();
          lbTracker.currentPageNumber = -1;
          lbTracker.maxPageNumber = -1;
          lbTracker.cachedEntries.clear();
          timer.cancel();
        }
        count++;
        Screen currenScreen = minecraft.screen;
        if (!(currenScreen instanceof ContainerScreen)) {
          return;
        }
        Component title = currenScreen.getTitle();
        if (!title.toString().contains("Leaderboard") && !title.toString().contains("Season")) {
          return;
        }

        List<Component> titleSiblings = title.getSiblings();
        if (titleSiblings.size() > 1) {
          titleSiblings = titleSiblings.get(0).getSiblings();
          if (titleSiblings.size() > 1) {
            lbTracker.currentLeaderboard = lbTracker.titelStringToLeaderboard(
                titleSiblings.get(1).getString());
          }
          String pageNumberString = title.getSiblings().get(1).getString().trim();
          String[] pageNumbers = pageNumberString.replace("(", "").replace(")", "").split("/");
          try {
            lbTracker.currentPageNumber = Integer.parseInt(pageNumbers[0]);
            lbTracker.maxPageNumber = Integer.parseInt(pageNumbers[1]);
          } catch (NumberFormatException e) {
            return;
          }
        }

        if (lbTracker.currentLeaderboard == null
            || lbTracker.currentPageNumber == -1
            || lbTracker.maxPageNumber == -1) {
          return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          for (ItemStack itemStack : menu.getItems()) {
            if (!itemStack.getItem().toString().equals("minecraft:player_head")) {
              continue;
            }

            String name;
            int position = -1;
            int score = -1;

            List<Component> hoverText = itemStack.getTooltipLines(
                TooltipContext.of(Minecraft.getInstance().level),
                player, TooltipFlag.NORMAL);
            if (hoverText.size() < 3) {
              continue;
            }
            List<Component> nameSiblings = hoverText.get(0).getSiblings();
            if (!nameSiblings.isEmpty()) {
              nameSiblings = nameSiblings.get(0).getSiblings();
              if (!nameSiblings.isEmpty()) {
                nameSiblings = nameSiblings.get(0).getSiblings();
                if (!nameSiblings.isEmpty()) {
                  name = nameSiblings.get(0).getString();
                } else {
                  name = null;
                }
              } else {
                name = null;
              }
            } else {
              name = null;
            }

            List<Component> positionSiblings = hoverText.get(1).getSiblings();
            if (!positionSiblings.isEmpty()) {
              positionSiblings = positionSiblings.get(0).getSiblings();
              if (!positionSiblings.isEmpty()) {
                String positionString = positionSiblings.get(0).getString().replaceAll("\\D+", "");
                try {
                  position = Integer.parseInt(positionString);
                } catch (NumberFormatException e) {
                  continue;
                }
              }
            }

            List<Component> scoreSiblings = hoverText.get(2).getSiblings();
            if (!scoreSiblings.isEmpty()) {
              scoreSiblings = scoreSiblings.get(0).getSiblings();
              if (scoreSiblings.size() > 1) {
                String scoreString = scoreSiblings.get(1).getString();
                try {
                  score = Integer.parseInt(scoreString);
                } catch (NumberFormatException e) {
                  continue;
                }

              }
            }

            if (name == null
                || position == -1
                || score == -1) {
              continue;
            }

            lbTracker.addLeaderboardEntry(new LeaderboardRow(
                lbTracker.currentLeaderboard,
                name,
                position,
                score,
                texture(itemStack.get(DataComponents.PROFILE)),
                0
            ));
          }
          lbTracker.recordedPageNumbers.add(lbTracker.currentPageNumber);
          timer.cancel();

          if (lbTracker.currentPageNumber == lbTracker.maxPageNumber
              && lbTracker.recordedPageNumbers.size() == lbTracker.maxPageNumber
              && lbTracker.cachedEntries.size() == 200) {
            lbTracker.submitToApi();
          }
        }
      }
    }, 100, 100);
  }

  @Nullable
  private String texture(ResolvableProfile profile) {
    if (profile == null) {
      return null;
    }
    Property p = profile.properties()
        .get("textures")
        .stream()
        .findFirst()
        .orElse(null);
    return p == null ? null : p.value();
  }
}
