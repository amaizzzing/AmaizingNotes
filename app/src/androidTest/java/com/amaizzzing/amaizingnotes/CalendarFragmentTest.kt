package com.amaizzzing.amaizingnotes

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.amaizzzing.amaizingnotes.view.fragments.CalendarFragment
import org.junit.Test


class CalendarFragmentTest {
    @Test
    fun sampleTesting(){
        launchFragmentInContainer<CalendarFragment>()
        onView(withId(R.id.fab_button_fragment_calendar)).perform(click())
    }

    @Test
    fun testEventFragment() {
        val scenario = launchFragmentInContainer<CalendarFragment>()
        onView(withId(R.id.search_note_fragment_calendar)).check(matches(withText("")))
    }

}
