package au.edu.swin.passtask2a_wishyouwerehere.model

import android.os.Parcel
import android.os.Parcelable

data class LocationItem(
    val name: String,
    val cityStateCountry: String,
    val lastVisitDate: String,
    val rating: Float,
    val imageResId: Int,
    val region: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        name = parcel.readString().orEmpty(),
        cityStateCountry = parcel.readString().orEmpty(),
        lastVisitDate = parcel.readString().orEmpty(),
        rating = parcel.readFloat(),
        imageResId = parcel.readInt(),
        region = parcel.readString().orEmpty()
    )

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

