# Wish You Were Here - Development and Commit Plan

## Overview
- Do the work in small steps.
- Commit after each mini-task (about 30 to 90 minutes).
- Keep simple proof for each step (screenshots and short notes).

## Quick checklist (what your commits should show)
- Two activities using `startActivity`.
- A `Parcelable` object used to pass place data.
- Four place images on the main screen with name and rating.
- `Spinner`, `CheckBox`, `RadioGroup`, and `RatingBar` in use.
- Visible text kept in `strings.xml`.
- At least two shared styles used in more than one place.
- Place data kept in memory, images kept in `res/drawable`.

## Work sessions (make one commit at the end of each)

### Session 1 - Clean up repo (30 to 45 min)
Tasks:
- Add `.gitignore` so IDE files and build files are not tracked.
- Remove tracked `.idea` files from Git.
- Check Git log and status.

PowerShell commands:
```powershell
Set-Location "C:\Users\Drsky\AndroidStudioProjects\Passtask2AWishyouwerehere"
@"
.idea/
*.iml
"@ | Add-Content .gitignore
git rm -r --cached .idea
git add .gitignore
git commit -m "chore: ignore IDE metadata and clean tracked project files"
git --no-pager status
```

### Session 2 - Add your four places (60 to 90 min)
Tasks:
- Update `MainActivity.kt` with your four real places.
- Include name, city/state/country, last visit date, rating, and region.
- Replace placeholder images in `app/src/main/res/drawable` with your own.

Commit message:
```text
feat: add my four local locations with real details and images
```

### Session 3 - Improve filtering and sorting (60 to 90 min)
Tasks:
- Make sure `Spinner`, `CheckBox`, and `RadioGroup` clearly change results.
- Add a small empty-state `TextView` when no places match filters.
- Add clear content descriptions for images.

Commit message:
```text
feat: polish filtering, sorting and empty-state behaviour
```

### Session 4 - Finalise detail screen and styles (45 to 75 min)
Tasks:
- Confirm `LocationDetailActivity` reads the `Parcelable` and shows all fields.
- Confirm `RatingBar` shows the correct value when the screen opens.
- Reuse at least two styles from `styles.xml` across both screens.

Commit message:
```text
feat: finalise detail screen intent flow and reusable styles
```

### Session 5 - Test, capture proof, and draft report (45 to 60 min)
Tasks:
- Run `.\gradlew.bat test`.
- Capture screenshots: main, filtered, sorted, detail, empty-state.
- Draft `docs/spike-notes.md` with sketches, intent notes, `Parcelable` notes, gaps, and references (APA 7).

PowerShell commands:
```powershell
Set-Location "C:\Users\Drsky\AndroidStudioProjects\Passtask2AWishyouwerehere"
.\gradlew.bat test
git add -A
git commit -m "docs: add spike notes evidence and references draft"
```

## Suggested commit messages
- `feat: add two-activity scaffold with parcelable intent flow`
- `feat: build main/detail layouts with spinner checkbox radiogroup and ratingbar`
- `chore: add resources, styles, placeholder images and README`
- `chore: ignore IDE metadata and clean tracked project files`
- `feat: add my four local locations with real details and images`
- `feat: polish filtering, sorting and empty-state behaviour`
- `feat: finalise detail screen intent flow and reusable styles`
- `test/docs: add screenshots and spike report draft`

## Evidence for the spike report
- Two quick screen sketches in `docs/sketches/`.
- Screenshots of:
  - main screen with four images and ratings
  - filtered results
  - sorted results
  - detail screen with rating shown
  - empty-state screen
- Short code snippets for intent and `Parcelable`, with short captions.
- GitHub repo link and commit list to show progress.

## Continue on another computer
- Clone the repo.
- Run `.\gradlew.bat test`.
- Open in Android Studio and run the `app` config on an emulator/device.
- Read `docs/development_plan.md` and `docs/spike-notes.md` for next tasks.

## Notes
- Keep each commit focused on one clear task.
- Keep UI text in `strings.xml`.
- Keep images in `res/drawable`.
- Avoid frequent large binary commits while you are still iterating.
