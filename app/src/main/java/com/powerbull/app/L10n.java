package com.powerbull.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.LocaleList;
import java.util.Locale;

/** Explicit, app-local language selection. Never changes the device's language. */
public final class L10n {
  private L10n() {}

  public static boolean isFa(Context c) {
    return "fa".equals(Prefs.get(c).getString("language", "en"));
  }

  public static Context wrap(Context c) {
    Locale locale = new Locale(isFa(c) ? "fa" : "en");
    Configuration config = new Configuration(c.getResources().getConfiguration());
    config.setLocales(new LocaleList(locale));
    config.setLayoutDirection(locale);
    return c.createConfigurationContext(config);
  }

  public static String t(Context c, int resource) {
    return wrap(c).getString(resource);
  }

  public static void toggle(Context c) {
    Prefs.get(c).edit().putString("language", isFa(c) ? "en" : "fa").apply();
    BullWidget.refreshAll(c);
    Prefs.refresh();
  }
}
