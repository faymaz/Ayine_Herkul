package com.faymaz.herkul

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.faymaz.herkul.databinding.ActivityMainBinding
import com.faymaz.herkul.fragment.AboutFragment
import com.faymaz.herkul.fragment.HomeFragment
import com.faymaz.herkul.fragment.PrayerTimesFragment
import com.faymaz.herkul.fragment.RadioFragment
import com.faymaz.herkul.fragment.TvFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment        = HomeFragment()
    private val prayerTimesFragment = PrayerTimesFragment()
    private val radioFragment       = RadioFragment()
    private val tvFragment          = TvFragment()
    private val aboutFragment       = AboutFragment()

    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, homeFragment)
                .add(R.id.fragmentContainer, prayerTimesFragment).hide(prayerTimesFragment)
                .add(R.id.fragmentContainer, radioFragment).hide(radioFragment)
                .add(R.id.fragmentContainer, tvFragment).hide(tvFragment)
                .add(R.id.fragmentContainer, aboutFragment).hide(aboutFragment)
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val target = when (item.itemId) {
                R.id.nav_home         -> homeFragment
                R.id.nav_prayer_times -> prayerTimesFragment
                R.id.nav_radio        -> radioFragment
                R.id.nav_tv           -> tvFragment
                R.id.nav_about        -> aboutFragment
                else -> return@setOnItemSelectedListener false
            }
            if (target !== activeFragment) {
                supportFragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(target)
                    .commit()
                activeFragment = target
            }
            true
        }

        binding.btnClose.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }
}
