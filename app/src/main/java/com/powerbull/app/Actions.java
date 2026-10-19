package com.powerbull.app;

import android.accessibilityservice.AccessibilityService;
import android.content.*;
import android.media.AudioManager;
import android.widget.Toast;

public final class Actions {
  public static final String[] IDS = {"lock", "power", "up", "down", "home", "back", "recents"};
  public static final String[] NAMES = {
    "Lock screen", "Power menu", "Volume up", "Volume down", "Home", "Back", "Recent apps"
  };

  public static String label(String a) {
    for (int i = 0; i < IDS.length; i++) if (IDS[i].equals(a)) return NAMES[i];
    return "PowerBull";
  }

  public static String label(Context c, String a) {
    int[] labels = {
      R.string.pb_081,
      R.string.pb_082,
      R.string.pb_083,
      R.string.pb_084,
      R.string.pb_085,
      R.string.pb_086,
      R.string.pb_087
    };
    for (int i = 0; i < IDS.length; i++) if (IDS[i].equals(a)) return L10n.t(c, labels[i]);
    return "PowerBull";
  }

  public static String[] names(Context c) {
    String[] out = new String[IDS.length];
    for (int i = 0; i < out.length; i++) out[i] = label(c, IDS[i]);
    return out;
  }

  public static int icon(String a) {
    switch (a) {
      case "lock":
        return R.drawable.ic_lock;
      case "up":
        return R.drawable.ic_up;
      case "down":
        return R.drawable.ic_down;
      case "home":
        return R.drawable.ic_home;
      case "back":
        return R.drawable.ic_back;
      case "recents":
        return R.drawable.ic_recents;
      default:
        return R.drawable.ic_power;
    }
  }

  public static void run(Context c, String a) {
    try {
      if (a.equals("up") || a.equals("down")) {
        ((AudioManager) c.getSystemService(Context.AUDIO_SERVICE))
            .adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                a.equals("up") ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_SHOW_UI);
        return;
      }
      BullService s = BullService.live;
      if (s == null) {
        Toast.makeText(c, L10n.t(c, R.string.pb_088), Toast.LENGTH_LONG).show();
        c.startActivity(new Intent(c, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return;
      }
      int id;
      switch (a) {
        case "lock":
          id = AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN;
          break;
        case "power":
          id = AccessibilityService.GLOBAL_ACTION_POWER_DIALOG;
          break;
        case "home":
          id = AccessibilityService.GLOBAL_ACTION_HOME;
          break;
        case "back":
          id = AccessibilityService.GLOBAL_ACTION_BACK;
          break;
        case "recents":
          id = AccessibilityService.GLOBAL_ACTION_RECENTS;
          break;
        default:
          return;
      }
      if (!s.performGlobalAction(id))
        Toast.makeText(c, L10n.t(c, R.string.pb_089), Toast.LENGTH_SHORT).show();
    } catch (SecurityException e) {
      Toast.makeText(c, L10n.t(c, R.string.pb_090), Toast.LENGTH_LONG).show();
    }
  }
}
