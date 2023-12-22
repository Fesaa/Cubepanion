package org.cubepanion.core.listener.games;

import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.QOLConfig;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.utils.Colours;
import java.util.regex.Pattern;

public class VoteReminder {

  private final Cubepanion addon;
  private final QOLConfig config;
  private final Pattern fiveSecondsRemaining = Pattern.compile("[a-zA-Z ]{0,30} is starting in 5 seconds\\.");
  private final Component voteReminderComponent = Component.translatable("cubepanion.messages.voteReminder", Colours.Primary);

  public VoteReminder(Cubepanion addon) {
    this.addon = addon;
    this.config = addon.configuration().getQolConfig();
  }

  private boolean hasVoted = false;

  @Subscribe
  public void onGameUpdate(GameUpdateEvent e) {
    if (!e.isSwitch()) {
      return;
    }

    if (e.isPreLobby()) {
      hasVoted = false;
    }
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    if (!addon.getManager().onCubeCraft()) {
      return;
    }
    Minecraft minecraft = this.addon.labyAPI().minecraft();
    ClientPlayer p = minecraft.getClientPlayer();
    if (p == null) {
      return;
    }
    String playerRegex = ".{0,5}" + p.getName() + ".{0,5}";
    String msg = e.chatMessage().getPlainText();
    String voteMessage = playerRegex + " voted for .*\\. \\d{1,4} votes?";

    if (msg.matches(voteMessage)) {
      hasVoted = true;
      return;
    }

    if (!fiveSecondsRemaining.matcher(msg).matches() || hasVoted) {
      return;
    }

    if (!config.getReminderToVote().get()) {
      return;
    }

    minecraft.sounds().playSound(config.getVoteReminderResourceLocation(), 100, 1);
    minecraft.chatExecutor().displayClientMessage(this.voteReminderComponent);
  }

}
