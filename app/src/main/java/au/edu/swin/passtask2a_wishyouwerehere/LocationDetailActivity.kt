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
import au.edu.swin.passtask2a_wishyouwerehere.validation.LocationInputValidator
import au.edu.swin.passtask2a_wishyouwerehere.validation.LocationValidationError
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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

        // Pre-fill the form with current values so the user has something to edit
        // rather than staring at blank fields.
        with(location) {
            findViewById<ImageView>(R.id.iv_detail_location).setImageResource(imageResId)
            nameInput.setText(name)
            placeInput.setText(cityStateCountry)
            dateInput.setText(lastVisitDate)
            ratingBar.rating = rating
        }

        // Intercept the system back button so we can save before leaving.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveAndFinish()
            }
        })
    }

    // Go back to the main screen when the back arrow in the toolbar is tapped.
    // This also triggers the save handler above, which validates and returns the data.
    override fun onSupportNavigateUp(): Boolean {
        saveAndFinish()
        return true
    }

    private fun saveAndFinish() {
        // Bail out early if we don't have the original location data.
        val original = originalLocation ?: run {
            finish()
            return
        }

        // Copy the location with the new form values the user entered.
        val updated = original.copy(
            name = nameInput.text?.toString().orEmpty().trim(),
            cityStateCountry = placeInput.text?.toString().orEmpty().trim(),
            lastVisitDate = dateInput.text?.toString().orEmpty().trim(),
            rating = ratingBar.rating
        )

        // Run validation using the shared validator object.
        // If there's an error, show a Snackbar and stay on screen.
        val errorMessage = validateInputs(updated)
        if (errorMessage != null) {
            Snackbar.make(findViewById(R.id.scroll_detail), errorMessage, Snackbar.LENGTH_LONG).show()
            return
        }

        // No errors — pack the updated item into the result intent and finish.
        setResult(RESULT_OK, Intent().apply {
            putExtra(EXTRA_LOCATION_INDEX, locationIndex)
            putExtra(EXTRA_UPDATED_LOCATION, updated)
        })
        finish()
    }

    private fun validateInputs(item: LocationItem): String? {
        // Clear all field errors first, then check each rule.
        nameLayout.error = null
        placeLayout.error = null
        dateLayout.error = null

        // Use the reusable validator to check the item.
        // Map the returned error type to a user-friendly Snackbar message.
        return when (LocationInputValidator.validate(item)) {
            LocationValidationError.NAME_REQUIRED -> {
                nameLayout.error = getString(R.string.error_name_required)
                getString(R.string.error_fix_fields)
            }
            LocationValidationError.PLACE_REQUIRED -> {
                placeLayout.error = getString(R.string.error_place_required)
                getString(R.string.error_fix_fields)
            }
            LocationValidationError.DATE_FORMAT -> {
                dateLayout.error = getString(R.string.error_date_format)
                getString(R.string.error_fix_fields)
            }
            LocationValidationError.DATE_FUTURE -> {
                dateLayout.error = getString(R.string.error_date_future)
                getString(R.string.error_fix_fields)
            }
            LocationValidationError.RATING_RANGE -> getString(R.string.error_rating_range)
            null -> null
        }
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
        const val EXTRA_LOCATION_INDEX = "extra_location_index"
        const val EXTRA_UPDATED_LOCATION = "extra_updated_location"
    }
}
