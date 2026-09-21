#!/bin/sh
# Wrapper bootstrap: install Gradle 8.11.1 if needed, then run it.
set -e
if command -v gradle >/dev/null 2>&1; then exec gradle "$@"; fi
echo "Gradle is not installed. Open this project in Android Studio or install Gradle 8.11.1." >&2
exit 1
