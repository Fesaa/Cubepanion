package org.cubecraftutilities.core.utils;

public class Utils {

  public static String stringJoiner(String[] string, String del) {
    StringBuilder out = new StringBuilder();
    for (String s: string) {
      out.append(del).append(s);
    }
    return out.toString();
  }

}
