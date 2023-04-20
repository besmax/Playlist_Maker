package bes.max.playlistmaker

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    val activityScenario: ActivityScenario<MainActivity>

    init {
        Intents.init()
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun searchButtonOpensSearchActivity() {
        //val searchIntent = Intent(this, SearchActivity::class.java)
        onView(withId(R.id.button_search)).perform(click())
    }
}