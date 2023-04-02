package org.cubecraftutilities.core;

import net.labymod.api.client.component.Component;
import java.util.function.Function;

public class i18nNamespaces {

  public final static String globalNamespace = "cubecraftutilities";
  public final static String commandNamespace = i18nNamespaces.globalNamespace +  ".commands.";



  public static Function<String, String> commandNameSpaceMaker(String commandName) {
    return (extra) -> i18nNamespaces.commandNamespace + commandName + "." + extra;
  }

  public static Function<String, Component> commandNamespaceTransformer(String commandName) {
    return (extra) -> Component.translatable(i18nNamespaces.commandNamespace + commandName + "." + extra);
  }

}
