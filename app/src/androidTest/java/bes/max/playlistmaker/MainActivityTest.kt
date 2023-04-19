package bes.max.playlistmaker

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
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
        val searchIntent = Intent(this, SearchActivity::class.java)
    }
}