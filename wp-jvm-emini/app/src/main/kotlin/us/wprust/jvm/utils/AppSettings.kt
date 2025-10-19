package us.wprust.jvm.utils

import java.util.prefs.Preferences

object AppSettings {

    private val prefs: Preferences = Preferences.userRoot().node("wattdownload")
    private const val KEY_CONCURRENT_CHAPTER_COUNT = "concurrentChapterCount"
    private const val DEFAULT_CONCURRENT_CHAPTER_COUNT = 20U
    val allowedConcurrencyValues = arrayOf(10U, 20U, 30U, 40U, 50U)

    var concurrentChapterCount: UInt
        get() {
            val value = prefs.getInt(KEY_CONCURRENT_CHAPTER_COUNT, DEFAULT_CONCURRENT_CHAPTER_COUNT.toInt())
            if (!prefs.keys().contains(KEY_CONCURRENT_CHAPTER_COUNT)) {
                prefs.putInt(KEY_CONCURRENT_CHAPTER_COUNT, DEFAULT_CONCURRENT_CHAPTER_COUNT.toInt())
            }
            return value.toUInt()
        }
        set(value) {
            if (value in allowedConcurrencyValues) {
                prefs.putInt(KEY_CONCURRENT_CHAPTER_COUNT, value.toInt())
            } else {
                throw IllegalArgumentException("Invalid concurrentChapterCount: $value")
            }
        }

}