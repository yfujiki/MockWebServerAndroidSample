@file:Suppress("DEPRECATION")

package com.yfujiki.wiremocksample

import android.widget.TextView
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import java.net.InetAddress

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private var webServer: MockWebServer? = null

    @Before
    fun setUp() {
        webServer = MockWebServer()
        webServer!!.dispatcher = RequestDispatcher()
        webServer!!.start(InetAddress.getLoopbackAddress(), 8000) // Beware of the port number that is occupied on CI etc.
    }

    @After
    fun tearDown() {
        webServer?.shutdown()
    }

    @Test
    fun openMainActivity() {
        onView(withId(R.id.recyclerView)).check { view, noViewFoundException ->
            Assert.assertNotNull("RecyclerView should not be null.", view)
        }

        onView(withRecyclerView(R.id.recyclerView).atPosition(0)).check { view, noViewFoundException ->
            val titleTextView = view.findViewById<TextView>(R.id.textView)
            Assert.assertEquals("Our Dance of Revolution", titleTextView.text)
        }
    }
}