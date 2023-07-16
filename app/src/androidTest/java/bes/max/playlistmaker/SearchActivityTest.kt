package bes.max.playlistmaker

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import bes.max.playlistmaker.presentation.search.SearchActivity
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchActivityTest {

    private val activityScenario: ActivityScenario<SearchActivity>

    init {
        Intents.init()
        activityScenario = ActivityScenario.launch(SearchActivity::class.java)
    }

    @After
    fun cleanUp() {
        Intents.release()
        activityScenario.close()
    }

    @Test
    fun backIconFinishesSearchActivity() {
        val testScenarioWithResult = ActivityScenario.launchActivityForResult(SearchActivity::class.java)
        onView(withId(R.id.search_activity_back_icon)).perform(click())

        assertEquals(testScenarioWithResult.state, Lifecycle.State.DESTROYED)
    }
}