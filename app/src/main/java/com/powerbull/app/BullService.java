package com.powerbull.app;

import android.accessibilityservice.AccessibilityService;
import android.animation.ValueAnimator;
import android.content.*;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.view.accessibility.AccessibilityEvent;
import android.widget.*;

public class BullService extends AccessibilityService {
  public static BullService live;
  private WindowManager wm;
  private View bubble, panel;
  private WindowManager.LayoutParams bp, pp;
  private final Handler handler = new Handler(Looper.getMainLooper());
  private ValueAnimator snap;
  private boolean dragging = false;
  private Runnable fade;

  private int d(float n) {
    return Math.round(n * getResources().getDisplayMetrics().density);
  }

  @Override
  protected void onServiceConnected() {
    live = this;
    wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    refresh();
  }

  @Override
  public void onAccessibilityEvent(
      AccessibilityEvent event) {} // No event subscription or screen-content access.

  @Override
  public void onInterrupt() {}

  @Override
  public void onDestroy() {
    removeAll();
    if (live == this) live = null;
    super.onDestroy();
  }

  @Override
  public boolean onUnbind(Intent i) {
    removeAll();
    live = null;
    return super.onUnbind(i);
  }

  public void refresh() {
    handler.post(
        () -> {
          removeAll();
          if (Prefs.get(this).getBoolean("floating", false) && Settings.canDrawOverlays(this))
            showBubble();
        });
  }

  private GradientDrawable bg(int color, int radius) {
    GradientDrawable g = new GradientDrawable();
    g.setColor(color);
    g.setCornerRadius(d(radius));
    g.setStroke(d(1), 0xFF35496A);
    return g;
  }

  private int screenW() {
    return getResources().getDisplayMetrics().widthPixels;
  }

  private int screenH() {
    return getResources().getDisplayMetrics().heightPixels;
  }

  private WindowManager.LayoutParams params(int w, int h) {
    WindowManager.LayoutParams p =
        new WindowManager.LayoutParams(
            w,
            h,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT);
    p.gravity = Gravity.TOP | Gravity.LEFT;
    p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
    return p;
  }

