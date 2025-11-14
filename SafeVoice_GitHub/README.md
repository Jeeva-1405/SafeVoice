# SafeVoice - Women's Safety App (GitHub-ready)

This repository is a **GitHub-ready Android project** prepared for Codemagic (or other CI) to build an APK without Android Studio.

## Features (Advanced)
- Login & Registration (local SQLite; Firebase-ready placeholders included)
- Background listening service using SpeechRecognizer
- Emergency flow: shows "Are you safe?" dialog with 10s timer
- If no response: sends SMS with location (requires permissions & SIM) and makes phone call to trusted contact
- Simple, clean UI and improved project structure
- Instructions for connecting Firebase (optional) and Codemagic build steps

---

## Important notes before building on Codemagic / device
1. **Firebase (optional):**
   - If you want Firebase Authentication or Realtime DB, add `google-services.json` into `app/` and uncomment `apply plugin: 'com.google.gms.google-services'` in `app/build.gradle`.
   - Update `project/build.gradle` to include the `com.google.gms:google-services` classpath if you add Firebase.

2. **SMS & CALL Permissions:**
   - Runtime permissions required: `RECORD_AUDIO`, `CALL_PHONE`, `SEND_SMS`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `FOREGROUND_SERVICE`.
   - On Android 10+ background location & foreground service handling may require extra steps.

3. **Wakeword reliability:**
   - `SpeechRecognizer` continuous listening may be unreliable on some devices. For production use, consider a proper wakeword engine (Porcupine, Snowboy alternatives) or native C++ libraries.

4. **Codemagic build steps (summary):**
   - Create GitHub repo and push this project.
   - Sign in to Codemagic, connect repo, select branch.
   - Set Android build workflow (Gradle) and provide signing keys (optional).
   - Start build → download APK.

---

## How to upload to GitHub and build
1. Create a new repository on GitHub (name: SafeVoice).
2. Upload the contents of this folder (or `git init` locally and push).
3. Sign in to Codemagic (or Appcircle) and build the repo.
4. Install the produced APK on your Android phone (allow unknown sources).

---

If you'd like, I can:
- Generate a ready-made Codemagic configuration file.
- Walk you through adding `google-services.json` and enabling Firebase Auth.
- Add a professionally designed UI.

