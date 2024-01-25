package art.ameliah.laby.addons.cubepanion.core.managers.submanagers;

import java.util.ArrayList;
import java.util.List;
import art.ameliah.laby.addons.cubepanion.core.managers.Manager;

public class PartyManager implements Manager {

  private List<String> partyMembers;
  private boolean inParty;
  private boolean isPartyOwner;
  private boolean partyChat;

  public PartyManager() {
    this.partyMembers = new ArrayList<>();
    this.inParty = false;
    this.isPartyOwner = false;
    this.partyChat = false;
  }

  public void reset() {
    this.partyMembers = new ArrayList<>();
    this.inParty = false;
    this.isPartyOwner = false;
    this.partyChat = false;
  }

  public boolean isPartyOwner() {
    return isPartyOwner;
  }

  public void setPartyOwner(boolean partyOwner) {
    isPartyOwner = partyOwner;
  }

  public void addPartyMember(String userName) {
    if (!this.partyMembers.contains(userName)) {
      this.partyMembers.add(userName);
    }
  }

  public void removePartyMember(String userName) {
    this.partyMembers.remove(userName);
  }

  public void setEmptyParty() {
    this.partyMembers = new ArrayList<>();
    this.inParty = false;
    this.partyChat = false;
  }

  public boolean isMemberInParty(String username) {
    return this.partyMembers.contains(username);
  }

  public List<String> getPartyMembers() {
    return this.partyMembers;
  }

  public boolean isInParty() {
    return inParty;
  }

  public void setInParty(boolean inParty) {
    this.inParty = inParty;
  }

  public boolean isPartyChat() {
    return partyChat;
  }

  public void setPartyChat(boolean partyChat) {
    this.partyChat = partyChat;
  }
}
