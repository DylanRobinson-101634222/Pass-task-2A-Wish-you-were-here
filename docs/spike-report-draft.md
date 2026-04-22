Pass Task 2A - Spike Report: Wish You Were Here

Unit: COS30017 Software Development for Mobile Devices  
Student: Dylan Robinson – 101634222  
Date: Wednesday, 22 April 2026  
GitHub Repository: <https://github.com/DylanRobinson-101634222/Pass-task-2A-Wish-you-were-here>

Please see Appendix section 9.1 for weekly task evidence

# Introduction

This spike report documents the main technical questions investigated while building the two-activity Android app for Pass Task 2A: Wish You Were Here.

The completed app presents four travel locations on the first screen. Each card displays an image, title, star rating, and last visit date. Selecting a card opens a second activity that shows the full details for that location. Data is transferred between activities using an explicit Intent and a Parcelable model object.

This report includes:

-   two simple screen sketches,
-   an explanation of the Intent used in the app,
-   reasons for using Parcelable,
-   knowledge gaps and the solutions I applied,
-   references to supporting sources,
-   recommendations for future work,
-   and a checklist of screenshots and weekly task evidence to include in the final PDF.

# Screen sketches

The task requires two simple layout sketches. The wireframes developed in FigJam below show the structure of each screen and identify the widgets required by the assignment.

## Sketch 1: Main screen

![](media/12964ef0936b943d46fa70258fc83a8a.png)

Widgets shown in this sketch:

-   `Toolbar`
-   `TextView` heading
-   `Spinner`
-   `CheckBox`
-   `RadioGroup` with two `RadioButton` options
-   `GridLayout` with four included card layouts
-   each card contains an `ImageView`, `TextView`, `RatingBar`, and `TextView`
-   empty-state `TextView`

## 

## Sketch 2: Detail screen

![](media/bd5da162b2bf04c3c2b180e9ef35bf12.png)

Widgets shown in this sketch:

-   `Toolbar` with Up/back button
-   `ImageView`
-   `TextView` for the location name
-   `TextView` for city/state/country
-   `TextView` for last visit date
-   `RatingBar` configured as read-only

# 

# Intent and Parcelable

## Intent used in this app

An explicit Intent is used to open `LocationDetailActivity` from `MainActivity` when a location card is selected.

The Intent contains three important parts:

| Intent component  | Purpose in this app                                                                            |
|-------------------|------------------------------------------------------------------------------------------------|
| `Context`         | `this` tells Android which activity is launching the next screen                               |
| Destination class | `LocationDetailActivity::class.java` tells Android exactly which screen to open                |
| Extra data        | `putExtra(...)` attaches the selected `LocationItem` object so the second activity can read it |

After those parts are prepared, `startActivity(...)` asks Android to launch the target activity.

Code used to create and send the Intent:

```kotlin
private fun openLocationDetail(item: LocationItem) {
    startActivity(Intent(this, LocationDetailActivity::class.java).apply {
        putExtra(LocationDetailActivity.EXTRA_LOCATION, item)
    })
}
```

The detail activity then reads the Parcelable extra from the received Intent:

```kotlin
val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    intent.getParcelableExtra(EXTRA_LOCATION, LocationItem::class.java)
} else {
    @Suppress("DEPRECATION")
    intent.getParcelableExtra(EXTRA_LOCATION)
}
```

This structure keeps responsibility clear. The first activity determines which item was selected, while the second activity focuses only on reading and displaying the data it receives.

## Why Parcelable was used

`Parcelable` was used in `LocationItem` because it is the Android-recommended mechanism for passing structured objects between activities.

Advantages of `Parcelable` on Android:

-   it is faster than Java `Serializable`,
-   it produces less temporary object overhead,
-   it is designed specifically for Android inter-component communication,
-   and it lets me pass one full model object instead of many separate strings and numbers.

    In this app, each location object includes:

-   name,
-   city/state/country,
-   last visit date,
-   rating,
-   image resource ID,
-   region.

    That means one Parcelable object contains everything the detail screen needs to render the selected location correctly.

# Knowledge gaps and solutions

The following sections summarise the main implementation gaps encountered during development and the approach used to resolve each one.

## Passing full objects between activities

**Knowledge gap:** A simple Intent extra is straightforward for primitive data, but this app required several related values to be transferred together without rebuilding the object manually in the next activity.

**Solution:** A `LocationItem` model implementing `Parcelable` was introduced so one complete object could be passed from `MainActivity` to `LocationDetailActivity`.

Code snippet:

```kotlin
data class LocationItem(
    val name: String,
    val cityStateCountry: String,
    val lastVisitDate: String,
    val rating: Float,
    val imageResId: Int,
    val region: String
) : Parcelable
```

This approach reduces repeated code and keeps the Intent payload easier to maintain.

## Updating the grid when filter controls change

**Knowledge gap:** The main screen includes a `Spinner`, `CheckBox`, and `RadioGroup`, so the filtering and sorting logic needed to update the visible cards immediately whenever any control changed.

**Solution:** Each control was connected to a shared method, `refreshCards()`, which handles filtering, sorting, card visibility, and the empty-state message in one place.

Code snippet:

```kotlin
private fun setupFilters() {
    regionSpinner.onItemSelectedListener = SimpleItemSelectedListener { refreshCards() }
    recentOnlyCheckBox.setOnCheckedChangeListener { _, _ -> refreshCards() }
    sortRadioGroup.setOnCheckedChangeListener { _, _ -> refreshCards() }
}
```

This is a useful decomposition step because the UI logic is centralised instead of duplicated across multiple listeners.

## Using a non-TextView widget that loads the correct value on activity creation

