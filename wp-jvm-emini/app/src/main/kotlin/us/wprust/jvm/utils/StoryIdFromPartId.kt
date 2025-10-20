package us.wprust.jvm.utils

import client
import jsonParser
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import okhttp3.Request
import okhttp3.coroutines.executeAsync

@Serializable
data class GroupIdResponse(val groupId: Int)

suspend fun getStoryIdFromPartId(partId: Int): Int? {
    val apiUrl = "https://www.wattpad.com/api/v3/story_parts/$partId?fields=groupId"
    val apiRequest = Request.Builder()
        .url(apiUrl)
        .header(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
        )
        .build()

    return try {
        val response = client.newCall(apiRequest).executeAsync()
        response.use {
            if (!it.isSuccessful) return null
            val body = it.body.string()
            jsonParser.decodeFromString<GroupIdResponse>(body).groupId
        }
    } catch (e: CancellationException) {
        throw e // Properly propagate coroutine cancellations
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}