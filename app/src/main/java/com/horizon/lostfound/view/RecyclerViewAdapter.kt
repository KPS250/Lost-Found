package com.horizon.lostfound.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.horizon.lostfound.R
import com.horizon.lostfound.firebase.SharedPreferencesHelper
import com.horizon.lostfound.model.LostItem
import com.horizon.lostfound.utils.TimeHelper
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

        holder?.posted_person_name?.text = "Posted by: "+event[position].contactName
        holder?.iv_item?.setImageBitmap(image)
        holder?.tv_tags?.text =  "Category: "+event[position].catTags
        holder?.tv_date?.text = dateFormat.format(c.time)
        holder?.tv_time?.text = TimeHelper.timeElasped(event[position].dateTime)
        holder?.tv_desc?.text = event[position].desc
        if(event[position].status)
            holder?.tv_status_active.visibility=View.VISIBLE
        else
            holder?.tv_status_inactive.visibility=View.VISIBLE


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

            var appC = context as HomeActivity
            if(appC.title == context.getString(R.string.app_name)) {
                intent.putExtra("start", true)

            }else{
                intent.putExtra("start", false)
            }

            context.startActivity(intent)

            // Toast.makeText(context,children[position].data.author_fullname,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return event.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val posted_person_name = itemView.tv_posted_person_name
        val iv_item = itemView.iv_item
        val tv_desc = itemView.tv_desc
        val tv_date = itemView.tv_date
        val tv_time = itemView.tv_time
        val tv_tags = itemView.tv_tags
        val tv_status_active = itemView.tv_status_active
        val tv_status_inactive = itemView.tv_status_inactive
    }

    // This two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