**Knowledge gap:** The assignment requires at least one non-TextView widget to show the correct value when the activity is created. `RatingBar` was selected, but it needed to behave as a display widget rather than an editable control.

**Solution:** Each `RatingBar` was configured as read-only with `android:isIndicator="true"`, and its value was set from the model data when the card or detail screen was bound.

Code snippets:

```xml
<RatingBar
    android:id="@+id/rb_detail_rating"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:isIndicator="true"
    android:numStars="5"
    android:stepSize="0.5" />

findViewById<RatingBar>(R.id.rb_detail_rating).rating = rating
```

This ensures the non-TextView widget displays the correct star value as soon as the detail activity opens.

## Reusing styles and resources instead of repeating values

**Knowledge gap:** Several labels and text styles were repeated across both screens, which increased duplication and made layout maintenance harder.

**Solution:** Repeated text was moved into `strings.xml`, and shared view styles were defined in `styles.xml`.

Styles used across multiple places:

-   `ScreenHeadingStyle`
-   `LocationTitleStyle`
-   `LocationMetaStyle`

    For example, `ScreenHeadingStyle` is used for the main screen heading and the detail screen title text, while `LocationMetaStyle` is reused for metadata such as the place and last visit date.

    This makes the layouts easier to read and easier to maintain if colours, sizes, or labels need to change later.

## Using Kotlin scope functions clearly

**Knowledge gap:** The task asks for evidence of Kotlin-specific features, but these features still need to be used in a way that preserves readability.

**Solution:** Scope functions were used only in places where they improved clarity:

-   `apply` when configuring a new `Intent`,
-   `with` when binding several views from the same object.

    Code snippets:

```kotlin
startActivity(Intent(this, LocationDetailActivity::class.java).apply {
    putExtra(LocationDetailActivity.EXTRA_LOCATION, item)
})

with(location) {
    findViewById<ImageView>(R.id.iv_detail_location).setImageResource(imageResId)
    findViewById<TextView>(R.id.tv_detail_name).text = name
    findViewById<TextView>(R.id.tv_detail_place).text = cityStateCountry
    findViewById<TextView>(R.id.tv_detail_date).text =
        getString(R.string.last_visit_format, lastVisitDate)
    findViewById<RatingBar>(R.id.rb_detail_rating).rating = rating
}
```

Using scope functions in these small cases reduces repetition without making the control flow harder to follow.

# Evidence mapping to criteria

| Requirement                                       | Evidence in app                                                                                                         |
|---------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| Two activities with Intent                        | `MainActivity` opens `LocationDetailActivity` using `startActivity(...)`                                                |
| Parcelable object used                            | `LocationItem` implements `Parcelable`                                                                                  |
| Four images, title, rating and date               | four included cards in the main screen grid and `bindCard()` populates them                                             |
| Non-TextView widget loads correct value on create | `RatingBar` value is set in both card binding and detail screen binding                                                 |
| Wider range of widgets                            | `Spinner`, `CheckBox`, `RadioGroup`, `RadioButton`, `RatingBar`, `Toolbar`, `ImageView`, `TextView`                     |
| Resources used properly                           | strings are stored in `strings.xml`; images are referenced through drawable resources                                   |
| At least two shared styles                        | `ScreenHeadingStyle`, `LocationTitleStyle`, and `LocationMetaStyle` are reused                                          |
| Decomposition and readable Kotlin                 | methods such as `setupSpinner()`, `setupCards()`, `setupFilters()`, `refreshCards()`, and `bindCard()` avoid repetition |

# Recommendations for future improvement

If this app were extended further, the following related areas would be the next priorities for investigation.

1.  **RecyclerView instead of fixed cards**  
    The current layout is suitable for exactly four places, but it would not scale well if the app needed more items. A `RecyclerView` and adapter would be the better long-term solution.
2.  **State persistence across rotation**  
    At the moment, a configuration change such as screen rotation may reset filters and sort selection. I would investigate `ViewModel` and saved UI state to preserve user choices.
3.  **Image optimisation and loading strategy**  
    The app currently uses drawable resources directly. If the project grew, I would look at image compression, multiple density assets, and more advanced loading approaches.
4.  **Custom card component**  
    Another improvement would be to turn the repeated card binding logic into a reusable compound view or adapter-driven item to make the UI easier to extend.

# Screenshots to include before PDF submission

The following screenshots should be inserted into the final PDF version of the report as evidence of implementation and testing.

1.  Main screen with all four location cards visible.
2.  Main screen with a region selected in the `Spinner`.
3.  Main screen sorted by rating.
4.  Main screen showing the empty-state message after filter settings remove all results.
5.  Detail screen showing the correct image, place, date, and `RatingBar` value.

# References

Android Developers. (2021). *Styles and themes*. https://developer.android.com/develop/ui/views/theming/themes

Android Developers. (2022). *RatingBar*. https://developer.android.com/reference/android/widget/RatingBar

Android Developers. (2024a). *Intents and intent filters*. https://developer.android.com/guide/components/intents-filters

Android Developers. (2024b). *Parcelable implementation generator*. https://developer.android.com/kotlin/parcelize

Android Developers. (2024c). *GridLayout*. https://developer.android.com/reference/android/widget/GridLayout

Kotlin Foundation. (2022). *Scope functions*. https://kotlinlang.org/docs/scope-functions.html

Ronald, T. (2021). *SDMD 2021: Core 2 demo* [Video]. YouTube. https://www.youtube.com/watch?v=UBP5y3AKaVM

# Appendix

## Weekly task evidence - Week 6.4 discussion

![](media/2492ee7b07ad2cafdbcbc079459e3cc3.png)
