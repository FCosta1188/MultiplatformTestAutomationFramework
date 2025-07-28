#!/bin/bash

echo "=== Android Emulator Environment Check ==="

# 1. USER_HOME
echo
echo "[1] Checking USER_HOME"
echo "USER_HOME = $HOME"

# 2. ADB check
echo
echo "[2] Checking ADB location"
if command -v adb >/dev/null 2>&1; then
  echo "✔️ adb is in PATH: $(which adb)"
else
  echo "❌ adb not found in PATH"
fi

# 3. EMULATOR binary check
EMULATOR_PATH="$HOME/Library/Android/sdk/emulator/emulator"
echo
echo "[3] Checking emulator binary at: $EMULATOR_PATH"

if [ -f "$EMULATOR_PATH" ]; then
  echo "✔️ emulator binary exists"
  if [ -x "$EMULATOR_PATH" ]; then
    echo "✔️ emulator is executable"
  else
    echo "❌ emulator is not executable – fixing permissions..."
    chmod +x "$EMULATOR_PATH" && echo "✔️ chmod +x applied"
  fi
else
  echo "❌ emulator binary not found at expected path"
fi

# 4. Check ANDROID_HOME
echo
echo "[4] Checking ANDROID_HOME and ANDROID_SDK_ROOT"

echo "ANDROID_HOME = $ANDROID_HOME"
echo "ANDROID_SDK_ROOT = $ANDROID_SDK_ROOT"

if [ -z "$ANDROID_HOME" ]; then
  echo "⚠️ ANDROID_HOME not set – you can export it temporarily:"
  echo "    export ANDROID_HOME=$HOME/Library/Android/sdk"
fi

if [ -z "$ANDROID_SDK_ROOT" ]; then
  echo "⚠️ ANDROID_SDK_ROOT not set – you can export it temporarily:"
  echo "    export ANDROID_SDK_ROOT=$HOME/Library/Android/sdk"
fi

# 5. Check emulator in PATH
echo
echo "[5] Checking if emulator is in PATH"

if command -v emulator >/dev/null 2>&1; then
  echo "✔️ emulator found in PATH: $(which emulator)"
else
  echo "❌ emulator not found in PATH"
fi

# 6. Test launch manually
echo
echo "[6] Emulator version check (manual test)"
"$EMULATOR_PATH" -version || echo "❌ Emulator failed to execute"

# 7. Java view of user.home
echo
echo "[7] Java System.getProperty(\"user.home\")"
java -XshowSettings:properties -version 2>&1 | grep "user.home"

echo
echo "=== Done ==="