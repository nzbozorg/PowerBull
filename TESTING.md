# Verification scope — PowerBull 1.0.1

## 1.0.1 update checks

- Matching release signing certificate fingerprint versus the delivered 1.0.0 APK.
- Manifest/version inspection for versionName 1.0.1 and versionCode 2.
- 96 complete English/Persian resource pairs checked for matching keys and Persian text.
- Source regressions for centered icon labels, saved language selection, RTL, and gold support controls (`python tests/verify_update.py`).
- No emulator/device UI tests for this patch; visual confirmation still needs the user's phone. Check English and Persian navigation, Quick controls, language persistence, header fit and donation actions at normal/large system text sizes.

## Completed in the build environment

- Gradle release compilation, resource linking, DEX generation and packaging.
- Android Lint for release (no errors; warnings documented in the delivered lint report).
- Release APK signature verification with Android SDK apksigner.
- Manifest inspection: minSdk 28, targetSdk 35; release is not debuggable; only overlay and audio settings permissions plus the protected accessibility binding; no INTERNET permission.
- Source review of explicit/immutable widget PendingIntents, private action activity, system-protected accessibility service, no screen-reading capability, preference persistence and empty-panel prevention.

## Not completed

Interactive/device tests were not run. An Android 15 ATD emulator was provisioned but could not launch within this sandbox's available RAM (it requires at least 2.5 GB). There was no connected physical device. Compilation/static checks are not evidence that every feature works on every phone.

## Device acceptance checklist

Please check these on the actual phone before depending on PowerBull as the only way to control it:

- [ ] Fresh install, disclosure acceptance, permission denial/retry, setup gate.
- [ ] Restricted-settings procedure on sideloaded Android 13+.
- [ ] Bubble enable/disable and removal after Accessibility is revoked.
- [ ] Bubble tap, drag, long-press, animated edge snap, position after relaunch/rotation.
- [ ] Every panel action on Home and over another app.
- [ ] Lock screen and normal biometric/PIN unlock after locking.
- [ ] Power menu behavior without actually powering off a phone with a broken button.
- [ ] Media volume changes and long-press repeat, with and without Bluetooth audio.
- [ ] Home, Back and Recents from overlay and widgets.
- [ ] Opacity, bubble size, idle fade, haptic toggles; selected actions and single-action panel.
- [ ] Multiple widget instances with different actions, resizing and launcher restart.
- [ ] Reboot/first unlock and manufacturer battery/background restrictions.
- [ ] Large text, TalkBack, landscape, tablets, gesture and three-button navigation.
- [ ] Android 9 and recent Android versions on more than one manufacturer.

Known platform limitations are described in README.md and in-app About. This is a first release for device testing, not an exhaustively validated production deployment.
