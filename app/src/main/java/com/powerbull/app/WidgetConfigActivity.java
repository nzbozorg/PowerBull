package com.powerbull.app;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;

public class WidgetConfigActivity extends Activity {
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(L10n.wrap(base));
  }

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    setResult(RESULT_CANCELED);
    int id =
        getIntent()
            .getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    AppWidgetManager manager = AppWidgetManager.getInstance(this);
    AppWidgetProviderInfo info = manager.getAppWidgetInfo(id);
    if (id == AppWidgetManager.INVALID_APPWIDGET_ID
        || info == null
        || !new ComponentName(this, BullWidget.class).equals(info.provider)) {
      finish();
      return;
    }
    new AlertDialog.Builder(this)
        .setTitle(L10n.t(this, R.string.pb_091))
        .setItems(
            Actions.names(this),
            (dialog, index) -> {
              Prefs.get(this).edit().putString("widget_" + id, Actions.IDS[index]).apply();
              BullWidget.update(this, manager, id);
              setResult(RESULT_OK, new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id));
              finish();
            })
        .setOnCancelListener(v -> finish())
        .show();
  }
}
