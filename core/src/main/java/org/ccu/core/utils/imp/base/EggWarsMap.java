package org.ccu.core.utils.imp.base;

import net.labymod.api.client.component.Component;

public interface EggWarsMap {

  Component getGenLayoutComponent();
  Component getMapLayoutComponent();

  Component getBuildLimitMessage();

  String getPartyMessage();

  String getName();

  void setCurrentTeamColour(String teamColour);

}
