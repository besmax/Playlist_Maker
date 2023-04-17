package bes.max.playlistmaker

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsActivityTest {

    @get:Rule
    var sActivityRule = IntentsTestRule(SettingsActivity::class.java)

    @Before
    fun stubIntents() {
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }

    @Test
    fun testShareIntent() {
        onView(withId(R.id.settings_activity_icon_share)).perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
            )
        )
    }

    @Test
    fun testSendEmailIntent() {
        onView(withId(R.id.settings_activity_icon_support)).perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
            )
        )
    }

    @Test
    fun testOpenAgreementIntent() {
        onView(withId(R.id.settings_activity_icon_agreement)).perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_SEND),
                hasData(Uri.parse("https://practicum.yandex.ru/android-developer/"))
            )
        )
    }

}