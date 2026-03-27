package com.faymaz.herkul.util

import com.faymaz.herkul.model.PrayerTime
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object PrayerTimeFetcher {

    private val client = OkHttpClient.Builder()
        .connectTimeout(35, TimeUnit.SECONDS)
        .readTimeout(35, TimeUnit.SECONDS)
        .build()

    /**
     * Fetches all prayer times from the Diyanet page for the given city URL.
     * Retries up to 3 times with a random delay to work around the F5 WAF.
     */
    fun fetchAllPrayerTimes(cityUrl: String): List<PrayerTime> {
        var lastException: Exception? = null
        repeat(3) { attempt ->
            try {
                if (attempt > 0) Thread.sleep(3000L + (Math.random() * 3000).toLong())
                val request = Request.Builder()
                    .url(cityUrl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Cache-Control", "max-age=0")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "none")
                    .header("Sec-Fetch-User", "?1")
                    .header("Upgrade-Insecure-Requests", "1")
                    .build()
                val response = client.newCall(request).execute()
                val html = response.body?.string() ?: return emptyList()
                val result = parseRows(html)
                if (result.isNotEmpty()) return result
            } catch (e: Exception) {
                lastException = e
            }
        }
        throw lastException ?: Exception("Veri alınamadı")
    }

    private fun parseRows(html: String): List<PrayerTime> {
        val doc = Jsoup.parse(html)
        val result = mutableListOf<PrayerTime>()
        val seenDates = mutableSetOf<String>()

        // Collect all <tr> from all <tbody> elements
        val allRows = doc.select("tbody tr")

        for (row in allRows) {
            val cells = row.select("td")

            // Only use len=8 rows:
            // [0] Miladi Tarih  [1] Hicri Tarih
            // [2] İmsak  [3] Güneş  [4] Öğle  [5] İkindi  [6] Akşam  [7] Yatsı
            if (cells.size != 8) continue

            val miladi = cells[0].text().trim()
            val hicri  = cells[1].text().trim()
            val imsak  = cells[2].text().trim()
            val gunes  = cells[3].text().trim()
            val ogle   = cells[4].text().trim()
            val ikindi = cells[5].text().trim()
            val aksam  = cells[6].text().trim()
            val yatsi  = cells[7].text().trim()

            // Validate: first cell must be a date (not a time), prayer times must be HH:MM
            if (!isValidDate(miladi)) continue
            if (!isValidTime(imsak) || !isValidTime(yatsi)) continue

            // Deduplicate by Gregorian date
            if (seenDates.contains(miladi)) continue
            seenDates.add(miladi)

            result.add(
                PrayerTime(
                    date      = miladi,
                    hijriDate = hicri,
                    imsak     = imsak,
                    gunes     = gunes,
                    ogle      = ogle,
                    ikindi    = ikindi,
                    aksam     = aksam,
                    yatsi     = yatsi
                )
            )
        }

        return result.sortedBy { parseToEpoch(it.date) }
    }

    // Parse "26 Mart 2026 Salı" → epoch ms for chronological sorting.
    // Strips the day-of-week suffix before parsing.
    private fun parseToEpoch(dateStr: String): Long {
        // "26 Mart 2026 Salı" → take first 3 tokens: "26 Mart 2026"
        val clean = dateStr.split(" ").take(3).joinToString(" ")
        return try {
            SimpleDateFormat("d MMMM yyyy", Locale("tr")).parse(clean)?.time ?: 0L
        } catch (_: Exception) { 0L }
    }

    private fun isValidTime(s: String): Boolean = Regex("^\\d{2}:\\d{2}$").matches(s)

    // Gregorian date looks like "24 Mart 2026 Salı" — starts with a day number
    private fun isValidDate(s: String): Boolean = s.isNotEmpty() && s[0].isDigit()
}
