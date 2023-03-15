package org.cubecraftutilities.core.listener.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.managers.submanagers.PartyManager;

public class PartyTracker {

  private final CCU addon;
  private final PartyManager partyManager;

  private boolean tryingToReadPartyMembers;
  private int partySize;

  private final String userNameRegex = "([a-zA-Z0-9_]{2,16})";
  private final String joinedParty = "You have joined " + this.userNameRegex + "'s party!";
  private final Pattern ownerChange = Pattern.compile("The owner of the party has been changed to " + this.userNameRegex + "!");
  private final Pattern playerJoinsPartyMessage = Pattern.compile("\\[Party\\] \\[\\+\\] " + this.userNameRegex + " joined the party\\.");
  private final Pattern playerKickedFromPartyMessage = Pattern.compile(this.userNameRegex + " was kicked from the party!");
  private final Pattern playerLeavesPartyMessage = Pattern.compile("\\[Party\\] \\[-\\] " + this.userNameRegex + " left the party\\.");
  private final Pattern partyStatusTop = Pattern.compile("------- Party Status \\((\\d{1,2})\\/\\d{1,2}\\) -------");
  private final Pattern partyOwner = Pattern.compile("Owner: (" + this.userNameRegex + ")");
  private final Pattern partyMember = Pattern.compile(this.userNameRegex + "( - KICK)?");

  public PartyTracker(CCU addon) {
    this.addon = addon;
    this.partyManager = addon.getManager().getPartyManager();

    this.tryingToReadPartyMembers = false;
    this.partySize = 0;
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();
    ClientPlayer p = this.addon.labyAPI().minecraft().getClientPlayer();
    if (p == null) {
      return;
    }

    // Joining
    Matcher playerJoinsPartyMatcher = this.playerJoinsPartyMessage.matcher(msg);
    if (playerJoinsPartyMatcher.matches()) {
      this.partyManager.addPartyMember(playerJoinsPartyMatcher.group(1));

      if (!this.partyManager.isInParty()) {
        this.partyManager.setInParty(true);
        this.partyManager.setPartyOwner(true);

        this.toggleTryingToReadPartyMembers();
      }
      return;
    }

    // Leaving
    Matcher playerKickedFromPartyMatcher = this.playerKickedFromPartyMessage.matcher(msg);
    if (playerKickedFromPartyMatcher.matches()) {
      partyManager.removePartyMember(playerKickedFromPartyMatcher.group(1));
      return;
    }

    Matcher playerLeavesPartyMatcher = this.playerLeavesPartyMessage.matcher(msg);
    if (playerLeavesPartyMatcher.matches()) {
      partyManager.removePartyMember(playerLeavesPartyMatcher.group(1));
    }

    // Personal
    if (msg.matches(this.joinedParty)) {
      this.partyManager.setInParty(true);

      this.toggleTryingToReadPartyMembers();
      return;
    }

    String leftParty = "You have left your party!";
    String kickedFromParty = "You were kicked from your party!";
    String partyDisband = "The party has been disbanded!";
    if (msg.matches(leftParty)
    ||  msg.matches(partyDisband)
    ||  msg.matches(kickedFromParty)) {
      this.partyManager.setEmptyParty();
      return;
    }

    if (this.tryingToReadPartyMembers) {
      Matcher partyStatusTopMatcher = this.partyStatusTop.matcher(msg);
      if (partyStatusTopMatcher.matches()) {
        this.partySize = Integer.parseInt(partyStatusTopMatcher.group(1));
        e.setCancelled(true);
        return;
      }

      Matcher partyOwnerMatcher = this.partyOwner.matcher(msg);
      if (partyOwnerMatcher.matches()) {
        this.partyManager.addPartyMember(partyOwnerMatcher.group(1));
        this.partySize--;
        e.setCancelled(true);
        return;
      }

      String membersMessage = "Members:";
      if (msg.matches(membersMessage)) {
        e.setCancelled(true);
        return;
      }

      Matcher partyMemberMessage = this.partyMember.matcher(msg);
      if (partyMemberMessage.matches()) {
        this.partyManager.addPartyMember(partyMemberMessage.group(1));
        this.partySize--;
        e.setCancelled(true);

        if (this.partySize == 0) {
          this.tryingToReadPartyMembers = false;
          this.addon.logger().info(this.partyManager.getPartyMembers().toString());
        }
        return;
      }
    }

    if (this.partyManager.isInParty()) {
      Matcher ownerChangeMatcher = this.ownerChange.matcher(msg);
      if (ownerChangeMatcher.matches()) {
        this.partyManager.setPartyOwner(ownerChangeMatcher.group(1).equals(p.getName()));
      }
    }
  }

  private void toggleTryingToReadPartyMembers() {
    this.tryingToReadPartyMembers = true;
    this.addon.labyAPI().minecraft().chatExecutor().chat("/party info");
  }

}
