package au.edu.swin.passtask2a_wishyouwerehere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationItem(
    val name: String,
    val cityStateCountry: String,
    val lastVisitDate: String,
    val rating: Float,
    val imageResId: Int,
    val region: String
) : Parcelable

