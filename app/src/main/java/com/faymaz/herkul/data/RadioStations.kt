package com.faymaz.herkul.data

data class RadioStation(
    val name: String,
    val description: String,
    val streamUrl: String,
    val fallbackUrl: String? = null
)

object RadioStations {
    val ALL = listOf(
        RadioStation(
            name = "Herkul Radyo",
            description = "İslami içerik ve müzik",
            streamUrl = "https://listen.radioking.com/radio/721190/stream/787034",
            fallbackUrl = "https://play.radioking.io/herkulradyo"
        ),
        RadioStation(
            name = "Cihan Radyo",
            description = "Müzik ve kültür",
            streamUrl = "https://listen.radioking.com/radio/301204/stream/347869"
        ),
        RadioStation(
            name = "Sadece Müzik",
            description = "Türk ve dünya müziği",
            streamUrl = "https://listen.radioking.com/radio/605425/stream/666847"
        )
    )
}
