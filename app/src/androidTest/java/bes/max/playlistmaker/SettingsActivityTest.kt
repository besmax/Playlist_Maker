package bes.max.playlistmaker

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsActivityTest {

    lateinit var activityScenario: ActivityScenario<SettingsActivity>

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

        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
            )
        )
    }

    @Test
    fun supportIntentOpensChooser() {
        onView(withId(R.id.settings_activity_icon_support)).perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(Intent.EXTRA_EMAIL, arrayOf("bespalov.m.9@ya.ru"))
            )
        )
    }

    @Test
    fun openAgreementIntentOpensChooser() {
        onView(withId(R.id.settings_activity_icon_agreement)).perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse("https://practicum.yandex.ru/android-developer/"))
            )
        )
    }
}