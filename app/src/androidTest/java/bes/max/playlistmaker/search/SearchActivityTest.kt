package bes.max.playlistmaker.search

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import bes.max.playlistmaker.R
import bes.max.playlistmaker.presentation.search.TrackListItemAdapter
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchActivityTest {

//    private val activityScenario: ActivityScenario<SearchActivity>
//
//    init {
//        Intents.init()
//        activityScenario = ActivityScenario.launch(SearchActivity::class.java)
//    }
//
//    @After
//    fun cleanUp() {
//        Intents.release()
//        activityScenario.close()
//    }
//
//    @Test
//    fun backIconFinishesSearchActivity() {
//        val testScenarioWithResult =
//            ActivityScenario.launchActivityForResult(SearchActivity::class.java)
//        onView(withId(R.id.search_activity_back_icon)).perform(click())
//
//        assertEquals(testScenarioWithResult.state, Lifecycle.State.DESTROYED)
//    }
//
//    @Test
//    fun shouldShowHistoryWhenEdiTextIsFocused() {
//        onView(withId(R.id.search_screen_edit_text)).perform(click())
//
//        onView(withId(R.id.search_screen_history_scroll_view)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun searchRequestGivesListOfTracks() {
//
//    }
//
//    @Test
//    fun clickOnItemInSearchResultOpensPlayer() {
//        onView(withId(R.id.search_screen_edit_text)).perform(typeText("kovacs digging"))
//
//        sleep(3000)
//
//        onView(withId(R.id.search_screen_recycler_view_tracks)).perform(
//            RecyclerViewActions.actionOnItem<TrackListItemAdapter.TrackViewHolder>(
//                hasDescendant(withText("Diggin'")), click()
//            )
//        )
//
//        Intents.intending(IntentMatchers.hasComponent(ComponentNameMatchers.hasShortClassName(".PlayerActivity")))
//    }
}