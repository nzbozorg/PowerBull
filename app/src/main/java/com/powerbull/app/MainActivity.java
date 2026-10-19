package com.powerbull.app;

import android.app.*;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
  static final int BG = 0xFF080F24,
      CARD = 0xFF121E38,
      WHITE = 0xFFF4F6FF,
      MUTED = 0xFF96A5C2,
      RED = 0xFFFF3655,
      GOLD = 0xFFFFCD55;
  private LinearLayout root, body, nav, previewLine;
  private int tab = 0;
  private boolean ready;

  int d(float x) {
    return Math.round(x * getResources().getDisplayMetrics().density);
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(L10n.wrap(base));
  }

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    if (b != null) tab = b.getInt("tab");
    getWindow().setStatusBarColor(BG);
    getWindow().setNavigationBarColor(BG);
    getWindow().getDecorView().setSystemUiVisibility(0);
  }

  @Override
  protected void onSaveInstanceState(Bundle b) {
    super.onSaveInstanceState(b);
    b.putInt("tab", tab);
  }

  @Override
  protected void onResume() {
    super.onResume();
    render();
  }

  boolean granted() {
    return Settings.canDrawOverlays(this)
        && BullService.live != null
        && Prefs.get(this).getBoolean("consent", false);
  }

  GradientDrawable bg(int color, int radius) {
    GradientDrawable g = new GradientDrawable();
    g.setColor(color);
    g.setCornerRadius(d(radius));
    return g;
  }

  TextView text(String s, int sp, int color, boolean bold) {
    TextView t = new TextView(this);
    t.setText(s);
    t.setTextSize(sp);
    t.setTextColor(color);
    t.setTypeface(
        Typeface.create(
            bold ? "sans-serif-medium" : "sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
    t.setLineSpacing(d(3), 1);
    t.setGravity(Gravity.START);
    t.setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);
    return t;
  }

  LinearLayout col() {
    LinearLayout l = new LinearLayout(this);
    l.setOrientation(LinearLayout.VERTICAL);
    return l;
  }

  void space(LinearLayout l, int n) {
    l.addView(new View(this), new LinearLayout.LayoutParams(1, d(n)));
  }

  void title(String eyebrow, String name, String desc) {
    TextView e = text(eyebrow, 11, GOLD, true);
    e.setLetterSpacing(L10n.isFa(this) ? 0f : .16f);
    body.addView(e);
    space(body, 8);
    body.addView(text(name, 32, WHITE, true));
    space(body, 6);
    body.addView(text(desc, 14, MUTED, false));
    space(body, 24);
  }

  LinearLayout card() {
    LinearLayout c = col();
    c.setPadding(d(18), d(18), d(18), d(18));
    c.setBackground(bg(CARD, 24));
    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
    p.bottomMargin = d(14);
    body.addView(c, p);
    return c;
  }

  TextView button(String label, boolean primary, Runnable run) {
    TextView b = text(label, 15, primary ? WHITE : MUTED, true);
    b.setGravity(Gravity.CENTER);
    b.setPadding(d(16), d(15), d(16), d(15));
    b.setBackground(bg(primary ? RED : 0xFF1C2B48, 16));
    b.setOnClickListener(v -> run.run());
    return b;
  }

  void render() {
    ready = granted();
    root = col();
    root.setLayoutDirection(
        L10n.isFa(this) ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
    root.setBackgroundColor(BG);
    root.setFitsSystemWindows(true);
    setContentView(root);
    LinearLayout top = new LinearLayout(this);
    top.setGravity(Gravity.CENTER_VERTICAL);
    top.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    top.setPadding(d(16), d(14), d(16), d(10));
    ImageView logo = new ImageView(this);
    logo.setImageResource(R.drawable.ic_bull);
    logo.setBackground(bg(RED, 14));
    logo.setPadding(d(8), d(8), d(8), d(8));
    top.addView(logo, new LinearLayout.LayoutParams(d(40), d(40)));
    TextView brand = text("PowerBull", 18, WHITE, true);
    brand.setPadding(d(10), 0, 0, 0);
    brand.setSingleLine(true);
    brand.setEllipsize(android.text.TextUtils.TruncateAt.END);
    brand.setTextDirection(View.TEXT_DIRECTION_LTR);
    top.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
    TextView language = text(L10n.isFa(this) ? "EN" : "فا", 14, WHITE, true);
    language.setGravity(Gravity.CENTER);
    language.setContentDescription(L10n.t(this, R.string.pb_092));
    language.setBackground(bg(0xFF1C2B48, 14));
    language.setOnClickListener(
        v -> {
          L10n.toggle(this);
          recreate();
        });
    top.addView(language, new LinearLayout.LayoutParams(d(48), d(48)));
    ImageButton coffee = new ImageButton(this);
    coffee.setImageResource(R.drawable.ic_coffee);
    coffee.setColorFilter(GOLD);
    coffee.setPadding(d(12), d(12), d(12), d(12));
    coffee.setBackground(bg(BG, 14));
    coffee.setContentDescription(L10n.t(this, R.string.pb_093));
    coffee.setOnClickListener(v -> openSupport());
    top.addView(coffee, new LinearLayout.LayoutParams(d(48), d(48)));
    TextView tag = text("1.0.1", 10, MUTED, true);
    tag.setGravity(Gravity.CENTER);
    tag.setTextDirection(View.TEXT_DIRECTION_LTR);
    tag.setSingleLine(true);
    tag.setLetterSpacing(0f);
    top.addView(tag);
    root.addView(top);
    ScrollView scroll = new ScrollView(this);
    scroll.setFillViewport(true);
    scroll.setClipToPadding(false);
    body = col();
    body.setPadding(d(24), d(22), d(24), d(26));
    scroll.addView(body);
    root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
    if (!ready) {
      wizard();
      return;
    }
    if (tab == 0) dashboard();
    else if (tab == 1) customize();
    else if (tab == 2) widgets();
    else about();
    nav = new LinearLayout(this);
    nav.setPadding(d(10), d(10), d(10), d(14));
    nav.setBackgroundColor(0xFF0C162C);
    String[] names = {
      L10n.t(this, R.string.pb_000),
      L10n.t(this, R.string.pb_001),
      L10n.t(this, R.string.pb_002),
      L10n.t(this, R.string.pb_003)
    };
    int[] icons = {
      R.drawable.ic_bolt, R.drawable.ic_settings, R.drawable.ic_widgets, R.drawable.ic_heart
    };
    for (int i = 0; i < 4; i++) {
      final int n = i;
      LinearLayout item = col();
      item.setGravity(Gravity.CENTER);
      ImageView icon = new ImageView(this);
      icon.setImageResource(icons[i]);
      icon.setColorFilter(tab == i ? RED : MUTED);
      item.addView(icon, new LinearLayout.LayoutParams(d(22), d(22)));
      space(item, 5);
      TextView navLabel = centeredLabel(names[i], 10, tab == i ? WHITE : MUTED, tab == i);
      navLabel.setMinLines(2);
      navLabel.setMaxLines(2);
      item.addView(navLabel, new LinearLayout.LayoutParams(-1, -2));
      item.setPadding(0, d(3), 0, d(3));
      item.setContentDescription(names[i]);
      item.setOnClickListener(
          v -> {
            tab = n;
            render();
          });
      nav.addView(item, new LinearLayout.LayoutParams(0, -2, 1));
    }
    root.addView(nav);
  }

  void wizard() {
    title(
        L10n.t(this, R.string.pb_004),
        L10n.t(this, R.string.pb_005),
        L10n.t(this, R.string.pb_006));
    LinearLayout intro = card();
    intro.addView(text(L10n.t(this, R.string.pb_007), 18, WHITE, true));
    space(intro, 8);
    intro.addView(text(L10n.t(this, R.string.pb_008), 14, MUTED, false));
    boolean overlay = Settings.canDrawOverlays(this), access = BullService.live != null;
    permissionCard(
        "01",
        L10n.t(this, R.string.pb_009),
        L10n.t(this, R.string.pb_010),
        overlay,
        () -> {
          try {
            startActivity(
                new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
          } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
          }
        });
    permissionCard(
        "02",
        L10n.t(this, R.string.pb_011),
        L10n.t(this, R.string.pb_012),
        access && Prefs.get(this).getBoolean("consent", false),
        () ->
            new AlertDialog.Builder(this)
                .setTitle(L10n.t(this, R.string.pb_013))
                .setMessage(
                    L10n.t(this, R.string.accessibility_description)
                        + L10n.t(this, R.string.pb_014))
                .setNegativeButton(L10n.t(this, R.string.pb_015), null)
                .setPositiveButton(
                    L10n.t(this, R.string.pb_016),
                    (a, b) -> {
                      Prefs.get(this).edit().putBoolean("consent", true).apply();
                      startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    })
                .show());
    space(body, 6);
    TextView finish =
        button(
            L10n.t(this, R.string.pb_017),
            true,
            () -> {
              if (granted()) {
                Prefs.get(this).edit().putBoolean("floating", true).apply();
                Prefs.refresh();
                render();
              } else {
                render();
                Toast.makeText(this, L10n.t(this, R.string.pb_018), Toast.LENGTH_LONG).show();
              }
            });
    body.addView(finish);
    space(body, 16);
    body.addView(text(L10n.t(this, R.string.pb_019), 12, MUTED, false));
  }

  void permissionCard(String n, String name, String desc, boolean done, Runnable action) {
    LinearLayout c = card();
    c.addView(
        text(
            (done ? "✓" : L10n.t(this, R.string.pb_020) + n) + "  ·  " + name,
            17,
            done ? 0xFF78D9B7 : WHITE,
            true));
    space(c, 8);
    c.addView(text(desc, 13, MUTED, false));
    space(c, 14);
    c.addView(
        button(
            done ? L10n.t(this, R.string.pb_021) : L10n.t(this, R.string.pb_022), !done, action));
  }

  void dashboard() {
    title(
        L10n.t(this, R.string.pb_023),
        L10n.t(this, R.string.pb_024),
        L10n.t(this, R.string.pb_025));
    LinearLayout hero = card();
    GradientDrawable gradient =
        new GradientDrawable(
            GradientDrawable.Orientation.TL_BR, new int[] {0xFF253D6C, 0xFF131E38});
    gradient.setCornerRadius(d(24));
    hero.setBackground(gradient);
    LinearLayout headline = new LinearLayout(this);
    headline.setGravity(Gravity.CENTER_VERTICAL);
    LinearLayout words = col();
    words.addView(text(L10n.t(this, R.string.pb_026), 10, GOLD, true));
    space(words, 7);
    words.addView(text(L10n.t(this, R.string.pb_027), 23, WHITE, true));
    headline.addView(words, new LinearLayout.LayoutParams(0, -2, 1));
    ImageView orb = new ImageView(this);
    orb.setImageResource(R.drawable.ic_bull);
    orb.setPadding(d(21), d(21), d(21), d(21));
    orb.setBackground(bg(RED, 100));
    headline.addView(orb, new LinearLayout.LayoutParams(d(84), d(84)));
    hero.addView(headline);
    space(hero, 20);
    Switch enabled = new Switch(this);
    enabled.setText(L10n.t(this, R.string.pb_028));
    enabled.setTextSize(15);
    enabled.setTextColor(WHITE);
    enabled.setButtonTintList(ColorStateList.valueOf(RED));
    enabled.setChecked(Prefs.get(this).getBoolean("floating", false));
    hero.addView(enabled);
    enabled.setOnCheckedChangeListener(
        (b, on) -> {
          Prefs.get(this).edit().putBoolean("floating", on).apply();
          Prefs.refresh();
        });
    space(hero, 9);
    hero.addView(text(L10n.t(this, R.string.pb_029), 12, MUTED, false));
    space(body, 5);
    body.addView(text(L10n.t(this, R.string.pb_030), 20, WHITE, true));
    space(body, 5);
    body.addView(text(L10n.t(this, R.string.pb_031), 12, MUTED, false));
    space(body, 14);
    for (int i = 0; i < Actions.IDS.length; ) {
      LinearLayout row = new LinearLayout(this);
      for (int j = 0; j < 2 && i < Actions.IDS.length; j++, i++) {
        String a = Actions.IDS[i];
        LinearLayout tile = col();
        tile.setGravity(Gravity.CENTER);
        tile.setPadding(d(8), d(20), d(8), d(17));
        tile.setBackground(bg(CARD, 20));
        ImageView icon = new ImageView(this);
        icon.setImageResource(Actions.icon(a));
        icon.setColorFilter(a.equals("lock") ? RED : WHITE);
        tile.addView(icon, new LinearLayout.LayoutParams(d(27), d(27)));
        space(tile, 10);
        TextView actionLabel = centeredLabel(Actions.label(this, a), 13, WHITE, true);
        actionLabel.setMinLines(2);
        tile.addView(actionLabel, new LinearLayout.LayoutParams(-1, -2));
        tile.setContentDescription(Actions.label(this, a));
        tile.setOnClickListener(v -> Actions.run(this, a));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1);
        lp.setMarginEnd(d(8));
        lp.bottomMargin = d(8);
        row.addView(tile, lp);
      }
      if (row.getChildCount() == 1)
        row.addView(new View(this), new LinearLayout.LayoutParams(0, 1, 1));
      body.addView(row);
    }
    space(body, 12);
    body.addView(text(L10n.t(this, R.string.pb_032), 14, GOLD, true));
    space(body, 6);
    body.addView(text(L10n.t(this, R.string.pb_033), 12, MUTED, false));
  }

  void customize() {
    title(
        L10n.t(this, R.string.pb_034),
        L10n.t(this, R.string.pb_035),
        L10n.t(this, R.string.pb_036));
    LinearLayout preview = card();
    preview.addView(text(L10n.t(this, R.string.pb_037), 10, GOLD, true));
    space(preview, 12);
    previewLine = new LinearLayout(this);
    previewLine.setGravity(Gravity.CENTER);
    updatePreview();
    preview.addView(previewLine);
    space(preview, 7);
    preview.addView(text(L10n.t(this, R.string.pb_038), 11, MUTED, false));
    LinearLayout look = card();
    look.addView(text(L10n.t(this, R.string.pb_039), 18, WHITE, true));
    slider(look, L10n.t(this, R.string.pb_040), "opacity", 30, 100, 90, "%", false);
    slider(
        look,
        L10n.t(this, R.string.pb_041),
        "size",
        44,
        76,
        58,
        L10n.t(this, R.string.pb_042),
        false);
    switchRow(look, L10n.t(this, R.string.pb_043), L10n.t(this, R.string.pb_044), "autofade", true);
    switchRow(look, L10n.t(this, R.string.pb_045), L10n.t(this, R.string.pb_046), "haptic", true);
    LinearLayout choices = card();
    choices.addView(text(L10n.t(this, R.string.pb_047), 18, WHITE, true));
    space(choices, 5);
    choices.addView(text(L10n.t(this, R.string.pb_048), 12, MUTED, false));
    space(choices, 12);
    for (String a : Actions.IDS) {
      Switch s = new Switch(this);
      s.setText(Actions.label(this, a));
      s.setTextColor(WHITE);
      s.setTextSize(14);
      s.setPadding(0, d(10), 0, d(10));
      s.setChecked(Prefs.has(this, a));
      choices.addView(s);
      s.setOnCheckedChangeListener(
          (v, on) -> {
            if (!on && Prefs.selected(this).length == 1 && Prefs.has(this, a)) {
              s.setChecked(true);
              Toast.makeText(this, L10n.t(this, R.string.pb_049), Toast.LENGTH_SHORT).show();
              return;
            }
            Prefs.toggle(this, a, on);
            updatePreview();
          });
    }
    body.addView(
        button(
            L10n.t(this, R.string.pb_050),
            false,
            () ->
                new AlertDialog.Builder(this)
                    .setTitle(L10n.t(this, R.string.pb_051))
                    .setMessage(L10n.t(this, R.string.pb_052))
                    .setNegativeButton(L10n.t(this, R.string.pb_053), null)
                    .setPositiveButton(
                        L10n.t(this, R.string.pb_054),
                        (a, b) -> {
                          Prefs.get(this)
                              .edit()
                              .remove("actions")
                              .remove("opacity")
                              .remove("size")
                              .remove("autofade")
                              .remove("haptic")
                              .remove("y")
                              .remove("right")
                              .apply();
                          Prefs.refresh();
                          render();
                        })
                    .show()));
  }

  void updatePreview() {
    if (previewLine == null) return;
    previewLine.removeAllViews();
    for (String a : Prefs.selected(this)) {
      ImageView icon = new ImageView(this);
      icon.setImageResource(Actions.icon(a));
      icon.setPadding(d(7), d(7), d(7), d(7));
      icon.setContentDescription(Actions.label(this, a));
      previewLine.addView(icon, new LinearLayout.LayoutParams(0, d(40), 1));
    }
  }

  void slider(
      LinearLayout c,
      String name,
      String key,
      int min,
      int max,
      int def,
      String unit,
      boolean unused) {
    space(c, 16);
    TextView label =
        text(name + "  ·  " + Prefs.get(this).getInt(key, def) + unit, 14, WHITE, false);
    c.addView(label);
    SeekBar seek = new SeekBar(this);
    seek.setMax(max - min);
    seek.setProgress(Prefs.get(this).getInt(key, def) - min);
    seek.setProgressTintList(ColorStateList.valueOf(RED));
    seek.setThumbTintList(ColorStateList.valueOf(RED));
    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, d(44));
    c.addView(seek, p);
    seek.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar s, int progress, boolean user) {
            int value = progress + min;
            label.setText(name + "  ·  " + value + unit);
            if (user) Prefs.get(MainActivity.this).edit().putInt(key, value).apply();
          }

          public void onStartTrackingTouch(SeekBar s) {}

          public void onStopTrackingTouch(SeekBar s) {
            Prefs.refresh();
          }
        });
  }

  void switchRow(LinearLayout c, String name, String description, String key, boolean def) {
    space(c, 14);
    Switch s = new Switch(this);
    s.setText(name);
    s.setTextSize(14);
    s.setTextColor(WHITE);
    s.setChecked(Prefs.get(this).getBoolean(key, def));
    c.addView(s);
    space(c, 4);
    c.addView(text(description, 12, MUTED, false));
    s.setOnCheckedChangeListener(
        (b, on) -> {
          Prefs.get(this).edit().putBoolean(key, on).apply();
          Prefs.refresh();
        });
  }

  void widgets() {
    title(
        L10n.t(this, R.string.pb_055),
        L10n.t(this, R.string.pb_056),
        L10n.t(this, R.string.pb_057));
    LinearLayout how = card();
    how.addView(text(L10n.t(this, R.string.pb_058), 18, WHITE, true));
    space(how, 8);
    how.addView(text(L10n.t(this, R.string.pb_059), 14, MUTED, false));
    for (String action : Actions.IDS) {
      LinearLayout c = card();
      LinearLayout row = new LinearLayout(this);
      row.setGravity(Gravity.CENTER_VERTICAL);
      ImageView icon = new ImageView(this);
      icon.setImageResource(Actions.icon(action));
      row.addView(icon, new LinearLayout.LayoutParams(d(26), d(26)));
      TextView name = text("   " + Actions.label(this, action), 15, WHITE, true);
      row.addView(name, new LinearLayout.LayoutParams(0, -2, 1));
      TextView add = button(L10n.t(this, R.string.pb_060), false, () -> pin(action));
      row.addView(add);
      c.addView(row);
    }
    body.addView(text(L10n.t(this, R.string.pb_061), 12, MUTED, false));
  }

  void pin(String action) {
    AppWidgetManager m = AppWidgetManager.getInstance(this);
    if (m.isRequestPinAppWidgetSupported()) {
      Prefs.get(this).edit().putString("pending_widget", action).apply();
      Bundle options = new Bundle();
      options.putString("powerbull_action", action);
      boolean accepted =
          m.requestPinAppWidget(new ComponentName(this, BullWidget.class), options, null);
      if (!accepted) Toast.makeText(this, L10n.t(this, R.string.pb_062), Toast.LENGTH_LONG).show();
    } else Toast.makeText(this, L10n.t(this, R.string.pb_063), Toast.LENGTH_LONG).show();
  }

  void about() {
    title(
        L10n.t(this, R.string.pb_064),
        L10n.t(this, R.string.pb_065),
        L10n.t(this, R.string.pb_066));
    LinearLayout privacy = card();
    privacy.addView(text(L10n.t(this, R.string.pb_067), 19, WHITE, true));
    space(privacy, 10);
    privacy.addView(text(L10n.t(this, R.string.pb_068), 14, MUTED, false));
    space(privacy, 14);
    privacy.addView(
        button(
            L10n.t(this, R.string.pb_069),
            false,
            () -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))));
    space(privacy, 8);
    privacy.addView(
        button(
            L10n.t(this, R.string.pb_070),
            false,
            () ->
                startActivity(
                    new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())))));
    LinearLayout help = card();
    help.addView(text(L10n.t(this, R.string.pb_071), 18, WHITE, true));
    space(help, 10);
    help.addView(text(L10n.t(this, R.string.pb_072), 13, MUTED, false));
    LinearLayout support = card();
    support.addView(text(L10n.t(this, R.string.pb_073), 18, WHITE, true));
    space(support, 8);
    support.addView(text(L10n.t(this, R.string.pb_074), 13, MUTED, false));
    space(support, 16);
    TextView supportButton = button(L10n.t(this, R.string.pb_075), false, () -> openSupport());
    supportButton.setTextColor(BG);
    supportButton.setBackground(bg(GOLD, 16));
    support.addView(supportButton);
    space(body, 4);
    body.addView(text(L10n.t(this, R.string.pb_077), 11, MUTED, false));
  }

  TextView centeredLabel(String label, int size, int color, boolean bold) {
    TextView view = text(label, size, color, bold);
    view.setGravity(Gravity.CENTER);
    view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    return view;
  }

  void openSupport() {
    try {
      startActivity(
          new Intent(Intent.ACTION_VIEW, Uri.parse("https://coffeebede.com/Amir_NoZohour")));
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, L10n.t(this, R.string.pb_076), Toast.LENGTH_LONG).show();
    }
  }
}
