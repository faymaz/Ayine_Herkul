package com.faymaz.herkul.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faymaz.herkul.R
import com.faymaz.herkul.model.PrayerTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrayerTimeAdapter(
    private var items: List<PrayerTime>,
    private var todayDate: String = ""
) : RecyclerView.Adapter<PrayerTimeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvHijri: TextView = view.findViewById(R.id.tvHijri)
        val tvImsak: TextView = view.findViewById(R.id.tvImsak)
        val tvGunes: TextView = view.findViewById(R.id.tvGunes)
        val tvOgle: TextView = view.findViewById(R.id.tvOgle)
        val tvIkindi: TextView = view.findViewById(R.id.tvIkindi)
        val tvAksam: TextView = view.findViewById(R.id.tvAksam)
        val tvYatsi: TextView = view.findViewById(R.id.tvYatsi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayer_time, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvDate.text = item.date
        holder.tvHijri.text = item.hijriDate
        holder.tvImsak.text = item.imsak
        holder.tvGunes.text = item.gunes
        holder.tvOgle.text = item.ogle
        holder.tvIkindi.text = item.ikindi
        holder.tvAksam.text = item.aksam
        holder.tvYatsi.text = item.yatsi

        // Highlight today's row
        val isToday = todayDate.isNotEmpty() && item.date == todayDate
        holder.itemView.setBackgroundResource(
            if (isToday) R.drawable.bg_today_row else R.drawable.bg_normal_row
        )
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<PrayerTime>, newTodayDate: String = todayDate) {
        items = newItems
        todayDate = newTodayDate
        notifyDataSetChanged()
    }
}
