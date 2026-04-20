package au.edu.swin.passtask2a_wishyouwerehere

import android.os.Bundle
import android.os.Build
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)
        applyWindowInsets()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_LOCATION, LocationItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LOCATION)
        }
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

    private fun applyWindowInsets() {
        val scrollView = findViewById<android.view.View>(R.id.scroll_detail)
        val startPadding = scrollView.paddingLeft
        val topPadding = scrollView.paddingTop
        val endPadding = scrollView.paddingRight
        val bottomPadding = scrollView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                startPadding + bars.left,
                topPadding + bars.top,
                endPadding + bars.right,
                bottomPadding + bars.bottom
            )
            insets
        }
        ViewCompat.requestApplyInsets(scrollView)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
    }
}

