package org.ccu.core.utils.imp.base;

import net.kyori.adventure.text.Component;

public interface EggWarsMap {

  Component getGenLayoutComponent();
  Component getMapLayoutComponent();

  Component getBuildLimitMessage();

  String getPartyMessage();

  void setCurrentTeamColour(String teamColour);

}
