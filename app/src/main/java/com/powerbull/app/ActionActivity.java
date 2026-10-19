package com.powerbull.app;

import android.app.Activity;
import android.os.*;

public class ActionActivity extends Activity {
  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    String action = getIntent().getStringExtra("action");
    finish();
    if (action != null)
      new Handler(Looper.getMainLooper())
          .postDelayed(() -> Actions.run(getApplicationContext(), action), 180);
  }
}
