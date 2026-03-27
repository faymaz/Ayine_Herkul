package com.faymaz.herkul.util

import com.faymaz.herkul.model.PrayerTime
import com.faymaz.herkul.model.PrayerName
import java.util.Calendar

object PrayerCountdown {

    data class NextPrayer(val name: String, val time: String, val countdownText: String)

    /**
     * Given today's prayer times, returns the next prayer and countdown string.
     */
    fun getNextPrayer(today: PrayerTime): NextPrayer? {
        val now = Calendar.getInstance()
        val nowMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

        val prayers = listOf(
            PrayerName.IMSAK  to today.imsak,
            PrayerName.GUNES  to today.gunes,
            PrayerName.OGLE   to today.ogle,
            PrayerName.IKINDI to today.ikindi,
            PrayerName.AKSAM  to today.aksam,
            PrayerName.YATSI  to today.yatsi
        )

        for ((name, time) in prayers) {
            val prayerMinutes = parseTimeToMinutes(time) ?: continue
            if (prayerMinutes > nowMinutes) {
                val diff = prayerMinutes - nowMinutes
                val h = diff / 60
                val m = diff % 60
                val countdownText = if (h > 0) "${h} saat ${m} dk" else "${m} dakika"
                return NextPrayer(name.displayName, time, countdownText)
            }
        }
        // All prayers passed, next is tomorrow's Imsak
        val imsakMinutes = parseTimeToMinutes(today.imsak) ?: return null
        val minutesUntilMidnight = 1440 - nowMinutes
        val diff = minutesUntilMidnight + imsakMinutes
        val h = diff / 60
        val m = diff % 60
        return NextPrayer(PrayerName.IMSAK.displayName, today.imsak, "${h} saat ${m} dk (yarın)")
    }

    private fun parseTimeToMinutes(time: String): Int? {
        val parts = time.split(":")
        if (parts.size < 2) return null
        val h = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        return h * 60 + m
    }
}
