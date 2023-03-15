package org.cubecraftutilities.core.managers.submanagers;

import java.util.ArrayList;
import java.util.List;

public class PartyManager {

  private List<String> partyMembers;
  private boolean inParty;
  private boolean isPartyOwner;

  public PartyManager() {
    this.partyMembers = new ArrayList<>();
    this.inParty = false;
    this.isPartyOwner = false;
  }

  public void reset() {
    this.partyMembers = new ArrayList<>();
    this.inParty = false;
    this.isPartyOwner = false;
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

}
