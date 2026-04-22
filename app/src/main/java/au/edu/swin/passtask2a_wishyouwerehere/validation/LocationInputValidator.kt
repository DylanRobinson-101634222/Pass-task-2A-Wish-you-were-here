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
    // Checks a location against a set of business rules.
    // Returns the first error found, or null if the item passes all checks.
    fun validate(
        item: LocationItem,
        today: LocalDate = LocalDate.now()
    ): LocationValidationError? {
        // Name cannot be empty.
        if (item.name.isBlank()) return LocationValidationError.NAME_REQUIRED
        // Place cannot be empty.
        if (item.cityStateCountry.isBlank()) return LocationValidationError.PLACE_REQUIRED

        // Date must be in valid ISO format (yyyy-mm-dd).
        val parsedDate = runCatching {
            LocalDate.parse(item.lastVisitDate, DateTimeFormatter.ISO_LOCAL_DATE)
        }.getOrNull() ?: return LocationValidationError.DATE_FORMAT

        // Date cannot be in the future.
        if (parsedDate.isAfter(today)) return LocationValidationError.DATE_FUTURE
        // Rating must be between 0 and 5 (inclusive).
        if (item.rating !in 0f..5f) return LocationValidationError.RATING_RANGE

        return null
    }
}

