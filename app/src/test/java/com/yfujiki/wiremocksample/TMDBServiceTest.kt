package com.yfujiki.wiremocksample

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.AssertionError
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import okhttp3.mockwebserver.MockWebServer


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TMDBServiceTest {
    private var service: TMDBService? = null

    private var webServer: MockWebServer? = null

    @Before
    fun setUp() {

        webServer = MockWebServer()
        webServer?.dispatcher = RequestDispatcher()
        webServer?.start()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://127.0.0.1:${webServer!!.port}")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        service = retrofit.create<TMDBService>(TMDBService::class.java)

    }


    @After
    fun tearDown() {
        webServer?.shutdown()
    }

    @Test
    fun testNowPlaying() {
        val latch = CountDownLatch(1)
        val failure = AtomicReference<AssertionError>()

        service!!.nowplaying(1).subscribe({ moviesPage ->
            try {
                assertEquals(2, moviesPage.page)
                assertEquals(55, moviesPage.totalPages)
                assertEquals(1089, moviesPage.totalResults)
                assertEquals(20, moviesPage.resultsCount)
            } catch (assertionError: AssertionError) {
                assertionError.printStackTrace()
                failure.set(assertionError)
            } finally {
                latch.countDown()
            }
        }, { error ->
            try {
                error.printStackTrace()
                assertTrue("Failed with error : $error", false)
            } catch (assertionError: AssertionError) {
                failure.set(assertionError)
            } finally {
                latch.countDown()
            }
        })


        latch.await(3, TimeUnit.SECONDS)

        if (failure.get() != null) {
            assertFalse(true)
            throw failure.get()
        }
    }

}
