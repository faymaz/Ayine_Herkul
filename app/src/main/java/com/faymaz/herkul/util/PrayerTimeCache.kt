package com.faymaz.herkul.util

import android.content.Context
import com.faymaz.herkul.model.PrayerTime
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Disk cache for prayer times, stored per city ID.
 *
 * Format: one entry per line, fields tab-separated:
 *   date \t hijriDate \t imsak \t gunes \t ogle \t ikindi \t aksam \t yatsi
 *
 * Cache is considered valid as long as it contains an entry for today
 * (or at least one entry whose date is in the future), meaning the yearly
 * dataset from Diyanet has not yet expired.
 */
object PrayerTimeCache {

    private fun cacheFile(context: Context, cityId: Int): File =
        File(context.filesDir, "prayer_$cityId.tsv")

    /** Save a full year's data for a city. */
    fun save(context: Context, cityId: Int, times: List<PrayerTime>) {
        if (times.isEmpty()) return
        cacheFile(context, cityId).bufferedWriter(Charsets.UTF_8).use { w ->
            for (t in times) {
                w.write("${t.date}\t${t.hijriDate}\t${t.imsak}\t${t.gunes}\t${t.ogle}\t${t.ikindi}\t${t.aksam}\t${t.yatsi}")
                w.newLine()
            }
        }
    }

    /**
     * Load cached prayer times for a city.
     * Returns null if there is no cache or if the cache no longer covers today
     * (i.e. the data has expired and a fresh fetch is needed).
     */
    fun load(context: Context, cityId: Int): List<PrayerTime>? {
        val file = cacheFile(context, cityId)
        if (!file.exists()) return null

        val times = try {
            file.bufferedReader(Charsets.UTF_8).useLines { lines ->
                lines.mapNotNull { line ->
                    val p = line.split('\t')
                    if (p.size == 8) PrayerTime(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7])
                    else null
                }.toList()
            }
        } catch (_: Exception) {
            return null
        }

        if (times.isEmpty()) return null
        // Always sort chronologically — old cache files may have been saved with
        // an incorrect alphabetic sort (Turkish month names sorted as strings).
        val sorted = times.sortedBy { parseToEpoch(it.date) }
        return if (isValid(sorted)) sorted else null
    }

    private fun parseToEpoch(dateStr: String): Long {
        val clean = dateStr.split(" ").take(3).joinToString(" ")
        return try {
            SimpleDateFormat("d MMMM yyyy", Locale("tr")).parse(clean)?.time ?: 0L
        } catch (_: Exception) { 0L }
    }

    /** Delete cached data for a city (force re-fetch). */
    fun invalidate(context: Context, cityId: Int) {
        cacheFile(context, cityId).delete()
    }

    // ── Internal ─────────────────────────────────────────────────────────────

    /**
     * Cache is valid when it still contains today's entry.
     * Diyanet pages serve ~365 days from the fetch date, so once today's
     * row disappears from the cache the yearly window has rolled over.
     */
    private fun isValid(times: List<PrayerTime>): Boolean {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH).toString()
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale("tr")).format(cal.time)
        return times.any { pt ->
            pt.date.startsWith("$day ") && pt.date.contains(monthYear)
        }
    }
}
