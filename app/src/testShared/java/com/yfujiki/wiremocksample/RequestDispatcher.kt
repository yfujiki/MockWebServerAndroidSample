package com.yfujiki.wiremocksample

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.File

internal class RequestDispatcher : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {

        if (request.path.startsWith("/3/movie/now_playing", true)) {
            val file = File(this::class.java.classLoader.getResource("movies.json").path)
            val jsonFile = file.readText()
            return MockResponse().setResponseCode(200).setBody(jsonFile)
        }

        return MockResponse().setResponseCode(404)
    }
}
