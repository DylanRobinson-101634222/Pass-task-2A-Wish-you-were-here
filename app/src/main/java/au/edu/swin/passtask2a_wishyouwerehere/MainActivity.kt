package au.edu.swin.passtask2a_wishyouwerehere

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    private val locations = listOf(
        LocationItem(
            name = "St Kilda Beach",
            cityStateCountry = "Melbourne, Victoria, Australia",
            lastVisitDate = "2026-03-12",
            rating = 4.5f,
            imageResId = R.drawable.location_st_kilda,
            region = "Victoria"
        ),
        LocationItem(
            name = "Royal Botanic Gardens",
            cityStateCountry = "Melbourne, Victoria, Australia",
            lastVisitDate = "2025-10-04",
            rating = 4.0f,
            imageResId = R.drawable.location_botanic_gardens,
            region = "Victoria"
        ),
        LocationItem(
            name = "Sydney Opera House",
            cityStateCountry = "Sydney, New South Wales, Australia",
            lastVisitDate = "2024-07-20",
            rating = 5.0f,
            imageResId = R.drawable.location_opera_house,
            region = "New South Wales"
        ),
        LocationItem(
            name = "South Bank",
            cityStateCountry = "Brisbane, Queensland, Australia",
            lastVisitDate = "2026-01-05",
            rating = 3.5f,
            imageResId = R.drawable.location_south_bank,
            region = "Queensland"
        )
    )

    private lateinit var regionSpinner: Spinner
    private lateinit var recentOnlyCheckBox: CheckBox
    private lateinit var sortRadioGroup: RadioGroup
    private lateinit var locationGrid: GridLayout

    private val cardViews = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regionSpinner = findViewById(R.id.spinner_region)
        recentOnlyCheckBox = findViewById(R.id.checkbox_recent_only)
        sortRadioGroup = findViewById(R.id.rg_sort)
        locationGrid = findViewById(R.id.location_grid)

        setupSpinner()
        setupCards()
        setupFilters()
        refreshCards()
    }

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

    private fun setupCards() {
        cardViews.clear()
        cardViews += findViewById<View>(R.id.card_1)
        cardViews += findViewById<View>(R.id.card_2)
        cardViews += findViewById<View>(R.id.card_3)
        cardViews += findViewById<View>(R.id.card_4)
    }

    private fun setupFilters() {
        regionSpinner.onItemSelectedListener = SimpleItemSelectedListener { refreshCards() }
        recentOnlyCheckBox.setOnCheckedChangeListener { _, _ -> refreshCards() }
        sortRadioGroup.setOnCheckedChangeListener { _, _ -> refreshCards() }
    }

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
    }

    private fun bindCard(card: View, item: LocationItem) {
        val imageView = card.findViewById<ImageView>(R.id.iv_location)
        val titleText = card.findViewById<TextView>(R.id.tv_location_title)
        val ratingBar = card.findViewById<RatingBar>(R.id.rb_location_rating)
        val dateText = card.findViewById<TextView>(R.id.tv_last_visit)

        imageView.setImageResource(item.imageResId)
        titleText.text = item.name
        ratingBar.rating = item.rating
        dateText.text = getString(R.string.last_visit_format, item.lastVisitDate)

        imageView.setOnClickListener {
            startActivity(Intent(this, LocationDetailActivity::class.java).apply {
                putExtra(LocationDetailActivity.EXTRA_LOCATION, item)
            })
        }
    }

    private fun wasVisitedInLastYear(dateText: String): Boolean {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val visitDate = LocalDate.parse(dateText, formatter)
        return visitDate.isAfter(LocalDate.now().minusYears(1))
    }
}