  private void showBubble() {
    int size = d(Prefs.get(this).getInt("size", 58));
    ImageButton b = new ImageButton(this);
    b.setImageResource(R.drawable.ic_bull);
    b.setScaleType(ImageView.ScaleType.FIT_CENTER);
    b.setPadding(d(13), d(13), d(13), d(13));
    b.setContentDescription(L10n.t(this, R.string.pb_078));
    b.setBackground(bg(0xFF182642, 100));
    b.setElevation(d(10));
    bp = params(size, size);
    bp.x = Prefs.get(this).getBoolean("right", true) ? screenW() - size - d(8) : d(8);
    bp.y =
        Math.max(
            d(24), Math.min(Prefs.get(this).getInt("y", screenH() / 3), screenH() - size - d(80)));
    bubble = b;
    b.setOnClickListener(
        v -> {
          haptic(v);
          if (panel == null) showPanel();
          else closePanel();
        });
    b.setOnLongClickListener(
        v -> {
          if (!dragging) {
            startActivity(
                new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            closePanel();
            return true;
          }
          return false;
        });
    b.setOnTouchListener(
        new View.OnTouchListener() {
          float sx, sy;
          int ox, oy;
          boolean moved;

          public boolean onTouch(View v, android.view.MotionEvent e) {
            switch (e.getActionMasked()) {
              case MotionEvent.ACTION_DOWN:
                if (snap != null) snap.cancel();
                handler.removeCallbacksAndMessages(null);
                v.animate().alpha(opacity()).setDuration(100).start();
                sx = e.getRawX();
                sy = e.getRawY();
                ox = bp.x;
                oy = bp.y;
                moved = false;
                dragging = false;
                break;
              case MotionEvent.ACTION_MOVE:
                float dx = e.getRawX() - sx, dy = e.getRawY() - sy;
                if (Math.hypot(dx, dy) > d(8)) {
                  moved = true;
                  dragging = true;
                  closePanel();
                }
                if (moved) {
                  bp.x = Math.max(0, Math.min(screenW() - size, ox + (int) dx));
                  bp.y = Math.max(d(20), Math.min(screenH() - size - d(48), oy + (int) dy));
                  updateBubble();
                  return true;
                }
                break;
              case MotionEvent.ACTION_UP:
                if (moved) {
                  v.cancelLongPress();
                  v.setPressed(false);
                  snapToEdge();
                  dragging = false;
                  return true;
                }
                idle();
                break;
              case MotionEvent.ACTION_CANCEL:
                dragging = false;
                snapToEdge();
                break;
            }
            return false;
          }
        });
    try {
      wm.addView(b, bp);
      b.setAlpha(0f);
      b.animate().alpha(opacity()).setDuration(180).start();
      idle();
    } catch (RuntimeException e) {
      bubble = null;
    }
  }

  private float opacity() {
    return Prefs.get(this).getInt("opacity", 90) / 100f;
  }

  private void updateBubble() {
    if (bubble != null && bubble.isAttachedToWindow())
      try {
        wm.updateViewLayout(bubble, bp);
      } catch (RuntimeException ignored) {
      }
  }

  private void snapToEdge() {
    boolean right = bp.x + bp.width / 2 > screenW() / 2;
    int target = right ? screenW() - bp.width - d(8) : d(8);
    snap = ValueAnimator.ofInt(bp.x, target);
    snap.setDuration(220);
    snap.setInterpolator(new android.view.animation.DecelerateInterpolator());
    snap.addUpdateListener(
        a -> {
          bp.x = (Integer) a.getAnimatedValue();
          updateBubble();
        });
    snap.start();
    Prefs.get(this).edit().putBoolean("right", right).putInt("y", bp.y).apply();
    idle();
  }

  private void idle() {
    if (fade != null) handler.removeCallbacks(fade);
    fade =
        () -> {
          if (bubble != null && panel == null && Prefs.get(this).getBoolean("autofade", true))
            bubble.animate().alpha(Math.max(.2f, opacity() * .5f)).setDuration(350).start();
        };
    handler.postDelayed(fade, 3500);
  }

  private void haptic(View v) {
    if (Prefs.get(this).getBoolean("haptic", true))
      v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
  }

  private void showPanel() {
    if (bubble == null) return;
    bubble.animate().alpha(opacity()).setDuration(100).start();
    String[] actions = Prefs.selected(this);
    int cols = Math.min(3, actions.length);
    int cell = d(58), pad = d(10);
    int width = Math.max(2, cols) * cell + 2 * pad;
    LinearLayout box = new LinearLayout(this);
    box.setOrientation(LinearLayout.VERTICAL);
    box.setPadding(pad, pad, pad, pad);
    box.setBackground(bg(0xFF111E38, 26));
    box.setElevation(d(16));
    for (int i = 0; i < actions.length; ) {
      LinearLayout row = new LinearLayout(this);
      row.setGravity(Gravity.CENTER);
      for (int j = 0; j < cols && i < actions.length; j++, i++) {
        String a = actions[i];
        ImageButton b = button(Actions.icon(a), Actions.label(this, a));
        row.addView(b, new LinearLayout.LayoutParams(cell, cell));
        b.setOnClickListener(
            v -> {
              haptic(v);
              if (!a.equals("up") && !a.equals("down")) closePanel();
              Actions.run(this, a);
            });
        if (a.equals("up") || a.equals("down")) repeat(b, a);
      }
      box.addView(row);
    }
    LinearLayout tools = new LinearLayout(this);
    tools.setGravity(Gravity.CENTER);
    ImageButton settings = button(R.drawable.ic_settings, L10n.t(this, R.string.pb_079)),
        close = button(R.drawable.ic_close, L10n.t(this, R.string.pb_080));
    tools.addView(settings, new LinearLayout.LayoutParams(cell, d(48)));
    tools.addView(close, new LinearLayout.LayoutParams(cell, d(48)));
    box.addView(tools);
    settings.setOnClickListener(
        v -> {
          closePanel();
          startActivity(
              new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
    close.setOnClickListener(v -> closePanel());
    int height = ((actions.length + cols - 1) / cols) * cell + 2 * pad + d(48);
    pp = params(width, height);
    boolean right = bp.x + bp.width / 2 > screenW() / 2;
    pp.x =
        right
            ? Math.max(d(8), bp.x - width - d(10))
            : Math.min(screenW() - width - d(8), bp.x + bp.width + d(10));
    pp.y = Math.max(d(24), Math.min(bp.y - height / 3, screenH() - height - d(60)));
    pp.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
    panel = box;
    box.setOnTouchListener(
        (v, e) -> {
          if (e.getAction() == MotionEvent.ACTION_OUTSIDE) {
            closePanel();
            return true;
          }
          return false;
        });
    try {
      wm.addView(box, pp);
      box.setAlpha(0);
      box.setScaleX(.88f);
      box.setScaleY(.88f);
      box.animate()
          .alpha(opacity())
          .scaleX(1)
          .scaleY(1)
          .setDuration(180)
          .setInterpolator(new android.view.animation.DecelerateInterpolator())
          .start();
    } catch (RuntimeException e) {
      panel = null;
    }
  }

  private void repeat(ImageButton button, String action) {
    Handler h = new Handler(Looper.getMainLooper());
    Runnable r =
        new Runnable() {
          public void run() {
            if (button.isPressed() && button.isAttachedToWindow()) {
              Actions.run(BullService.this, action);
              h.postDelayed(this, 140);
            }
          }
        };
    button.setOnLongClickListener(
        v -> {
          h.post(r);
          return true;
        });
    button.setOnTouchListener(
        (v, e) -> {
          if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL)
            h.removeCallbacks(r);
          return false;
        });
  }

  private ImageButton button(int res, String label) {
    ImageButton b = new ImageButton(this);
    b.setImageResource(res);
    b.setContentDescription(label);
    b.setPadding(d(17), d(17), d(17), d(17));
    android.util.TypedValue tv = new android.util.TypedValue();
    getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, tv, true);
    b.setBackgroundResource(tv.resourceId);
    return b;
  }

  private void closePanel() {
    View p = panel;
    panel = null;
    if (p != null)
      try {
        wm.removeView(p);
      } catch (RuntimeException ignored) {
      }
    idle();
  }

  private void removeAll() {
    handler.removeCallbacksAndMessages(null);
    if (snap != null) snap.cancel();
    closePanel();
    handler.removeCallbacksAndMessages(null);
    if (bubble != null)
      try {
        wm.removeView(bubble);
      } catch (RuntimeException ignored) {
      }
    bubble = null;
  }

  @Override
  public void onConfigurationChanged(android.content.res.Configuration c) {
    super.onConfigurationChanged(c);
    refresh();
  }
}
