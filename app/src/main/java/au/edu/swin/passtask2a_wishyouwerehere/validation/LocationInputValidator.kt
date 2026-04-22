package au.edu.swin.passtask2a_wishyouwerehere.validation

import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class LocationValidationError {
    NAME_REQUIRED,
    PLACE_REQUIRED,
    DATE_FORMAT,
    DATE_FUTURE,
    RATING_RANGE
}

object LocationInputValidator {
    fun validate(
        item: LocationItem,
        today: LocalDate = LocalDate.now()
    ): LocationValidationError? {
        if (item.name.isBlank()) return LocationValidationError.NAME_REQUIRED
        if (item.cityStateCountry.isBlank()) return LocationValidationError.PLACE_REQUIRED

        val parsedDate = runCatching {
            LocalDate.parse(item.lastVisitDate, DateTimeFormatter.ISO_LOCAL_DATE)
        }.getOrNull() ?: return LocationValidationError.DATE_FORMAT

        if (parsedDate.isAfter(today)) return LocationValidationError.DATE_FUTURE
        if (item.rating !in 0f..5f) return LocationValidationError.RATING_RANGE

        return null
    }
}

