package com.horizon.lostfound.view

import android.content.Context
import android.content.Intent
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

    lateinit  var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom view from xml layout file
        val v: View = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_view,parent,false)

        this.context = parent.context

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageBytes = Base64.decode(event[position].image, 0)
        var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        var c = Calendar.getInstance()
        c.timeInMillis = event[position].dateTime

        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        System.out.println(dateFormat.format(c.time))

        holder?.iv_item?.setImageBitmap(image)
        holder?.tv_dateTime?.text = dateFormat.format(c.time)
        holder?.tv_desc?.text = event[position].desc

        holder.itemView.setOnClickListener {

            var intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("image", event[position].image )
            intent.putExtra("catTags", event[position].catTags )
            intent.putExtra("desc", event[position].desc)
            intent.putExtra("dateTime", event[position].dateTime)
            intent.putExtra("lat", event[position].lat)
            intent.putExtra("long", event[position].long)
            intent.putExtra("complaintId", event[position].complaintId)
            intent.putExtra("trainName", event[position].trainName)
            intent.putExtra("contactName", event[position].contactName)
            intent.putExtra("contactNo", event[position].contactNo)
            intent.putExtra("status", event[position].status)
            context.startActivity(intent)

            // Toast.makeText(context,children[position].data.author_fullname,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return event.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iv_item = itemView.iv_item
        val tv_desc = itemView.tv_desc
        val tv_dateTime = itemView.tv_datetime
    }

    // This two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

