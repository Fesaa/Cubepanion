package org.cubepanion.core.utils.eggwarsmaps.base;

import net.labymod.api.client.component.Component;
import org.cubepanion.core.utils.Colours;

public interface EggWarsMap {

  Component getGenLayoutComponent();
  Component getMapLayoutComponent();

  Component getBuildLimitMessage();

  String getPartyMessage();

  String getName();

  void setCurrentTeamColour(String teamColour);

  default Component getTeamFiller(String colour) {
    return Component.text("||||||", Colours.colourToNamedTextColor(colour));
  }

}
