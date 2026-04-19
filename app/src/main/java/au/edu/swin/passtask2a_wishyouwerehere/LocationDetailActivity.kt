package au.edu.swin.passtask2a_wishyouwerehere

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val location = intent.getParcelableExtra<LocationItem>(EXTRA_LOCATION)
        if (location == null) {
            finish()
            return
        }

        findViewById<ImageView>(R.id.iv_detail_location).setImageResource(location.imageResId)
        findViewById<TextView>(R.id.tv_detail_name).text = location.name
        findViewById<TextView>(R.id.tv_detail_place).text = location.cityStateCountry
        findViewById<TextView>(R.id.tv_detail_date).text =
            getString(R.string.last_visit_format, location.lastVisitDate)
        findViewById<RatingBar>(R.id.rb_detail_rating).rating = location.rating
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
    }
}

