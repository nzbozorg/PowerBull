<div align="center">

# 🐂 PowerBull

### Your buttons. Back in action.

**Floating controls and home-screen widgets that put your phone back within reach.**

**Android 9+ · English & فارسی · No root · No ads · Offline**

[🇮🇷 فارسی](README.fa.md) · [📦 Release notes](RELEASE_NOTES.md) · [💛 Support development](https://coffeebede.com/Amir_NoZohour)

</div>

---

## ✨ What is PowerBull?

A broken button shouldn't make your phone harder to use.

PowerBull brings everyday hardware and navigation controls to a compact floating menu and configurable home-screen widgets. Whether your physical buttons are worn out or you simply prefer a gentler way to use your phone, your essential controls are just a tap away.

An original navy, red and gold interface keeps things clear, compact and out of your way.

## 🎛️ Your everyday controls

| Control | What it does |
| --- | --- |
| 🔒 Lock screen | Locks the device and turns the display off using Android's accessibility action. |
| ⏻ Power menu | Opens the system power menu, where supported. |
| 🔊 Volume up | Raises media volume. |
| 🔉 Volume down | Lowers media volume. |
| 🏠 Home | Returns to the home screen. |
| ↩️ Back | Performs Android's Back action. |
| ▣ Recent apps | Opens the recent-apps overview. |

> **Important:** PowerBull cannot turn on a powered-off phone, wake a sleeping display, or bypass the lock screen. If your power button is broken, make sure you have a reliable way to turn the phone back on before shutting it down.

## 🫧 Small panel. Your rules.

- **Icon-only floating menu** — useful controls without a wall of text.
- **Drag to move** — the bubble snaps to the nearest edge with an animation.
- **Remembered position** — your preferred side and vertical position are saved.
- **Choose your buttons** — keep one action or build a fuller panel; at least one action stays enabled.
- **Adjust the look** — panel opacity from **30–100%**, bubble size from **44–76 dp**.
- **Optional idle fading** — make the bubble less noticeable when you're not using it.
- **Optional touch feedback** — a gentle vibration on floating controls.
- **Hold to adjust volume** — long-press a floating volume button for repeated changes.
- **Easy access to settings** — long-press the bubble or use the panel's settings icon.

To move an action to the end of the panel, switch it off and back on in **Personalize**. Settings and Close remain available in the expanded panel.

## 🧩 Widgets that fit your home screen

Add independent widgets for any of the seven actions. Use a single Lock screen shortcut or add several different controls — each widget keeps its own action.

Add widgets from PowerBull's **Widgets** tab, or from your launcher's widget picker. Sizing, resizing and in-app pinning support depend on your launcher.

## 🌍 English or فارسی

Switch languages using the **فا / EN** button beside the version number in the header.

Persian uses natural, conversational wording and right-to-left content layout. Your language choice is saved. Controls, setup explanations, dialogs, widget action names and feedback messages are localized.

## 📥 Install & get started

1. Download `PowerBull-1.0.1-universal-release.apk` from this repository's **Releases** section.
2. Open the APK and allow installation from your chosen file manager or browser if Android asks. Install only from a source you trust.
3. Open PowerBull and follow the setup wizard:
   - Enable **Display over other apps**.
   - Read and accept the accessibility disclosure, then enable **PowerBull controls** in Android Accessibility settings.
4. Return to the app and turn on **Floating button** in **Controls**.
5. Tap the bubble to expand it, drag it to move it, and customize it in **Personalize**.

### Updating from 1.0.0

Install the official 1.0.1 APK over the previous release. It uses the same application ID and signing certificate, so uninstalling first is not necessary. Keep your existing settings.

### 🔐 Android's restricted settings

Some Android 13+ devices restrict Accessibility for sideloaded applications. If Android blocks enabling PowerBull:

**Settings → Apps → PowerBull → overflow menu → Allow restricted settings**

Then return to Accessibility and enable PowerBull controls. Menu names vary by manufacturer. Only allow this if you trust the APK. PowerBull never enables permissions on your behalf.

## 🛡️ Permissions & privacy

| Access | Why it is needed |
| --- | --- |
| Display over other apps | Shows the floating bubble and panel over other applications. |
| Accessibility service | Performs Lock screen, Power menu, Home, Back and Recents when you choose those controls. |
| Modify audio settings | Adjusts media volume; no separate runtime prompt is normally required. |

**Your phone stays yours:**

- No advertising, analytics, trackers or account requirement.
- No `INTERNET` permission and no data transmission by the app.
- No screen-content retrieval, activity monitoring, text entry or screen recording.
- Accessibility events are not subscribed to; the service acts on the control you choose.
- Settings stay locally on your device; app backup is disabled.
- Permissions can be revoked in Android Settings at any time.

The optional support button opens an external browser. The donation website has its own privacy practices.

## ⚠️ Know the Android limits

- **Android 9 / API 28 or newer is required.** Screen locking uses an official accessibility action, not root or legacy Device Admin.
- **Universal does not mean every phone is guaranteed.** The APK has no native ABI-specific libraries, but OEM restrictions, work profiles and Android editions can affect functionality.
- **Power menu is not forced shutdown.** Android and your manufacturer decide which power controls are available.
- **Volume controls are for media**, not dedicated call, alarm or ringer volume. Audio routes and system policies can limit changes.
- **Secure screens can hide overlays**, including permission dialogs and sensitive apps.
- **Battery restrictions can affect availability.** If the bubble disappears, check Accessibility and your manufacturer's background-app settings.
- **After a restart**, Android may require your first unlock before starting the accessibility service.

For the verification scope and suggested device checks, see [TESTING.md](TESTING.md). The release has passed compilation, signature verification and static checks; that is not a guarantee of exhaustive device compatibility.

## 🛠️ Build from source

### Toolchain

| Component | Version |
| --- | --- |
| Language | Java |
| JDK | 17 |
| Minimum Android | 9 / API 28 |
| Compile / target SDK | 35 |
| Android Build Tools | 35.0.0 |
| Android Gradle Plugin | 8.7.3 |
| Gradle Wrapper | 8.9 |
| Application ID | `com.powerbull.app` |

Open the project in Android Studio and install the required SDK components. There are no third-party runtime dependencies.

```sh
# Build a development APK
./gradlew assembleDebug

# Run Android static checks
./gradlew lintRelease

# Run source/resource regression checks (Python 3)
python3 tests/verify_update.py
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

### 🔑 Signed release builds

Private signing files are **not part of the public source**.

The current Gradle configuration expects a keystore at `signing/powerbull-release.jks`, alias `powerbull`, and its password in the `PB_STORE_PASSWORD` environment variable. Use your own signing identity for an independent build; never request or publish the maintainer's private key.

```sh
# After configuring your local signing key and environment
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`.

A build signed with a different key cannot update the official APK in place. Official updates must keep the original application ID and release key and increase `versionCode`.

## 🗂️ Project map

```text
app/src/main/
├── java/com/powerbull/app/
│   ├── MainActivity.java          # Setup, controls, personalization and About
│   ├── BullService.java           # Accessibility actions and floating UI
│   ├── Actions.java               # Shared action dispatch
│   ├── BullWidget.java            # Independent widget instances
│   ├── WidgetConfigActivity.java  # Widget action picker
│   ├── ActionActivity.java        # Widget action entry point
│   ├── L10n.java                  # Saved app language and localized resources
│   └── Prefs.java                 # Local preferences
└── res/                           # Icons, layouts and EN/FA resources
```

## 🤝 Feedback & contributions

Found a problem or have an idea? Open an issue in this repository. For bugs, please include:

- Phone model and Android version.
- PowerBull version and selected language.
- Steps to reproduce, expected behavior and what actually happened.
- A screenshot or recording when helpful — remove personal information first.

For larger changes, discuss the idea in an issue before opening a pull request. Please keep accessibility, privacy, compact controls and both languages in mind.

## 💛 Support PowerBull

If PowerBull makes your day easier, you can support its development with a coffee:

**[☕ Buy Amir a coffee](https://coffeebede.com/Amir_NoZohour)**

Completely optional. No locked features, donation pop-ups or reminders. The support shortcut is available in the header and in About.

---

<div align="center">

**🐂 PowerBull — A small touch. More control.**

An independent project. Not affiliated with, endorsed by, or sponsored by Red Bull or Apple.

</div>

## 📄 License

PowerBull is licensed under the **Apache License 2.0**. See [LICENSE](LICENSE) for the full terms.

You may use, modify and redistribute the project, including commercially, subject to the license conditions. Keep the required license and attribution notices, mark modified files, and preserve applicable NOTICE attributions if provided. The software is provided **“AS IS,” without warranties or conditions of any kind**. This license does not grant rights to project names or trademarks.
