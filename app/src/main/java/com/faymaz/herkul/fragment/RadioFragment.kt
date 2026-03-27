package com.faymaz.herkul.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.faymaz.herkul.R
import com.faymaz.herkul.data.RadioStation
import com.faymaz.herkul.data.RadioStations
import com.faymaz.herkul.service.RadioService

class RadioFragment : Fragment() {

    private var radioService: RadioService? = null
    private var isBound = false
    private var selectedStation: RadioStation? = null

    private val stationCards = mutableListOf<View>()
    private lateinit var tvStatus: TextView

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            radioService = (binder as RadioService.RadioBinder).getService()
            isBound = true
            radioService?.onPlaybackStateChanged = { playing ->
                activity?.runOnUiThread { updatePlaybackUI(playing) }
            }
            radioService?.onError = { msg ->
                activity?.runOnUiThread { Toast.makeText(context, "Hata: $msg", Toast.LENGTH_LONG).show() }
            }
            updatePlaybackUI(radioService?.isPlaying() == true)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            radioService = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_radio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStatus = view.findViewById(R.id.tvRadioStatus)

        val container = view.findViewById<LinearLayout>(R.id.stationsContainer)
        RadioStations.ALL.forEachIndexed { index, station ->
            val card = buildStationCard(station, index)
            stationCards.add(card)
            container.addView(card)
        }
    }

    private fun buildStationCard(station: RadioStation, index: Int): View {
        val card = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_radio_station, null)

        card.findViewById<TextView>(R.id.tvStationName).text = station.name
        card.findViewById<TextView>(R.id.tvStationDesc).text = station.description

        val btnPlayPause = card.findViewById<ImageButton>(R.id.btnPlayPause)
        btnPlayPause.setOnClickListener {
            if (radioService?.isPlaying() == true && radioService?.getCurrentStation() == station.name) {
                stopPlayback()
            } else {
                playStation(station)
            }
        }

        return card
    }

    private fun playStation(station: RadioStation) {
        selectedStation = station
        val serviceIntent = Intent(requireContext(), RadioService::class.java).apply {
            action = RadioService.ACTION_PLAY
            putExtra(RadioService.EXTRA_STREAM_URL, station.streamUrl)
            putExtra(RadioService.EXTRA_STATION_NAME, station.name)
        }
        requireContext().startService(serviceIntent)
        tvStatus.text = "${station.name} yükleniyor..."
    }

    private fun stopPlayback() {
        val serviceIntent = Intent(requireContext(), RadioService::class.java).apply {
            action = RadioService.ACTION_STOP
        }
        requireContext().startService(serviceIntent)
    }

    private fun updatePlaybackUI(playing: Boolean) {
        val currentStation = radioService?.getCurrentStation() ?: ""
        stationCards.forEachIndexed { index, card ->
            val station = RadioStations.ALL.getOrNull(index) ?: return@forEachIndexed
            val isThisPlaying = playing && currentStation == station.name
            val btn = card.findViewById<ImageButton>(R.id.btnPlayPause)
            btn.setImageResource(
                if (isThisPlaying) android.R.drawable.ic_media_pause
                else android.R.drawable.ic_media_play
            )
            card.setBackgroundResource(
                if (isThisPlaying) R.drawable.bg_station_playing
                else R.drawable.bg_station_normal
            )
        }
        tvStatus.text = if (playing) "Çalıyor: $currentStation" else "Durdu"
    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), RadioService::class.java).also { intent ->
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            requireContext().unbindService(serviceConnection)
            isBound = false
        }
    }
}
