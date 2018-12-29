package com.horizon.lostfound.view

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.horizon.lostfound.R
import com.horizon.lostfound.model.LostItem
import kotlinx.android.synthetic.main.item_view.view.*

import java.util.*
import java.text.SimpleDateFormat


class RecyclerViewAdapter(val event: ArrayList<LostItem>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom view from xml layout file
        val v: View = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_view,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageBytes = Base64.decode(event.get(position).image, 0)
        var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        var c = Calendar.getInstance()
        c.timeInMillis = event[position].dateTime

        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy hh:mm ")
        System.out.println(dateFormat.format(c.time))

        holder?.img_event?.setImageBitmap(image)
        holder?.tv_dateTime?.text = dateFormat.format(c.time)
        holder?.tv_name?.text = event.get(position).desc
        holder?.tv_organizer?.text = event.get(position).desc
    }

    override fun getItemCount(): Int {
        return event.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_dateTime = itemView.tv_datetime
        val img_event = itemView.img_event
        val tv_name = itemView.tv_name
        val tv_organizer = itemView.tv_organizer
    }

    // This two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

interface OnDataReceiveCallback {
    fun onDataReceived(eventList: ArrayList<LostItem>)
}

interface OnInitiateNotificationCallback {
    fun  onInitiateNotification(event: LostItem)
}