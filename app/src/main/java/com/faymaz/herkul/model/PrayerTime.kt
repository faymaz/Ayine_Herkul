package com.faymaz.herkul.model

data class PrayerTime(
    val date: String,
    val hijriDate: String,
    val imsak: String,
    val gunes: String,
    val ogle: String,
    val ikindi: String,
    val aksam: String,
    val yatsi: String
)

data class DailyPrayerTimes(
    val gregorianDate: String,
    val hijriDate: String,
    val times: Map<String, String>  // prayer name -> time string
)

enum class PrayerName(val displayName: String) {
    IMSAK("İmsak"),
    GUNES("Güneş"),
    OGLE("Öğle"),
    IKINDI("İkindi"),
    AKSAM("Akşam"),
    YATSI("Yatsı")
}
