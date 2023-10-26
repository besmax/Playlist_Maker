package bes.max.playlistmaker

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import bes.max.playlistmaker.presentation.main.MainActivity
import org.junit.After
import org.junit.Before
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

}