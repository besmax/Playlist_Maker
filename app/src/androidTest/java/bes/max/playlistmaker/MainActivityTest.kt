package bes.max.playlistmaker

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import bes.max.playlistmaker.presentation.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Intents.init()
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    @Test
    fun searchButtonOpensSearchActivity() {
        onView(withId(R.id.button_search)).perform(click())

        intending(hasComponent(hasShortClassName(".SearchActivity")))
    }

    @Test
    fun settingsButtonOpensSettingsActivity() {
        onView(withId(R.id.button_settings)).perform(click())

        intending(hasComponent(hasShortClassName(".SettingsActivity")))
    }

    @Test
    fun mediatekaButtonOpensMediatekaActivity() {
        onView(withId(R.id.button_mediateka)).perform(click())

        intending(hasComponent(hasShortClassName(".MediatekaActivity")))
    }
}