package com.faymaz.herkul.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faymaz.herkul.R
import com.faymaz.herkul.adapter.PrayerTimeAdapter
import com.faymaz.herkul.data.Countries
import com.faymaz.herkul.model.City
import com.faymaz.herkul.model.Country
import com.faymaz.herkul.model.PrayerTime
import com.faymaz.herkul.util.PrayerCountdown
import com.faymaz.herkul.util.PrayerTimeCache
import com.faymaz.herkul.util.PrayerTimeFetcher
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class PrayerTimesFragment : Fragment() {

    // viewMode: 0=Günlük  1=Haftalık  2=Aylık  3=Yıllık
    private var viewMode = 0

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var spinnerCountry: Spinner
    private lateinit var spinnerCity: Spinner
    private lateinit var tvTodayDate: TextView
    private lateinit var tvHijriDate: TextView
    private lateinit var tvNextPrayer: TextView
    private lateinit var tvCountdown: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var btnDaily: Button
    private lateinit var btnWeekly: Button
    private lateinit var btnMonthly: Button
    private lateinit var btnYearly: Button

    private var adapter: PrayerTimeAdapter? = null
    private var allPrayerTimes: List<PrayerTime> = emptyList()

    private var selectedCountry: Country = Countries.DEFAULT_COUNTRY
    private var selectedCity: City = Countries.DEFAULT_COUNTRY.cities.first()

    // Prevent spinner cascade loop during programmatic selection
    private var suppressCityListener = false

    private val countdownRunnable = object : Runnable {
        override fun run() {
            updateCountdown()
            handler.postDelayed(this, 60_000L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_prayer_times, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerCountry = view.findViewById(R.id.spinnerCountry)
        spinnerCity    = view.findViewById(R.id.spinnerCity)
        tvTodayDate    = view.findViewById(R.id.tvTodayDate)
        tvHijriDate    = view.findViewById(R.id.tvHijriDate)
        tvNextPrayer   = view.findViewById(R.id.tvNextPrayer)
        tvCountdown    = view.findViewById(R.id.tvCountdown)
        recyclerView   = view.findViewById(R.id.recyclerView)
        progressBar    = view.findViewById(R.id.progressBar)
        tvError        = view.findViewById(R.id.tvError)
        btnDaily       = view.findViewById(R.id.btnDaily)
        btnWeekly      = view.findViewById(R.id.btnWeekly)
        btnMonthly     = view.findViewById(R.id.btnMonthly)
        btnYearly      = view.findViewById(R.id.btnYearly)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PrayerTimeAdapter(emptyList())
        recyclerView.adapter = adapter

        setupCountrySpinner()
        setupTabs()
        updateDateDisplay()
        loadSavedSelection()
        fetchPrayerTimes()
    }

    // ── Country spinner ───────────────────────────────────────────────────────

    private fun setupCountrySpinner() {
        val countryNames = Countries.ALL.map { it.name }
        val sa = ArrayAdapter(requireContext(), R.layout.spinner_item, countryNames)
        sa.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCountry.adapter = sa

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                val country = Countries.ALL[pos]
                if (country.name != selectedCountry.name) {
                    selectedCountry = country
                    // Populate city spinner for the new country
                    populateCitySpinner(country, selectFirst = true)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun populateCitySpinner(country: Country, selectFirst: Boolean, cityName: String? = null) {
        suppressCityListener = true

        val cityNames = country.cities.map { it.name }
        val ca = ArrayAdapter(requireContext(), R.layout.spinner_item, cityNames)
        ca.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCity.adapter = ca

        val targetIdx = when {
            cityName != null -> country.cities.indexOfFirst { it.name == cityName }.takeIf { it >= 0 } ?: 0
            selectFirst      -> 0
            else             -> 0
        }
        spinnerCity.setSelection(targetIdx)
        selectedCity = country.cities[targetIdx]

        suppressCityListener = false

        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                if (suppressCityListener) return
                val city = selectedCountry.cities[pos]
                if (city.id != selectedCity.id) {
                    selectedCity = city
                    saveSelection()
                    fetchPrayerTimes()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // ── Tabs ──────────────────────────────────────────────────────────────────

    private fun setupTabs() {
        fun selectTab(mode: Int) {
            viewMode = mode
            btnDaily.isSelected   = (mode == 0)
            btnWeekly.isSelected  = (mode == 1)
            btnMonthly.isSelected = (mode == 2)
            btnYearly.isSelected  = (mode == 3)
            updatePrayerList()
        }
        btnDaily.setOnClickListener   { selectTab(0) }
        btnWeekly.setOnClickListener  { selectTab(1) }
        btnMonthly.setOnClickListener { selectTab(2) }
        btnYearly.setOnClickListener  { selectTab(3) }
        btnDaily.isSelected = true
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private fun fetchPrayerTimes() {
        progressBar.visibility  = View.VISIBLE
        tvError.visibility      = View.GONE
        recyclerView.visibility = View.GONE

        val cityId = selectedCity.id
        val ctx    = requireContext()

        coroutineScope.launch {
            try {
                val times = withContext(Dispatchers.IO) {
                    // 1. Try cache first
                    PrayerTimeCache.load(ctx, cityId)
                        ?: run {
                            // 2. Cache miss or expired → fetch from Diyanet
                            val fetched = PrayerTimeFetcher.fetchAllPrayerTimes(selectedCity.url)
                            if (fetched.isNotEmpty()) {
                                PrayerTimeCache.save(ctx, cityId, fetched)
                            }
                            fetched
                        }
                }
                allPrayerTimes = times
                if (times.isEmpty()) {
                    showError("Namaz vakitleri alınamadı.\nİnternet bağlantınızı kontrol edin.")
                } else {
                    updatePrayerList()
                    updateCountdown()
                }
            } catch (e: Exception) {
                showError("Hata: ${e.message}")
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    // ── View update ───────────────────────────────────────────────────────────

    private fun updatePrayerList() {
        if (allPrayerTimes.isEmpty()) return

        val today = todayEntry() ?: allPrayerTimes.first()
        tvHijriDate.text = today.hijriDate

        val displayed: List<PrayerTime> = when (viewMode) {
            0    -> listOf(today)
            1    -> weekEntries()
            2    -> monthEntries()
            else -> allPrayerTimes
        }

        adapter?.updateData(displayed, today.date)
        recyclerView.visibility = View.VISIBLE

        if (viewMode >= 1) {
            val idx = displayed.indexOfFirst { it.date == today.date }
            if (idx >= 0) recyclerView.scrollToPosition(idx)
        }
    }

    private fun todayEntry(): PrayerTime? {
        val cal = Calendar.getInstance()
        val day       = cal.get(Calendar.DAY_OF_MONTH).toString()
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale("tr")).format(cal.time)
        return allPrayerTimes.firstOrNull { pt ->
            pt.date.startsWith("$day ") && pt.date.contains(monthYear)
        }
    }

    private fun weekEntries(): List<PrayerTime> {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale("tr"))
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        // Manually rewind to Monday of the current week.
        // Calendar.DAY_OF_WEEK: SUN=1 MON=2 TUE=3 WED=4 THU=5 FRI=6 SAT=7
        val dow = cal.get(Calendar.DAY_OF_WEEK)
        val daysSinceMonday = if (dow == Calendar.SUNDAY) 6 else dow - Calendar.MONDAY
        cal.add(Calendar.DAY_OF_MONTH, -daysSinceMonday)

        val weekDates = (0..6).map { val d = cal.time; cal.add(Calendar.DAY_OF_MONTH, 1); sdf.format(d) }.toSet()
        return allPrayerTimes.filter { pt ->
            // Prayer date strings: "26 Mart 2026 Perşembe" — strip trailing day name
            val dateOnly = pt.date.split(" ").take(3).joinToString(" ")
            weekDates.contains(dateOnly)
        }
    }

    private fun monthEntries(): List<PrayerTime> {
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale("tr")).format(Date())
        return allPrayerTimes.filter { it.date.contains(monthYear) }
    }

    private fun updateDateDisplay() {
        tvTodayDate.text = SimpleDateFormat("dd MMMM yyyy EEEE", Locale("tr")).format(Date())
    }

    private fun updateCountdown() {
        val today = todayEntry() ?: return
        val next = PrayerCountdown.getNextPrayer(today) ?: return
        tvNextPrayer.text = "Sonraki: ${next.name} - ${next.time}"
        tvCountdown.text  = next.countdownText
    }

    private fun showError(message: String) {
        tvError.text            = message
        tvError.visibility      = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    private fun loadSavedSelection() {
        val p = prefs()
        val countryName = p.getString("country_name", Countries.DEFAULT_COUNTRY.name)
        val cityName    = p.getString("city_name", null)

        val country = Countries.ALL.firstOrNull { it.name == countryName } ?: Countries.DEFAULT_COUNTRY
        selectedCountry = country

        val countryIdx = Countries.ALL.indexOf(country)
        spinnerCountry.setSelection(countryIdx)

        // Populate city spinner for the saved country, selecting saved city
        populateCitySpinner(country, selectFirst = cityName == null, cityName = cityName)
    }

    private fun saveSelection() {
        prefs().edit()
            .putString("country_name", selectedCountry.name)
            .putString("city_name", selectedCity.name)
            .apply()
    }

    private fun prefs() =
        requireContext().getSharedPreferences("herkul_prefs", Context.MODE_PRIVATE)

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        updateDateDisplay()
        handler.post(countdownRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(countdownRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        handler.removeCallbacks(countdownRunnable)
    }
}
