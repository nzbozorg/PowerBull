# PowerBull 1.0.1

A small, offline Android assistive utility for worn-out physical buttons. English and conversational Persian interfaces; original navy/red/gold identity, not affiliated with Red Bull or Apple.

## Changes in 1.0.1

- Explicit horizontal/vertical centering of navigation and Quick controls labels under their icons; stable two-line label space.
- Persistent English/Persian switch in the header (فا / EN). Persian content, consent dialogs, feedback, widget configuration and accessibility labels are localized. RTL content layout; header branding/version remain LTR for readability.
- Gold coffee icon in the header and gold support button in About, both opening the existing donation URL only on a user tap.
- versionCode 2; same application ID and release signing certificate as 1.0.0, allowing an in-place update without clearing settings.

## Requirements

- Android 9 (API 28) or newer. This baseline is intentional: screen locking uses the official Accessibility global action, not legacy Device Admin or root.
- Display-over-other-apps permission and the PowerBull accessibility service. Both are explained in the setup wizard, and the controls screen stays gated until granted.
- Universal APK: pure Java/resources, no native libraries or ABI splits. This does **not** guarantee compatibility with every OEM, restricted work profile, Android Go edition, or future Android release.

## Features

- Lock screen, system power menu, media volume up/down, Home, Back and Recents.
- Floating icon-only menu, draggable bubble with animated edge snapping and saved side/vertical position.
- 30–100% opacity, 44–76 dp bubble size, optional idle fading and haptic feedback.
- Configurable action membership/order: switch an action off/on to append it. One action must remain. Settings/close icons are always available.
- Press and hold either floating volume control for repeating volume adjustment.
- Individually configurable home-screen button widgets for all seven actions. Add multiple instances and resize through your launcher.
- Unobtrusive optional donation link only in About: https://coffeebede.com/Amir_NoZohour
- No ads, analytics, account, network permission, root, storage access, or screen-content access.

## Install & setup

1. Install the release APK from a trusted file manager (allow installation from that source if Android asks).
2. Open PowerBull and grant Display over other apps.
3. Read the in-app accessibility disclosure and choose **I agree · Open settings**. Enable **PowerBull controls**.
4. Return to PowerBull; enable the Floating button switch on Controls.
5. Tap the bubble to open, drag to reposition, long-press it to open PowerBull settings.

Android 13+ may restrict Accessibility for sideloaded apps: open Android Settings → Apps → PowerBull → the overflow menu → Allow restricted settings, **only if you trust this APK**, then return to Accessibility. Exact wording varies by manufacturer. PowerBull never enables its own permissions.

For widgets, use Widgets → + Add. If your launcher does not support in-app pinning, long-press your home screen → Widgets → PowerBull button and select the desired action. Pinning behavior and sizing are launcher-dependent.

## Android limitations

- A software button cannot power on a completely switched-off phone, wake a sleeping display, or bypass the lock screen. Do not switch off a phone whose power button cannot turn it back on.
- The power action **opens the Android power menu**, not a forced power-off/reboot. Available actions vary by OS/OEM.
- Volume is **media** volume, not call/ringer/alarm volume. Remote devices, fixed-volume routes, DND and manufacturer policies may limit changes.
- Android can hide third-party overlays on secure screens, permission prompts, lock screens or sensitive applications.
- Some OEMs aggressively stop apps; revisit Accessibility and manufacturer battery/background settings if the bubble disappears. No exemption is silently requested.
- The accessibility service owns the overlay lifecycle. There is no foreground-service notification and no independent background job. After restart, Android may require first unlock before binding it.
- Turning off Accessibility removes the floating controls; all permissions remain revocable in Android Settings.

## Build from source

Open this directory in Android Studio with **JDK 17**, Android SDK Platform 35 and Build Tools 35.0.0 installed. AGP 8.7.3 and Gradle 8.9 are pinned; no third-party runtime dependencies.

```sh
# Development build (uses a separate debug key)
./gradlew assembleDebug

# Static Android checks
./gradlew lintRelease
```

The release signing key is intentionally **not included in the source ZIP**. The separate, private signing backup contains `powerbull-release.jks` and its password. To produce an update compatible with the delivered APK:

1. Put that keystore at `signing/powerbull-release.jks`.
2. Set `PB_STORE_PASSWORD` in your environment to the supplied password.
3. Run:

```sh
./gradlew assembleRelease
```

APK: `app/build/outputs/apk/release/app-release.apk`.

Keep application ID `com.powerbull.app` and the same signing key for updates; increment `versionCode`. Do not publish the signing backup or password. If creating your own separate signing identity, generate a new keystore and adjust the signing configuration; it cannot update installations of this APK without uninstalling.

## Project layout

- `MainActivity`: setup wizard, controls, personalization, widgets and about.
- `BullService`: accessibility global actions and floating overlay lifecycle.
- `Actions`: shared volume/system action dispatch, with failure feedback.
- `Prefs`: local-only settings.
- `BullWidget` / `WidgetConfigActivity` / `ActionActivity`: configurable independent widget instances and explicit immutable PendingIntents.

## Privacy and distribution

The app has no INTERNET permission and transmits no data. The donation button opens an external browser, where that site's policies apply. Accessibility has no event subscriptions, requests no view hierarchy, and processes no screen content. Backup is disabled.

The APK is a signed release build, not a claim of Google Play certification or exhaustive device testing. Review current Play accessibility, target SDK and disclosure policies before publishing to a store. See TESTING.md for the actual verification scope and outstanding device checks.
