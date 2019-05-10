@file:Suppress("DEPRECATION")

package com.yfujiki.wiremocksample

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import android.content.Intent
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.apache.commons.io.IOUtils
import java.io.File


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

    private var webServer: MockWebServer? = null

    @Before
    fun setUp() {
        webServer = MockWebServer()
        webServer!!.dispatcher = RequestDispatcher()
        webServer!!.start(
            8000
        ) // Beware of the port number that is occupied on CI etc.
    }

    @After
    fun tearDown() {
        webServer?.shutdown()
    }

    @Test
    fun openMainActivity() {
        activityTestRule.launchActivity(Intent())

        onView(withId(R.id.recyclerView)).check { view, noViewFoundException ->
            Assert.assertNotNull("RecyclerView should not be null.", view)
        }

        onView(withRecyclerView(R.id.recyclerView).atPosition(0)).check { view, noViewFoundException ->
            val titleTextView = view.findViewById<TextView>(R.id.textView)
            Assert.assertEquals("Fall in Love at First Kiss", titleTextView.text)
        }
    }

    class RequestDispatcher() : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {

            if (request.path.startsWith("/3/movie/now_playing", true)) {
                val reader = InstrumentationRegistry.getContext().assets.open("movies.json").bufferedReader()
                val jsonText = IOUtils.toString(reader)
                return MockResponse().setResponseCode(200).setBody(jsonText)
            }

            return MockResponse().setResponseCode(404)
        }
    }
}

