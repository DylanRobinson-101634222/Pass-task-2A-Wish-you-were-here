package au.edu.swin.passtask2a_wishyouwerehere

import android.os.Bundle
import android.os.Build
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Set up a persistent toolbar with a back/up button.
        val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
        }

        // Retrieve the LocationItem passed via the intent.
        // Uses the modern API on Tiramisu+ and the legacy API on older devices.
        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_LOCATION, LocationItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LOCATION)
        }

        // If no location was passed, close the activity straight away.
        if (location == null) {
            finish()
            return
        }

        // Bind all location details to views.
        // Uses Kotlin's 'with' scope function to avoid repeating 'location.'.
        with(location) {
            findViewById<ImageView>(R.id.iv_detail_location).setImageResource(imageResId)
            findViewById<TextView>(R.id.tv_detail_name).text = name
            findViewById<TextView>(R.id.tv_detail_place).text = cityStateCountry
            findViewById<TextView>(R.id.tv_detail_date).text =
                getString(R.string.last_visit_format, lastVisitDate)
            // RatingBar is a non-TextView widget — its value is set directly from the Parcelable.
            findViewById<RatingBar>(R.id.rb_detail_rating).rating = rating
        }
    }

    // Navigate back to MainActivity when the up button is pressed.
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
    }
}
