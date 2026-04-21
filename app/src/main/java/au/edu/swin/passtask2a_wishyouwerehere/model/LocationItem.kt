package au.edu.swin.passtask2a_wishyouwerehere.model

import android.os.Parcel
import android.os.Parcelable

// Holds all the data for a single location card.
// Parcelable lets Android pass this object between activities via an Intent.
data class LocationItem(
    val name: String,
    val cityStateCountry: String,
    val lastVisitDate: String,   // stored as ISO format, e.g. 2025-09-15
    val rating: Float,           // 0.0 to 5.0 in 0.5 steps
    val imageResId: Int,         // drawable resource ID for the location photo
    val region: String           // used by the region spinner filter
) : Parcelable {

    // Read each field back out of the Parcel in the same order they were written.
    private constructor(parcel: Parcel) : this(
        name = parcel.readString().orEmpty(),
        cityStateCountry = parcel.readString().orEmpty(),
        lastVisitDate = parcel.readString().orEmpty(),
        rating = parcel.readFloat(),
        imageResId = parcel.readInt(),
        region = parcel.readString().orEmpty()
    )

    // Write each field into the Parcel so Android can carry it across to the detail screen.
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(cityStateCountry)
        parcel.writeString(lastVisitDate)
        parcel.writeFloat(rating)
        parcel.writeInt(imageResId)
        parcel.writeString(region)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<LocationItem> {
        override fun createFromParcel(parcel: Parcel): LocationItem = LocationItem(parcel)
        override fun newArray(size: Int): Array<LocationItem?> = arrayOfNulls(size)
    }
}
