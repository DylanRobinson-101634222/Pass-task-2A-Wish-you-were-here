# Wish You Were Here (Pass Task 2A)

Two-activity Android app built in Kotlin for COS30017.

## Current scope (Stage 1)

- Activity 1 shows four location cards in a grid.
- Each card has an image, title, last-visit text, and rating.
- UI includes `Spinner`, `CheckBox`, `RadioGroup`, and `RatingBar`.
- Tapping an image opens Activity 2.
- Data is passed via a `Parcelable` model (`LocationItem`).

## Run

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on an emulator/device (API 30+).

## Quick check from terminal

```powershell
Set-Location "C:\Users\Drsky\AndroidStudioProjects\Passtask2AWishyouwerehere"
.\gradlew.bat test
```

## Next planned stages

- Replace placeholder drawable images with your own photos.
- Add small validation and UX polish.
- Capture screenshots for spike report evidence.
- Final pass on comments, decomposition, and style reuse notes.

