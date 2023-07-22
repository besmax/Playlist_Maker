package bes.max.playlistmaker

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import bes.max.playlistmaker.presentation.settings.SettingsActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsActivityTest {

    private lateinit var activityScenario: ActivityScenario<SettingsActivity>

    @Before
    fun setUp() {
        Intents.init()
        activityScenario =
            ActivityScenario.launch(SettingsActivity::class.java)
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityScenario.close()
    }

    @Test
    fun shareIntentOpensChooser() {
        onView(withId(R.id.settings_activity_icon_share)).perform(click())

        val expectedIntent = Matchers.allOf(
            hasAction(Intent.ACTION_SEND),
            hasExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/"),
            hasType("text/plain")
        )
        intended(expectedIntent)
    }

    @Test
    fun supportIntentOpensChooser() {
        onView(withId(R.id.settings_activity_section_support)).perform(click())

        val expectedIntent = Matchers.allOf(
            hasAction(Intent.ACTION_SENDTO),
            hasExtra(Intent.EXTRA_EMAIL, arrayOf("bespalov.m.9@ya.ru")),
            hasExtra(
                Intent.EXTRA_TEXT,
                "Спасибо разработчикам и разработчицам за крутое приложение!"
            ),
            hasExtra(
                Intent.EXTRA_SUBJECT,
                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            )
        )

        intended( expectedIntent )
    }

    @Test
    fun openAgreementIntentOpensLink() {
        onView(withId(R.id.settings_activity_icon_agreement)).perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("https://practicum.yandex.ru/android-developer/"))
            )
        )
    }

    @Test
    fun clickOnBackIconFinishesActivity() {
        val testScenario = ActivityScenario.launchActivityForResult(SettingsActivity::class.java)
        onView(withId(R.id.back_icon)).perform(click())

        assertTrue(testScenario.state == Lifecycle.State.DESTROYED)
        assertTrue(testScenario.result.resultCode == Activity.RESULT_CANCELED)
    }

    private fun getChooser(matcher: Matcher<Intent>): Matcher<Intent> {
        return allOf(
            hasAction(Intent.ACTION_CHOOSER),
            hasExtra(`is`(Intent.EXTRA_INTENT), matcher)
        )
    }
}