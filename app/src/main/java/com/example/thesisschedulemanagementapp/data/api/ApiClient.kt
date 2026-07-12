package com.example.thesisschedulemanagementapp.data.api

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

object ApiClient {
    const val EMULATOR_BASE_URL = "http://10.0.2.2/thesis_schedule_api/"
    const val LAN_BASE_URL_EXAMPLE = "http://192.168.1.44/thesis_schedule_api/"

    var baseUrl: String = EMULATOR_BASE_URL
        private set

    @PublishedApi
    internal val gson = Gson()
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 20_000
        }
    }

    fun updateBaseUrl(url: String) {
        baseUrl = if (url.endsWith("/")) url else "$url/"
    }

    suspend fun post(endpoint: String, body: Any): JsonObject {
        val response = client.post(baseUrl + endpoint) {
            contentType(ContentType.Application.Json)
            setBody(gson.toJson(body))
        }
        return parse(response.bodyAsText())
    }

    suspend fun get(endpoint: String, params: Map<String, Any?> = emptyMap()): JsonObject {
        val response = client.get(baseUrl + endpoint) {
            params.forEach { (key, value) ->
                if (value != null) parameter(key, value)
            }
        }
        return parse(response.bodyAsText())
    }

    fun success(json: JsonObject): Boolean = json.get("success")?.asBoolean == true

    fun message(json: JsonObject): String =
        json.get("message")?.asString ?: "Unexpected server response."

    fun data(json: JsonObject): JsonElement? = json.get("data")

    inline fun <reified T> fromData(json: JsonObject): T? {
        val data = data(json) ?: return null
        return gson.fromJson(data, T::class.java)
    }

    inline fun <reified T> listFromData(json: JsonObject): List<T> {
        val data = data(json) ?: return emptyList()
        val type = TypeToken.getParameterized(List::class.java, T::class.java).type
        return gson.fromJson(data, type)
    }

    private fun parse(text: String): JsonObject = try {
        gson.fromJson(text, JsonObject::class.java)
    } catch (e: Exception) {
        JsonObject().apply {
            addProperty("success", false)
            addProperty("message", "Invalid JSON response from server.")
        }
    }
}
