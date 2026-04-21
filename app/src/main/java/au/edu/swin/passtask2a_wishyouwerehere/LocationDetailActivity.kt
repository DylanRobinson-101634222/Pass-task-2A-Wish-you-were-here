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

        // Wire up the toolbar so it acts as the action bar for this screen.
        val toolbar = findViewById<Toolbar>(R.id.toolbar_detail)
        setSupportActionBar(toolbar)

        // Read the location that was passed from the main screen.
        // Android 13+ has a newer typed API; older devices use the legacy one.
        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_LOCATION, LocationItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LOCATION)
        }

        // Nothing to show if no location came through — go back straight away.
        if (location == null) {
            finish()
            return
        }

        // Show the location name in the toolbar and add the back arrow.
        supportActionBar?.apply {
            title = location.name
            setDisplayHomeAsUpEnabled(true)
        }

        // Fill in all the detail views from the location data.
        // 'with' lets us reference each field directly without writing 'location.' every time.
        with(location) {
            findViewById<ImageView>(R.id.iv_detail_location).setImageResource(imageResId)
            findViewById<TextView>(R.id.tv_detail_name).text = name
            findViewById<TextView>(R.id.tv_detail_place).text = cityStateCountry
            findViewById<TextView>(R.id.tv_detail_date).text =
                getString(R.string.last_visit_format, lastVisitDate)
            // RatingBar shows the star rating — set here so it reflects the correct value on load.
            findViewById<RatingBar>(R.id.rb_detail_rating).rating = rating
        }
    }

    // Go back to the main screen when the back arrow in the toolbar is tapped.
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
    }
}
