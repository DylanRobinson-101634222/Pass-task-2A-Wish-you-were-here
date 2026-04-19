# Wish You Were Here — Development & commit plan

Overview
- Work in small steps and commit after each mini-task (30–90 minutes).
- Keep simple evidence with each commit (screenshots, short notes) so the spike report is easy to write.

Quick checklist (what each commit should show)
- Two activities using `startActivity` and a `Parcelable` object to pass data.
- Four place images shown on the main screen with name and rating under each.
- At least one non-TextView widget on the detail screen (we use `RatingBar`).
- Use `Spinner`, `CheckBox` and `RadioGroup` on the main screen.
- Put all visible text in `strings.xml` and use at least two reusable styles from `styles.xml`.
- Keep place data in memory and images in `res/drawable` (do not write app data to external files).

Work sessions (each ends with a commit)

Session 1 — Repo hygiene (30–45 min)
Tasks:
- Add a `.gitignore` to stop IDE files and build outputs being tracked.
- Remove any tracked `.idea` files from the Git index.
- Check the Git log and status.

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

Session 2 — Add your four places (60–90 min)
Tasks:
- Edit `MainActivity.kt` and replace the example `LocationItem` entries with your four real places (name, place, last visit date, rating, region).
- Replace the placeholder drawables in `app/src/main/res/drawable` with your photos (keep the same resource names or update `imageResId`).

Commit message:
```
feat: add my four local locations with real details and images
```

Session 3 — Improve filtering and sorting (60–90 min)
Tasks:
- Make sure `Spinner`, `CheckBox` and `RadioGroup` clearly change what is shown.
- Add a small empty-state `TextView` that shows when no items match the filters.
- Add content descriptions for the images for accessibility.

Commit message:
```
feat: polish filtering, sorting and empty-state behaviour
```

Session 4 — Finalise detail screen and styles (45–75 min)
Tasks:
- Confirm `LocationDetailActivity` reads the `Parcelable` and shows all fields.
- Ensure `RatingBar` (and any other non-TextView widget) shows the correct value when the activity starts.
- Use at least two shared styles from `styles.xml` on both screens.

Commit message:
```
feat: finalise detail screen intent flow and reusable styles
```

Session 5 — Tests, screenshots and spike report draft (45–60 min)
Tasks:
- Run `.\gradlew.bat test` to check the project builds.
- Take screenshots for evidence: main screen, filtered view, sorted view, detail view, empty state.
- Write `docs/spike-notes.md` with sketches, a short explanation of intents and `Parcelable`, knowledge gaps and references (APA 7).

PowerShell commands:
```powershell
Set-Location "C:\Users\Drsky\AndroidStudioProjects\Passtask2AWishyouwerehere"
.\gradlew.bat test
git add -A
git commit -m "docs: add spike notes evidence and references draft"
```

Suggested commit messages (short list)
- `feat: add two-activity scaffold with parcelable intent flow`
- `feat: build main/detail layouts with spinner checkbox radiogroup and ratingbar`
- `chore: add resources, styles, placeholder images and README`
- `chore: ignore IDE metadata and clean tracked project files`
- `feat: add my four local locations with real details and images`
- `feat: polish filtering, sorting and empty-state behaviour`
- `feat: finalise detail screen intent flow and reusable styles`
- `test/docs: add screenshots and spike report draft`

Evidence to include in the spike report
- Two quick sketches of the screens (photo or scanned) in `docs/sketches/`.
- Screenshots:
  - main screen showing four images and ratings
  - filtered results (Spinner or CheckBox applied)
  - sorted results (RadioGroup effect)
  - detail screen with rating shown correctly
  - empty-state screen when no items match
- Short code snippets (intent creation, `Parcelable` implementation) with short captions.
- Link to your GitHub repo and list of the commits that show progress.

How to pick up this project on another computer
- Clone the repo.
- Run `.\gradlew.bat test` to check the project builds.
- Open the project in Android Studio and run the `app` configuration on an emulator (API 30+ is fine).
- Look at `docs/development_plan.md` and `docs/spike-notes.md` for the next tasks.

Notes and tips
- Keep each commit focused on one task so marking can see progress.
- Use string resources for all visible text and keep images in `res/drawable`.
- Avoid committing large binary files frequently while you iterate; add them only when needed for the report.

If you want, I will also create a ready-to-edit `docs/spike-notes.md` template and an empty `docs/sketches/` folder for your images. Tell me to proceed and I will add these files now.
