package au.edu.swin.passtask2a_wishyouwerehere

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditLocationFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun editName_thenBack_showsUpdatedNameOnMainScreen() {
        val updatedName = "Uluru Updated"

        onView(withId(R.id.card_1)).perform(click())
        onView(withId(R.id.et_detail_name)).perform(replaceText(updatedName), closeSoftKeyboard())
        pressBack()

        onView(withText(updatedName)).check(matches(isDisplayed()))
    }
}

