package com.horizon.lostfound.view

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.horizon.lostfound.R
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val bundle=intent.extras
        if(bundle!=null)
        {
            val imageBytes = Base64.decode(bundle.getString("image"), 0)
            var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            var c = Calendar.getInstance()
            c.timeInMillis = bundle.getLong("dateTime")

            val dateFormat = SimpleDateFormat("dd MMM yyyy")
            System.out.println(dateFormat.format(c.time))
            iv_image.setImageBitmap(image)
            tv_cat_tags.text = bundle.getString("catTags")
            tv_description.text = bundle.getString("desc")
            tv_date.text = dateFormat.format(c.time)
            tv_complaint_id.text = bundle.getString("complaintId")
            tv_train_name.text = bundle.getString("trainName")
            tv_contact_name.text = bundle.getString("contactName")
            tv_contact_number.text = bundle.getString("contactNo")

        }
    }
}
