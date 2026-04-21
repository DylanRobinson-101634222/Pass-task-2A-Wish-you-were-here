package au.edu.swin.passtask2a_wishyouwerehere

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    // All four places are stored here in memory — nothing is written to disk.
    private val locations = listOf(
        LocationItem(
            name = "Uluru (Ayers Rock)",
            cityStateCountry = "Uluru-Kata Tjuta National Park, Northern Territory, Australia",
            lastVisitDate = "2025-09-15",
            rating = 4.5f,
            imageResId = R.drawable.location_uluru,
            region = "Northern Territory"
        ),
        LocationItem(
            name = "Great Barrier Reef",
            cityStateCountry = "Cairns / Queensland, Australia",
            lastVisitDate = "2026-02-10",
            rating = 5.0f,
            imageResId = R.drawable.location_great_barrier_reef,
            region = "Queensland"
        ),
        LocationItem(
            name = "Sydney Opera House",
            cityStateCountry = "Sydney, New South Wales, Australia",
            lastVisitDate = "2024-07-20",
            rating = 4.5f,
            imageResId = R.drawable.location_opera_house,
            region = "New South Wales"
        ),
        LocationItem(
            name = "Twelve Apostles (Great Ocean Road)",
            cityStateCountry = "Port Campbell, Victoria, Australia",
            lastVisitDate = "2023-12-01",
            rating = 4.0f,
            imageResId = R.drawable.location_twelve_apostles,
            region = "Victoria"
        )
    )

    private lateinit var regionSpinner: Spinner
    private lateinit var recentOnlyCheckBox: CheckBox
    private lateinit var sortRadioGroup: RadioGroup
    private lateinit var locationGrid: GridLayout
    private lateinit var toolbar: Toolbar
    private lateinit var emptyStateText: TextView

    private val cardViews = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Wire up the toolbar so it shows the app title at the top of the screen.
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        regionSpinner = findViewById(R.id.spinner_region)
        recentOnlyCheckBox = findViewById(R.id.checkbox_recent_only)
        sortRadioGroup = findViewById(R.id.rg_sort)
        locationGrid = findViewById(R.id.location_grid)
        emptyStateText = findViewById(R.id.tv_empty_state)

        setupSpinner()
        setupCards()
        setupFilters()
        refreshCards()
    }

    // Build the region spinner from the list defined in strings.xml.
    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.region_options,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            regionSpinner.adapter = this
        }
    }

    // Grab all four card views and keep them in a list so we can loop through them easily.
    private fun setupCards() {
        cardViews.clear()
        cardViews += findViewById<View>(R.id.card_1)
        cardViews += findViewById<View>(R.id.card_2)
        cardViews += findViewById<View>(R.id.card_3)
        cardViews += findViewById<View>(R.id.card_4)
    }

    // Watch each filter control and re-draw the cards whenever something changes.
    private fun setupFilters() {
        regionSpinner.onItemSelectedListener = SimpleItemSelectedListener { refreshCards() }
        recentOnlyCheckBox.setOnCheckedChangeListener { _, _ -> refreshCards() }
        sortRadioGroup.setOnCheckedChangeListener { _, _ -> refreshCards() }
    }

    // Work out which locations to show based on the current filter and sort settings,
    // then update each card on screen.
    private fun refreshCards() {
        val selectedRegion = regionSpinner.selectedItem?.toString().orEmpty()
        val recentOnly = recentOnlyCheckBox.isChecked

        val filtered = locations
            .filter { selectedRegion == getString(R.string.region_all) || it.region == selectedRegion }
            .filter { !recentOnly || wasVisitedInLastYear(it.lastVisitDate) }

        val sorted = when (sortRadioGroup.checkedRadioButtonId) {
            R.id.rb_sort_rating -> filtered.sortedByDescending { it.rating }
            else -> filtered.sortedBy { it.name }
        }

        cardViews.forEachIndexed { index, card ->
            val item = sorted.getOrNull(index)
            if (item == null) {
                card.visibility = View.INVISIBLE
                card.isEnabled = false
            } else {
                bindCard(card, item)
                card.visibility = View.VISIBLE
                card.isEnabled = true
            }
        }

        // Show the empty-state message if nothing matched, otherwise hide it.
        emptyStateText.visibility = if (sorted.isEmpty()) View.VISIBLE else View.GONE
    }

    // Fill a single card with data from a LocationItem.
    // 'with' means we can call findViewById directly on the card without repeating it.
    private fun bindCard(card: View, item: LocationItem) {
        with(card) {
            findViewById<ImageView>(R.id.iv_location).setImageResource(item.imageResId)
            findViewById<TextView>(R.id.tv_location_title).text = item.name
            findViewById<RatingBar>(R.id.rb_location_rating).rating = item.rating
            findViewById<TextView>(R.id.tv_last_visit).text =
                getString(R.string.last_visit_format, item.lastVisitDate)
            setOnClickListener { openLocationDetail(item) }
        }
    }

    // Pack the selected location into an Intent and open the detail screen.
    // 'apply' lets us attach the extra to the Intent before passing it to startActivity.
    private fun openLocationDetail(item: LocationItem) {
        startActivity(Intent(this, LocationDetailActivity::class.java).apply {
            putExtra(LocationDetailActivity.EXTRA_LOCATION, item)
        })
    }

    // Check if the given date string falls within the last 12 months.
    private fun wasVisitedInLastYear(dateText: String): Boolean {
        val visitDate = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE)
        return visitDate.isAfter(LocalDate.now().minusYears(1))
    }
}

