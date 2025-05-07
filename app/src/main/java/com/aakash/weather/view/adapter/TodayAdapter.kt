package com.aakash.weather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aakash.weather.databinding.TodayWeatherBinding

class TodayAdapter : RecyclerView.Adapter<TodayAdapter.TodayViewHolder>() {
    inner class TodayViewHolder(binding: TodayWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(holder: TodayViewHolder, position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val binding = TodayWeatherBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return TodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        holder.bind(holder, position)
    }

    override fun getItemCount(): Int {
        return 24
    }
}