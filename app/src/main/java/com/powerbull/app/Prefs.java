package com.powerbull.app;

import android.content.*;
import java.util.*;

public final class Prefs {
  public static SharedPreferences get(Context c) {
    return c.getSharedPreferences("powerbull", 0);
  }

  public static String[] selected(Context c) {
    String s = get(c).getString("actions", "lock,up,down,power,home,back,recents");
    return s.isEmpty() ? new String[] {"lock"} : s.split(",");
  }

  public static boolean has(Context c, String a) {
    return Arrays.asList(selected(c)).contains(a);
  }

  public static void toggle(Context c, String a, boolean on) {
    ArrayList<String> list = new ArrayList<>(Arrays.asList(selected(c)));
    if (on && !list.contains(a)) list.add(a);
    if (!on) list.remove(a);
    if (list.isEmpty()) list.add("lock");
    get(c).edit().putString("actions", String.join(",", list)).apply();
    refresh();
  }

  public static void refresh() {
    if (BullService.live != null) BullService.live.refresh();
  }
}
