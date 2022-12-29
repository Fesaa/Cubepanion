package org.ccu.core.listener;

import com.google.inject.Inject;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.TextComponent;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamUpdateEvent;
import org.ccu.core.CCU;
import org.ccu.core.config.internal.CCUinternalConfig;
import org.ccu.core.config.subconfig.EndGameSubConfig;
import org.ccu.core.utils.AutoVote;
import org.ccu.core.utils.EggWarsMapInfo;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatReceiveEventListener {

  private final CCU addon;
  private final Pattern playerElimination = Pattern.compile("([a-zA-Z0-9_]{2,16}) has been eliminated from the game.");

  @Inject
  public ChatReceiveEventListener(CCU addon) {this.addon = addon;}

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent chatReceiveEvent) {
    String msg = chatReceiveEvent.chatMessage().getPlainText();

    // Auto GG
    EndGameSubConfig config = this.addon.configuration().getEndGameSubConfig();
    if (config.isEnabled().get() && !CCUinternalConfig.hasSaidGG) {
      String eliminationMessage = this.addon.labyAPI().minecraft().clientPlayer().getName() + " has been eliminated from the game.";
      if (msg.equals("Congratulations, you win!") || (msg.equals(eliminationMessage) && config.getOnElimination().get())) {
        this.addon.labyAPI().minecraft().chatExecutor().chat(config.getGameEndMessage().get().msg, false);
        if (!config.getCustomMessage().isDefaultValue()) {
          this.addon.labyAPI().minecraft().chatExecutor().chat(config.getCustomMessage().get(), false);
        }
        CCUinternalConfig.hasSaidGG = true;
      }
    }

    // Friend Message Sound
    if (this.addon.configuration().friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me: .*\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me: .*")) {
        this.addon.labyAPI().minecraft().sounds().playSound(
            ResourceLocation.create("minecraft", "entity.experience_orb.pickup"), 1000, 1);
      }
    }

    // Start of game
    if (msg.equals("Let the games begin!")) {
      CCUinternalConfig.inPreLobby = false;
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() -> {
        if (this.addon.configuration().getEggWarsMapInfoSubConfig().isEnabled().get()) {
          CCUinternalConfig.updateTeamColour(this.addon);
          EggWarsMapInfo.eggWarsMapInfo(this.addon);
        }
        if (this.addon.configuration().getAutoVoteSubConfig().isEnabled()) {
          //AutoVote.vote(this.addon);
          this.addon.logger().info("Tried to auto vote");
        }
        this.addon.rpcManager.startOfGame();
        this.addon.rpcManager.updateRPC();
      },100, TimeUnit.MILLISECONDS);
    }

    // RPC
    Matcher matcher = playerElimination.matcher(msg);
    if (matcher.matches()) {
      this.addon.rpcManager.registerDeath(matcher.group(1));
    }

    // Party Tracker
    if (msg.matches("You have joined [a-zA-Z0-9_]{2,16}'s party!")) {
      CCUinternalConfig.partyStatus = true;
    }
    if (msg.matches("You have left your party!")
        || msg.matches("You were kicked from your party!")
        || msg.matches("The party has been disbanded!")) {
      CCUinternalConfig.partyStatus = false;
    }

    if (msg.matches("[a-zA-Z0-9_]{2,16} joined the party!")) {
      CCUinternalConfig.partyStatus = true;
    }
  }
}
