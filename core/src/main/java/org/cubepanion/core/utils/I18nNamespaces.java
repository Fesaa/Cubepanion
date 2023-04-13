package org.cubepanion.core.utils;

import java.util.function.Function;
import net.labymod.api.client.component.Component;

public class I18nNamespaces {

  public final static String globalNamespace = "cubecraftutilities";
  public final static String commandNamespace = I18nNamespaces.globalNamespace +  ".commands.";
  public final static String managerNameSpace = I18nNamespaces.globalNamespace + ".managers.";



  public static Function<String, String> commandNameSpaceMaker(String commandName) {
    return (extra) -> I18nNamespaces.commandNamespace + commandName + "." + extra;
  }

  public static Function<String, Component> commandNamespaceTransformer(String commandName) {
    return (extra) -> Component.translatable(I18nNamespaces.commandNamespace + commandName + "." + extra);
  }

  public static Function<String, String> managerNameSpaceMaker(String managerName) {
    return (extra) -> I18nNamespaces.managerNameSpace + managerName + "." + extra;
  }

  public static Function<String, Component> managersNamespaceTransformer(String managerName) {
    return (extra) -> Component.translatable(I18nNamespaces.managerNameSpace + managerName + "." + extra);
  }

}
