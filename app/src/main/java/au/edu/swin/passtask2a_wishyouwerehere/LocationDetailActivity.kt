package au.edu.swin.passtask2a_wishyouwerehere

import android.content.Intent
import android.os.Bundle
import android.os.Build
import android.widget.ImageView
import android.widget.RatingBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem
import com.google.android.material.textfield.TextInputEditText

class LocationDetailActivity : AppCompatActivity() {

    private var originalLocation: LocationItem? = null
    private var locationIndex: Int = -1

    private lateinit var nameInput: TextInputEditText
    private lateinit var placeInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Wire up the toolbar so it acts as the action bar for this screen.
        val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
        setSupportActionBar(toolbar)

        nameInput = findViewById(R.id.et_detail_name)
        placeInput = findViewById(R.id.et_detail_place)
        dateInput = findViewById(R.id.et_detail_date)
        ratingBar = findViewById(R.id.rb_detail_rating)

        // Read the location that was passed from the main screen.
        // Android 13+ has a newer typed API; older devices use the legacy one.
        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_LOCATION, LocationItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LOCATION)
        }
        locationIndex = intent.getIntExtra(EXTRA_LOCATION_INDEX, -1)

        // Nothing to show if no location came through — go back straight away.
        if (location == null || locationIndex < 0) {
            finish()
            return
        }
        originalLocation = location

        // Show the location name in the toolbar and add the back arrow.
        supportActionBar?.apply {
            title = location.name
            setDisplayHomeAsUpEnabled(true)
        }

        // Fill the form with current values so the user can edit existing details.
        with(location) {
            findViewById<ImageView>(R.id.iv_detail_location).setImageResource(imageResId)
            nameInput.setText(name)
            placeInput.setText(cityStateCountry)
            dateInput.setText(lastVisitDate)
            ratingBar.rating = rating
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveAndFinish()
            }
        })
    }

    // Go back to the main screen when the back arrow in the toolbar is tapped.
    override fun onSupportNavigateUp(): Boolean {
        saveAndFinish()
        return true
    }

    private fun saveAndFinish() {
        val original = originalLocation ?: run {
            finish()
            return
        }

        val updated = original.copy(
            name = nameInput.text?.toString().orEmpty().trim(),
            cityStateCountry = placeInput.text?.toString().orEmpty().trim(),
            lastVisitDate = dateInput.text?.toString().orEmpty().trim(),
            rating = ratingBar.rating
        )

        setResult(RESULT_OK, Intent().apply {
            putExtra(EXTRA_LOCATION_INDEX, locationIndex)
            putExtra(EXTRA_UPDATED_LOCATION, updated)
        })
        finish()
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
        const val EXTRA_LOCATION_INDEX = "extra_location_index"
        const val EXTRA_UPDATED_LOCATION = "extra_updated_location"
    }
}
