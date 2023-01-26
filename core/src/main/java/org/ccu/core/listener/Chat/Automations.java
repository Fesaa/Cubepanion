package org.ccu.core.listener.Chat;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.ccu.core.CCU;
import org.ccu.core.config.CCUManager;
import org.ccu.core.config.subconfig.EndGameSubConfig;

public class Automations {

  private final CCU addon;
  private final CCUManager manager;

  private final Pattern playerElimination = Pattern.compile("([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");
  private final Pattern EggWarsTeamJoin = Pattern.compile("You have joined .{1,30} team\\.");

  public Automations(CCU addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();
    ClientPlayer p = this.addon.labyAPI().minecraft().clientPlayer();
    if (p == null) {
      return;
    }

    // Friend Message Sound
    if (this.addon.configuration().friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me: .*")) {
        this.addon.labyAPI().minecraft().sounds().playSound(
            ResourceLocation.create("minecraft", "entity.experience_orb.pickup"), 1000, 1);
        return;
      }
    }

    // Start of game
    if (msg.equals("Let the games begin!")) {
      this.manager.setInPreLobby(false);
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() -> {
        if (this.addon.configuration().getEggWarsMapInfoSubConfig().isEnabled().get()) {
          this.manager.updateTeamColour();
          this.manager.getEggWarsMapInfoManager().doEggWarsMapLayout();
        }
        this.addon.rpcManager.startOfGame();
        this.addon.rpcManager.updateRPC();
      },1000, TimeUnit.MILLISECONDS);
      return;
    }

    // RPC
    Matcher matcher = playerElimination.matcher(msg);
    if (matcher.matches()) {
      this.addon.rpcManager.registerDeath(matcher.group(1));
    }

    // Auto GG
    EndGameSubConfig config = this.addon.configuration().getEndGameSubConfig();
    if (config.isEnabled().get() && !manager.isEliminated()) {
      String eliminationMessage = this.addon.labyAPI().minecraft().clientPlayer().getName() + " has been eliminated from the game.";
      if (msg.equals("Congratulations, you win!") || (msg.equals(eliminationMessage) && config.getOnElimination().get())) {
        this.addon.labyAPI().minecraft().chatExecutor().chat(config.getGameEndMessage().get().msg, false);
        if (!config.getCustomMessage().isDefaultValue()) {
          this.addon.labyAPI().minecraft().chatExecutor().chat(config.getCustomMessage().get(), false);
        }
        manager.setEliminated(true);
        return;
      }
    }

    // Spawn protection countdown
    if (msg.equals("You are now invincible for 10 seconds.")) {
      this.addon.getManager().getSpawnProtectionManager().getClientPlayerSpawnProtection().registerDeath();
      return;
    }

    // TeamColour Tracker
    Matcher teamColourMatcher = this.EggWarsTeamJoin.matcher(msg);
    if (teamColourMatcher.matches()) {
      TextColor colour = e.chatMessage().component().getChildren().get(0).getChildren().get(1)
          .getColor();
      if (colour == null) {
        this.manager.setTeamColour("yellow");
      } else {
        this.manager.setTeamColour(colour.toString());
      }
    }
  }

}
