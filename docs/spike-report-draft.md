Pass Task 2A - Spike Report: Wish You Were Here

Unit: COS30017 Software Development for Mobile Devices  
Student: Dylan Robinson – 101634222  
Date: Wednesday, 22 April 2026  
GitHub Repository: https://github.com/DylanRobinson-101634222/Pass-task-2A-Wish-you-were-here

Please see Appendix section 9.10 for weekly task evidence

# Introduction

This report explains how I built my two-activity Android app for Pass Task 2A.

The app shows four locations on the first screen. Each location card has an image, title, rating, and last visit date. When I tap a card, a second screen opens and shows full details for that location.

The report includes:

-   two screen sketches,
-   how I used Intents,
-   why I used Parcelable,
-   key knowledge gaps and how I solved them,
-   references,
-   and evidence screenshots.

# Screen sketches

## Sketch 1: Main screen

[Insert sketch image here]

Widgets shown in this sketch:

-   `Toolbar`
-   `Spinner`
-   `CheckBox`
-   `RadioGroup` with two `RadioButton` options
-   `GridLayout` with 4 cards
-   Card contents: `ImageView`, `TextView`, `RatingBar`, `TextView`
-   Empty-state `TextView`

## Sketch 2: Detail screen

[Insert sketch image here]

Widgets shown in this sketch:

-   `Toolbar` with Up/back button
-   `ImageView`
-   `TextView` (name)
-   `TextView` (place)
-   `TextView` (last visit)
-   `RatingBar` (read-only)

# Intent and Parcelable

## Intent used in this app

I used an explicit Intent to open `LocationDetailActivity` from `MainActivity`.

What each part does:

-   `Intent(this, LocationDetailActivity::class.java)`: tells Android the target screen.
-   `putExtra(...)`: attaches the selected location data.
-   `startActivity(...)`: opens the second screen.

    Code used:

```Kotlin
private fun openLocationDetail(item: LocationItem) {
   startActivity(Intent(this, LocationDetailActivity::class.java).apply {
      putExtra(LocationDetailActivity.EXTRA_LOCATION, item)
   })
}
```

## Why Parcelable

I used `Parcelable` in `LocationItem` because it is the Android standard for passing objects between activities. It is faster and lighter than Java `Serializable`.

In this app, each location object includes:

-   name,
-   city/state/country,
-   last visit date,
-   rating,
-   image resource ID,
-   region.

    The detail screen reads this object from the Intent and updates the views on screen.

# Knowledge gaps and solutions

## Passing full objects between activities

**Gap:** At first, I only knew how to pass simple strings between screens.

**Solution:** I made a `LocationItem` model that implements `Parcelable`. This let me pass one complete object to the detail screen.

Code snippet:

```Kotlin
data class LocationItem(
   val name: String,
   val cityStateCountry: String,
   val lastVisitDate: String,
   val rating: Float,
   val imageResId: Int,
   val region: String
) : Parcelable
```

## Making filter controls update the grid

**Gap:** I needed the screen to refresh when Spinner, CheckBox, or RadioGroup values changed.

**Solution:** I used listeners on each control and called one shared method `refreshCards()`.

Code snippet:

```Kotlin
private fun setupFilters() {
   regionSpinner.onItemSelectedListener = SimpleItemSelectedListener { refreshCards() }
   recentOnlyCheckBox.setOnCheckedChangeListener { _, _ -> refreshCards() }
   sortRadioGroup.setOnCheckedChangeListener { _, _ -> refreshCards() }
}
```

## Showing rating as read-only in both screens

**Gap:** I needed a non-TextView widget that loads the right value when the activity is created.

**Solution:** I used `RatingBar` with `android:isIndicator="true"` and set the rating from model data.

Code snippet:

```xml
<RatingBar
        android:id="@+id/rb_detail_rating"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:isIndicator="true"
        android:numStars="5"
        android:stepSize="0.5" />
```

```kotlin
findViewById<RatingBar>(R.id.rb_detail_rating).rating = rating
```

## Reusing styles and string resources

**Gap:** I had repeated text styling and labels.

**Solution:** I moved text labels to `strings.xml` and reused styles from `styles.xml`.

Styles used in multiple places:

-   `ScreenHeadingStyle`
-   `LocationTitleStyle`
-   `LocationMetaStyle`

    This kept the layout files cleaner and easier to update.

## Using scope functions clearly

**Gap:** I was unsure how to use Kotlin scope functions without making code harder to read.

**Solution:** I only used simple cases:

-   `apply` when creating an `Intent`.
-   `with` when binding many views from one object.

    This reduced repetition without making the code confusing.

# Recommendations for future improvement

If I continue this app, I would improve these areas:

1.  **RecyclerView instead of fixed includes**  
    This would scale better if I were to add more than four locations.
2.  **State handling on rotation**  
    I would keep filters/sort state with `ViewModel` so rotation does not reset the screen.
3.  **Image loading and optimisation**  
    I would resize and compress images further to reduce APK size.

# Evidence mapping to criteria

| Requirement                       | Evidence in app                                                         |
|-----------------------------------|-------------------------------------------------------------------------|
| Two activities with intent        | `MainActivity` opens `LocationDetailActivity` with `startActivity`      |
| Parcelable object used            | `LocationItem` implements `Parcelable`                                  |
| Four images + title + rating      | 4 cards in `activity_main.xml` and `bindCard()` in `MainActivity.kt`    |
| Non-TextView widget on create     | `RatingBar` value set in `LocationDetailActivity.kt`                    |
| Wider range of widgets            | `Spinner`, `CheckBox`, `RadioGroup`, `RatingBar`                        |
| Resources used properly           | Strings in `strings.xml`, images in `res/drawable`                      |
| At least two shared styles        | `ScreenHeadingStyle`, `LocationTitleStyle`, `LocationMetaStyle`         |
| Decomposition and readable Kotlin | Methods like `setupSpinner`, `setupFilters`, `refreshCards`, `bindCard` |

# Screenshots

Add these screenshots before submitting:

1.  Main screen with all four cards visible.
2.  Main screen with Spinner filter applied.
3.  Main screen sorted by rating.
4.  Main screen with empty-state message shown.
5.  Detail screen with correct data and RatingBar value.

[Insert screenshots here]

# References

Android Developers. (2021). *Styles and themes*. https://developer.android.com/develop/ui/views/theming/themes

Android Developers. (2022). *RatingBar*. https://developer.android.com/reference/android/widget/RatingBar

Android Developers. (2024). *Intents and intent filters*. https://developer.android.com/guide/components/intents-filters

Android Developers. (2024). *Parcelable implementation generator*. https://developer.android.com/kotlin/parcelize

Kotlin Foundation. (2022). *Scope functions*. https://kotlinlang.org/docs/scope-functions.html

Ronald, T. (2021). *SDMD 2021: Core 2 demo* [Video]. YouTube. https://www.youtube.com/watch?v=UBP5y3AKaVM

# Appendix
## 9.10 Weekly task evidence