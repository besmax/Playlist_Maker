package bes.max.playlistmaker

import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsActivityTest {

//    private lateinit var activityScenario: ActivityScenario<SettingsActivity>
//
//    @Before
//    fun setUp() {
//        Intents.init()
//        activityScenario =
//            ActivityScenario.launch(SettingsActivity::class.java)
//    }
//
//    @After
//    fun cleanUp() {
//        Intents.release()
//        activityScenario.close()
//    }
//
//    @Test
//    fun shareIntentOpensChooser() {
//        onView(withId(R.id.settings_screen_icon_share)).perform(click())
//
//        val expectedIntent = Matchers.allOf(
//            hasAction(Intent.ACTION_SEND),
//            hasExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/"),
//            hasType("text/plain")
//        )
//        intended(expectedIntent)
//    }
//
//    @Test
//    fun supportIntentOpensChooser() {
//        onView(withId(R.id.settings_screen_section_support)).perform(click())
//
//        val expectedIntent = Matchers.allOf(
//            hasAction(Intent.ACTION_SENDTO),
//            hasExtra(Intent.EXTRA_EMAIL, arrayOf("bespalov.m.9@ya.ru")),
//            hasExtra(
//                Intent.EXTRA_TEXT,
//                "Спасибо разработчикам и разработчицам за крутое приложение!"
//            ),
//            hasExtra(
//                Intent.EXTRA_SUBJECT,
//                "Сообщение разработчикам и разработчицам приложения Playlist Maker"
//            )
//        )
//
//        intended( expectedIntent )
//    }
//
//    @Test
//    fun openAgreementIntentOpensLink() {
//        onView(withId(R.id.settings_screen_icon_agreement)).perform(click())
//
//        intended(
//            allOf(
//                hasAction(Intent.ACTION_VIEW),
//                hasData(Uri.parse("https://practicum.yandex.ru/android-developer/"))
//            )
//        )
//    }
//
//    @Test
//    fun clickOnBackIconFinishesActivity() {
//        val testScenario = ActivityScenario.launchActivityForResult(SettingsActivity::class.java)
//        onView(withId(R.id.back_icon)).perform(click())
//
//        assertTrue(testScenario.state == Lifecycle.State.DESTROYED)
//        assertTrue(testScenario.result.resultCode == Activity.RESULT_CANCELED)
//    }
//
//    private fun getChooser(matcher: Matcher<Intent>): Matcher<Intent> {
//        return allOf(
//            hasAction(Intent.ACTION_CHOOSER),
//            hasExtra(`is`(Intent.EXTRA_INTENT), matcher)
//        )
//    }
}