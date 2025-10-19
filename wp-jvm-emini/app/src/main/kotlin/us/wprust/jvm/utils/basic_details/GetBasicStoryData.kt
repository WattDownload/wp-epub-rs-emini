import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.coroutines.executeAsync
import us.wprust.jvm.utils.basic_details.Story
import us.wprust.jvm.utils.basic_details.User

val client = OkHttpClient()
val jsonParser = Json { ignoreUnknownKeys = true }

@Serializable
private data class StoryApiResponse(
    val title: String, val user: User, val cover: String, val mature: Boolean
)

suspend fun getStoryDataAsync(storyId: String): Story? {
    val apiUrl = "https://www.wattpad.com/api/v3/stories/$storyId?fields=title,user(name),cover,mature"
    val apiRequest = Request.Builder().url(apiUrl).header(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36"
        ).build()

    return try {
        val response = client.newCall(apiRequest).executeAsync()
        val apiResponse = response.use {
            if (!it.isSuccessful) return null
            val body = it.body.string()
            jsonParser.decodeFromString<StoryApiResponse>(body)
        }

        // Download image
        val imageRequest = Request.Builder().url(apiResponse.cover).build()
        val imageResponse = client.newCall(imageRequest).executeAsync()

        val imageBytes = imageResponse.use {
            if (!it.isSuccessful) return null
            it.body.bytes()
        }

        Story(
            title = apiResponse.title, user = apiResponse.user, mature = apiResponse.mature, coverData = imageBytes
        )

    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}