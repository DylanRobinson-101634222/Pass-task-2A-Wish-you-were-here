package au.edu.swin.passtask2a_wishyouwerehere.validation

import au.edu.swin.passtask2a_wishyouwerehere.model.LocationItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class LocationInputValidatorTest {

    private val baseItem = LocationItem(
        name = "Uluru",
        cityStateCountry = "Northern Territory, Australia",
        lastVisitDate = "2025-01-01",
        rating = 4.5f,
        imageResId = 1,
        region = "Northern Territory"
    )

    @Test
    fun validate_returnsNull_whenInputIsValid() {
        val result = LocationInputValidator.validate(baseItem, today = LocalDate.of(2026, 4, 22))
        assertNull(result)
    }

    @Test
    fun validate_returnsNameRequired_whenNameIsBlank() {
        val result = LocationInputValidator.validate(
            baseItem.copy(name = "   "),
            today = LocalDate.of(2026, 4, 22)
        )

        assertEquals(LocationValidationError.NAME_REQUIRED, result)
    }

    @Test
    fun validate_returnsDateFormat_whenDateIsInvalid() {
        val result = LocationInputValidator.validate(
            baseItem.copy(lastVisitDate = "22/04/2026"),
            today = LocalDate.of(2026, 4, 22)
        )

        assertEquals(LocationValidationError.DATE_FORMAT, result)
    }

    @Test
    fun validate_returnsDateFuture_whenDateIsInFuture() {
        val result = LocationInputValidator.validate(
            baseItem.copy(lastVisitDate = "2026-12-31"),
            today = LocalDate.of(2026, 4, 22)
        )

        assertEquals(LocationValidationError.DATE_FUTURE, result)
    }

    @Test
    fun validate_returnsRatingRange_whenRatingTooHigh() {
        val result = LocationInputValidator.validate(
            baseItem.copy(rating = 5.5f),
            today = LocalDate.of(2026, 4, 22)
        )

        assertEquals(LocationValidationError.RATING_RANGE, result)
    }
}

