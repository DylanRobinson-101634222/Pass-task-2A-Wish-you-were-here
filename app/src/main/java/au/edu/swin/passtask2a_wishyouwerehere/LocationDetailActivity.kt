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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocationDetailActivity : AppCompatActivity() {

    private var originalLocation: LocationItem? = null
    private var locationIndex: Int = -1

    private lateinit var nameInput: TextInputEditText
    private lateinit var placeInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var ratingBar: RatingBar
    private lateinit var nameLayout: TextInputLayout
    private lateinit var placeLayout: TextInputLayout
    private lateinit var dateLayout: TextInputLayout

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
        nameLayout = findViewById(R.id.til_detail_name)
        placeLayout = findViewById(R.id.til_detail_place)
        dateLayout = findViewById(R.id.til_detail_date)

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

        val errorMessage = validateInputs(updated)
        if (errorMessage != null) {
            Snackbar.make(findViewById(R.id.scroll_detail), errorMessage, Snackbar.LENGTH_LONG).show()
            return
        }

        setResult(RESULT_OK, Intent().apply {
            putExtra(EXTRA_LOCATION_INDEX, locationIndex)
            putExtra(EXTRA_UPDATED_LOCATION, updated)
        })
        finish()
    }

    private fun validateInputs(item: LocationItem): String? {
        nameLayout.error = null
        placeLayout.error = null
        dateLayout.error = null

        if (item.name.isBlank()) {
            nameLayout.error = getString(R.string.error_name_required)
            return getString(R.string.error_fix_fields)
        }

        if (item.cityStateCountry.isBlank()) {
            placeLayout.error = getString(R.string.error_place_required)
            return getString(R.string.error_fix_fields)
        }

        val parsedDate = runCatching {
            LocalDate.parse(item.lastVisitDate, DateTimeFormatter.ISO_LOCAL_DATE)
        }.getOrNull()

        if (parsedDate == null) {
            dateLayout.error = getString(R.string.error_date_format)
            return getString(R.string.error_fix_fields)
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            dateLayout.error = getString(R.string.error_date_future)
            return getString(R.string.error_fix_fields)
        }

        if (item.rating < 0f || item.rating > 5f) {
            return getString(R.string.error_rating_range)
        }

        return null
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
        const val EXTRA_LOCATION_INDEX = "extra_location_index"
        const val EXTRA_UPDATED_LOCATION = "extra_updated_location"
    }
}
