package com.faymaz.herkul.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.faymaz.herkul.R
import com.faymaz.herkul.data.Countries
import com.faymaz.herkul.data.DisplayItem
import com.faymaz.herkul.data.DisplayItems
import com.faymaz.herkul.model.PrayerTime
import com.faymaz.herkul.util.PrayerCountdown
import com.faymaz.herkul.util.PrayerTimeCache
import com.faymaz.herkul.util.PrayerTimeFetcher
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val handler = Handler(Looper.getMainLooper())

    // Views
    private lateinit var homeRoot: FrameLayout
    private lateinit var ivDecoImage: ImageView
    private lateinit var tvArabic: TextView
    private lateinit var tvQuote: TextView
    private lateinit var tvEnglish: TextView
    private lateinit var tvQuoteAuthor: TextView
    private lateinit var tvClock: TextView
    private lateinit var tvMiladiDate: TextView
    private lateinit var tvHijriDate: TextView
    private lateinit var tvCityCountry: TextView
    private lateinit var homeProgressBar: ProgressBar
    private lateinit var homeErrorText: TextView

    // Prayer bar views
    private lateinit var prayerBarImsak:  LinearLayout
    private lateinit var prayerBarGunes:  LinearLayout
    private lateinit var prayerBarOgle:   LinearLayout
    private lateinit var prayerBarIkindi: LinearLayout
    private lateinit var prayerBarAksam:  LinearLayout
    private lateinit var prayerBarYatsi:  LinearLayout

    private lateinit var barLblImsak:  TextView; private lateinit var barTvImsak:  TextView
    private lateinit var barLblGunes:  TextView; private lateinit var barTvGunes:  TextView
    private lateinit var barLblOgle:   TextView; private lateinit var barTvOgle:   TextView
    private lateinit var barLblIkindi: TextView; private lateinit var barTvIkindi: TextView
    private lateinit var barLblAksam:  TextView; private lateinit var barTvAksam:  TextView
    private lateinit var barLblYatsi:  TextView; private lateinit var barTvYatsi:  TextView

    // State
    private var todayPrayer: PrayerTime? = null
    private var loadedCityId: Int = -1
    private var slideIndex = 0
    private var quoteIndex = 0

    private val backgrounds = intArrayOf(
        R.drawable.bg_home_wine,
        R.drawable.bg_home_navy,
        R.drawable.bg_home_teal,
        R.drawable.bg_home_plum
    )
    private val decoImages = intArrayOf(
        R.drawable.home_1,
        R.drawable.home_2,
        R.drawable.home_3,
        R.drawable.home_4
    )

    // Tick every second for clock
    private val clockRunnable = object : Runnable {
        override fun run() {
            updateClock()
            handler.postDelayed(this, 1_000L)
        }
    }

    // Slide change every 20 seconds
    private val slideRunnable = object : Runnable {
        override fun run() {
            nextSlide()
            handler.postDelayed(this, 20_000L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeRoot        = view.findViewById(R.id.homeRoot)
        ivDecoImage     = view.findViewById(R.id.ivDecoImage)
        tvArabic        = view.findViewById(R.id.tvArabic)
        tvQuote         = view.findViewById(R.id.tvQuote)
        tvEnglish       = view.findViewById(R.id.tvEnglish)
        tvQuoteAuthor   = view.findViewById(R.id.tvQuoteAuthor)
        tvClock         = view.findViewById(R.id.tvClock)
        tvMiladiDate    = view.findViewById(R.id.tvMiladiDate)
        tvHijriDate     = view.findViewById(R.id.tvHijriDate)
        tvCityCountry   = view.findViewById(R.id.tvCityCountry)
        homeProgressBar = view.findViewById(R.id.homeProgressBar)
        homeErrorText   = view.findViewById(R.id.homeErrorText)

        prayerBarImsak  = view.findViewById(R.id.prayerBarImsak)
        prayerBarGunes  = view.findViewById(R.id.prayerBarGunes)
        prayerBarOgle   = view.findViewById(R.id.prayerBarOgle)
        prayerBarIkindi = view.findViewById(R.id.prayerBarIkindi)
        prayerBarAksam  = view.findViewById(R.id.prayerBarAksam)
        prayerBarYatsi  = view.findViewById(R.id.prayerBarYatsi)

        barLblImsak  = view.findViewById(R.id.barLblImsak);  barTvImsak  = view.findViewById(R.id.barTvImsak)
        barLblGunes  = view.findViewById(R.id.barLblGunes);  barTvGunes  = view.findViewById(R.id.barTvGunes)
        barLblOgle   = view.findViewById(R.id.barLblOgle);   barTvOgle   = view.findViewById(R.id.barTvOgle)
        barLblIkindi = view.findViewById(R.id.barLblIkindi); barTvIkindi = view.findViewById(R.id.barTvIkindi)
        barLblAksam  = view.findViewById(R.id.barLblAksam);  barTvAksam  = view.findViewById(R.id.barTvAksam)
        barLblYatsi  = view.findViewById(R.id.barLblYatsi);  barTvYatsi  = view.findViewById(R.id.barTvYatsi)

        // Shuffle start index
        quoteIndex = (0 until DisplayItems.ALL.size).random()
        slideIndex = quoteIndex % backgrounds.size

        updateDateDisplay()
        updateCityCountry()
        showCurrentSlide(animate = false)
        fetchTodayPrayerTimes()
    }

    // ── Slide cycling ─────────────────────────────────────────────────────────

    private fun nextSlide() {
        slideIndex = (slideIndex + 1) % backgrounds.size
        quoteIndex = (quoteIndex + 1) % DisplayItems.ALL.size
        showCurrentSlide(animate = true)
    }

    private fun showCurrentSlide(animate: Boolean) {
        if (animate) {
            val fadeOut = ObjectAnimator.ofFloat(homeRoot, "alpha", 1f, 0f).apply { duration = 600 }
            fadeOut.start()
            handler.postDelayed({
                homeRoot.setBackgroundResource(backgrounds[slideIndex])
                ivDecoImage.setImageResource(decoImages[slideIndex])
                applyDisplayItem(DisplayItems.ALL[quoteIndex])
                ObjectAnimator.ofFloat(homeRoot, "alpha", 0f, 1f).apply { duration = 800 }.start()
            }, 620)
        } else {
            homeRoot.setBackgroundResource(backgrounds[slideIndex])
            ivDecoImage.setImageResource(decoImages[slideIndex])
            applyDisplayItem(DisplayItems.ALL[quoteIndex])
        }
    }

    private fun applyDisplayItem(item: DisplayItem) {
        when (item) {
            is DisplayItem.QuoteItem -> {
                tvArabic.visibility  = View.GONE
                tvEnglish.visibility = View.GONE
                tvQuote.text         = item.quote.text
                tvQuoteAuthor.text   = "— ${item.quote.author}"
                tvQuote.textSize     = 28f
            }
            is DisplayItem.HadithItem -> {
                val h = item.hadith
                tvArabic.text        = h.arabic
                tvArabic.visibility  = View.VISIBLE
                tvQuote.text         = h.turkish
                tvQuote.textSize     = 20f
                tvEnglish.text       = h.english
                tvEnglish.visibility = View.VISIBLE
                val attr = buildString {
                    if (h.narrator.isNotBlank()) append("— ${h.narrator}")
                    if (h.source.isNotBlank()) {
                        if (isNotEmpty()) append("  •  ")
                        append(h.source)
                    }
                }
                tvQuoteAuthor.text = attr
            }
        }
    }

    // ── Prayer data ───────────────────────────────────────────────────────────

    private fun fetchTodayPrayerTimes() {
        val prefs = requireContext().getSharedPreferences("herkul_prefs", Context.MODE_PRIVATE)
        val countryName = prefs.getString("country_name", Countries.DEFAULT_COUNTRY.name)
        val cityName    = prefs.getString("city_name", null)

        val country = Countries.ALL.firstOrNull { it.name == countryName } ?: Countries.DEFAULT_COUNTRY
        val city    = if (cityName != null) country.cities.firstOrNull { it.name == cityName }
                      else country.cities.firstOrNull()
        val cityObj = city ?: country.cities.first()

        homeProgressBar.visibility = View.VISIBLE
        homeErrorText.visibility   = View.GONE

        val ctx = requireContext()
        coroutineScope.launch {
            try {
                val times = withContext(Dispatchers.IO) {
                    PrayerTimeCache.load(ctx, cityObj.id)
                        ?: run {
                            val fetched = PrayerTimeFetcher.fetchAllPrayerTimes(cityObj.url)
                            if (fetched.isNotEmpty()) PrayerTimeCache.save(ctx, cityObj.id, fetched)
                            fetched
                        }
                }
                homeProgressBar.visibility = View.GONE
                if (times.isNotEmpty()) {
                    loadedCityId = cityObj.id
                    todayPrayer = findToday(times)
                    todayPrayer?.let { bindPrayerTimes(it) }
                } else {
                    showError("Bağlantı hatası")
                }
            } catch (e: Exception) {
                homeProgressBar.visibility = View.GONE
                showError("Hata")
            }
        }
    }

    private fun findToday(list: List<PrayerTime>): PrayerTime? {
        val cal       = Calendar.getInstance()
        val day       = cal.get(Calendar.DAY_OF_MONTH).toString()
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale("tr")).format(cal.time)
        return list.firstOrNull { it.date.startsWith("$day ") && it.date.contains(monthYear) }
            ?: list.firstOrNull()
    }

    // ── UI bind ───────────────────────────────────────────────────────────────

    private fun bindPrayerTimes(pt: PrayerTime) {
        barTvImsak.text  = pt.imsak
        barTvGunes.text  = pt.gunes
        barTvOgle.text   = pt.ogle
        barTvIkindi.text = pt.ikindi
        barTvAksam.text  = pt.aksam
        barTvYatsi.text  = pt.yatsi
        tvHijriDate.text = pt.hijriDate
        highlightActive(pt)
    }

    private fun highlightActive(pt: PrayerTime) {
        val nextName = PrayerCountdown.getNextPrayer(pt)?.name

        data class BarItem(val name: String, val lbl: TextView, val tv: TextView)
        val items = listOf(
            BarItem("İmsak",  barLblImsak,  barTvImsak),
            BarItem("Güneş",  barLblGunes,  barTvGunes),
            BarItem("Öğle",   barLblOgle,   barTvOgle),
            BarItem("İkindi", barLblIkindi, barTvIkindi),
            BarItem("Akşam",  barLblAksam,  barTvAksam),
            BarItem("Yatsı",  barLblYatsi,  barTvYatsi)
        )
        for (item in items) {
            val active = item.name == nextName
            item.lbl.setTextColor(if (active) 0xFFFFD700.toInt() else 0xFFAABBD0.toInt())
            item.tv.setTextColor(if (active)  0xFFFFD700.toInt() else 0xFFE6EDF3.toInt())
        }
    }

    // ── Clock / date ──────────────────────────────────────────────────────────

    private fun updateClock() {
        tvClock.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        todayPrayer?.let { highlightActive(it) }
    }

    private fun updateDateDisplay() {
        tvMiladiDate.text = SimpleDateFormat("d MMM yyyy, EEE", Locale("tr")).format(Date())
    }

    private fun updateCityCountry() {
        val prefs       = requireContext().getSharedPreferences("herkul_prefs", Context.MODE_PRIVATE)
        val countryName = prefs.getString("country_name", Countries.DEFAULT_COUNTRY.name) ?: ""
        val cityName    = prefs.getString("city_name", "") ?: ""
        tvCityCountry.text = if (cityName.isNotBlank()) "$cityName\n$countryName" else countryName
    }

    private fun showError(msg: String) {
        homeErrorText.text       = msg
        homeErrorText.visibility = View.VISIBLE
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        updateCityCountry()
        updateDateDisplay()
        handler.post(clockRunnable)
        handler.postDelayed(slideRunnable, 20_000L)
        val prefs = requireContext().getSharedPreferences("herkul_prefs", Context.MODE_PRIVATE)
        val country = Countries.ALL.firstOrNull { it.name == prefs.getString("country_name", Countries.DEFAULT_COUNTRY.name) } ?: Countries.DEFAULT_COUNTRY
        val cityName = prefs.getString("city_name", null)
        val currentCityId = (if (cityName != null) country.cities.firstOrNull { it.name == cityName } else country.cities.firstOrNull())?.id ?: -1
        if (todayPrayer == null || currentCityId != loadedCityId) fetchTodayPrayerTimes()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(clockRunnable)
        handler.removeCallbacks(slideRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        handler.removeCallbacks(clockRunnable)
        handler.removeCallbacks(slideRunnable)
    }
}
