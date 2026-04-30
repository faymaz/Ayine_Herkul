package com.faymaz.herkul.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.faymaz.herkul.BuildConfig
import com.faymaz.herkul.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvVersion).text = "Sürüm ${BuildConfig.VERSION_NAME}"

        view.findViewById<TextView>(R.id.tvEmail).setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:faymaz@aymaz.org")))
        }

        view.findViewById<TextView>(R.id.tvWebsite).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/faymaz/Ayine_Herkul/tree/mceu")))
        }

        view.findViewById<TextView>(R.id.tvAyine).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ayine.tv")))
        }
    }
}
