package org.cubepanion.v1_19_3;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.cubepanion.core.utils.LeaderboardEntry;
import org.cubepanion.core.versionlinkers.LeaderboardTrackerLink;

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
            if (!itemStack.getItem().toString().equals("player_head")) {
              continue;
            }

            String name = null;
            int position = -1;
            int score = -1;

            List<Component> hoverText = itemStack.getTooltipLines(player, TooltipFlag.NORMAL);
            if (hoverText.size() < 3) {
              continue;
            }
            List<Component> nameSiblings = hoverText.get(0).getSiblings();
            if (nameSiblings.size() > 0) {
              nameSiblings = nameSiblings.get(0).getSiblings();
              if (nameSiblings.size() > 0) {
                nameSiblings = nameSiblings.get(0).getSiblings();
                if (nameSiblings.size() > 0) {
                  name = nameSiblings.get(0).getString();
                }
              }
            }

            List<Component> positionSiblings = hoverText.get(1).getSiblings();
            if (positionSiblings.size() > 0) {
              positionSiblings = positionSiblings.get(0).getSiblings();
              if (positionSiblings.size() > 0) {
                String positionString = positionSiblings.get(0).getString().replaceAll("\\D+", "");
                try {
                  position = Integer.parseInt(positionString);
                } catch (NumberFormatException e) {
                  continue;
                }
              }
            }

            List<Component> scoreSiblings = hoverText.get(2).getSiblings();
            if (scoreSiblings.size() > 0) {
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

            lbTracker.addLeaderboardEntry(new LeaderboardEntry(
                lbTracker.currentLeaderboard,
                name,
                position,
                score
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
}
