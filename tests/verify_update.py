"""Source/resource regression checks, no emulator required."""
from pathlib import Path
import xml.etree.ElementTree as ET
import re
root = Path(__file__).resolve().parents[1]
res = root / 'app/src/main/res'
java = root / 'app/src/main/java/com/powerbull/app'

def strings(path):
    return {node.attrib['name']: node.text for node in ET.parse(path).getroot()}

en = strings(res / 'values/localization.xml')
fa = strings(res / 'values-fa/localization.xml')
assert en.keys() == fa.keys(), 'EN/FA key mismatch'
assert len(en) >= 90 and all(fa.values()), 'Missing translations'
for key in fa:
    assert re.search('[\u0600-\u06ff]', fa[key]), f'Persian translation missing: {key}'
main = (java / 'MainActivity.java').read_text()
assert 'centeredLabel(names[i]' in main
assert 'centeredLabel(Actions.label(this, a)' in main
assert 'view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)' in main
assert 'view.setGravity(Gravity.CENTER)' in main
assert 'L10n.toggle(this)' in main and 'recreate()' in main
assert 'coffee.setColorFilter(GOLD)' in main
assert 'supportButton.setBackground(bg(GOLD, 16))' in main
assert 'putString("language"' in (java / 'L10n.java').read_text()
assert 'versionCode 2;' in (root / 'app/build.gradle').read_text()
assert "versionName '1.0.1'" in (root / 'app/build.gradle').read_text()
manifest = (root / 'app/src/main/AndroidManifest.xml').read_text()
assert 'android:supportsRtl="true"' in manifest
assert 'android.permission.INTERNET' not in manifest
print(f'PASS: {len(en)} matching EN/FA translations; centered nav/quick labels; RTL, saved language, both gold support controls, version 1.0.1/code 2, no network permission.')
