package com.aakash.weather.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aakash.weather.databinding.TodayWeatherBinding
import com.aakash.weather.model.Hour
import com.bumptech.glide.Glide

class TodayAdapter(private val context: Context) :
    RecyclerView.Adapter<TodayAdapter.TodayViewHolder>() {
    private var dataList = mutableListOf<Hour>()

    inner class TodayViewHolder(private val binding: TodayWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hour: Hour) {
            val time = hour.time.split(" ")
            Log.d("Time", "time is ${time[1]}")
            binding.apply {
                this.txtHour.text = time[1]
                this.txtTemp.text = hour.tempC.toString()
                Glide.with(context).load("https:${hour.condition.icon}").into(binding.imgIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val binding = TodayWeatherBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return TodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        holder.bind(this.dataList[position])
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    fun setDataList(hourList: List<Hour>) {
        this.dataList.addAll(hourList)
        notifyDataSetChanged()
    }
    fun scrollToTime(recyclerView: RecyclerView, targetHour: Int) {
        val index = dataList.indexOfFirst { hour ->
            val time = hour.time.split(" ")[1] // e.g. "21:30"
            val hourPart = time.split(":")[0].toInt()
            hourPart == targetHour
        }

        if (index != -1) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                layoutManager.scrollToPositionWithOffset(index, 0)
            }
        }
    }

}