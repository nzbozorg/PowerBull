package com.powerbull.app;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.Bundle;
import android.widget.RemoteViews;

public class BullWidget extends AppWidgetProvider {
  public static void refreshAll(Context c) {
    AppWidgetManager m = AppWidgetManager.getInstance(c);
    for (int id : m.getAppWidgetIds(new ComponentName(c, BullWidget.class))) update(c, m, id);
  }

  @Override
  public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
    for (int id : ids) update(c, m, id);
  }

  @Override
  public void onAppWidgetOptionsChanged(Context c, AppWidgetManager m, int id, Bundle options) {
    update(c, m, id);
  }

  static void update(Context c, AppWidgetManager m, int id) {
    String saved = Prefs.get(c).getString("widget_" + id, null);
    if (saved == null) {
      saved = m.getAppWidgetOptions(id).getString("powerbull_action");
      if (saved == null) saved = Prefs.get(c).getString("pending_widget", "lock");
      Prefs.get(c).edit().putString("widget_" + id, saved).apply();
    }
    String action = saved;
    RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget);
    v.setImageViewResource(R.id.widget_button, Actions.icon(action));
    v.setContentDescription(R.id.widget_button, Actions.label(c, action));
    Intent i =
        new Intent(c, ActionActivity.class)
            .putExtra("action", action)
            .setData(android.net.Uri.parse("powerbull://widget/" + id));
    v.setOnClickPendingIntent(
        R.id.widget_button,
        PendingIntent.getActivity(
            c, id, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
    m.updateAppWidget(id, v);
  }

  @Override
  public void onDeleted(Context c, int[] ids) {
    for (int id : ids) Prefs.get(c).edit().remove("widget_" + id).apply();
  }

  @Override
  public void onRestored(Context c, int[] oldIds, int[] newIds) {
    for (int i = 0; i < oldIds.length; i++) {
      String action = Prefs.get(c).getString("widget_" + oldIds[i], "lock");
      Prefs.get(c)
          .edit()
          .putString("widget_" + newIds[i], action)
          .remove("widget_" + oldIds[i])
          .apply();
    }
  }
}
