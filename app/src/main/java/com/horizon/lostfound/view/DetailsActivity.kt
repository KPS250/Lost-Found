package com.horizon.lostfound.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.horizon.lostfound.R
import kotlinx.android.synthetic.main.activity_details.*
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    var contactNo=""
    var lat : Double = 0.0
    var long : Double = 0.0
    var status=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        title = "Item Details"

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
            tv_cat_tags.text =  bundle.getString("catTags")
            tv_description.text =bundle.getString("desc")
            tv_date.text = dateFormat.format(c.time)
            tv_complaint_id.text = bundle.getString("complaintId")
            tv_train_name.text = bundle.getString("trainName")
            tv_contact_name.text = bundle.getString("contactName")
            tv_contact_number.text =bundle.getString("contactNo")

            contactNo = bundle.getString("contactNo")
            lat = bundle.getDouble("lat")
            long = bundle.getDouble("long")

            if(!bundle.getBoolean("start")){
                rl_status.visibility = View.VISIBLE
                bt_claim.visibility = View.VISIBLE
                bt_claim.text="UPDATE"
                rl_contact_number.visibility=View.VISIBLE

                val arrayStatus = arrayOf("Active", "InActive")
                //Adapter for spinner
                spinner_status.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayStatus)
                //item selected listener for spinner
                spinner_status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        status = arrayStatus[p2]=="Active"
                        //Toast.makeText(this@AddLostItem, myStrings[p2], LENGTH_SHORT).show()
                    }

                }
            }

        }

    }

    fun callNow(view: View){
        makeCall(contactNo)
    }

    fun claimItem(view:View){

        if(bt_claim.text.toString() == "Claim Item") {

            val context = this
            val builder = AlertDialog.Builder(context)
            //builder.setTitle("New Category")
            builder.setMessage("Mobile Number verification is mandatory to claim the item")

            // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
            // Seems ok to inflate view with null rootView
            val view = layoutInflater.inflate(R.layout.layout_phoneno, null)

            val categoryEditText = view.findViewById(R.id.categoryEditText) as EditText

            builder.setView(view)

            // set up the ok button
            builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
                val newCategory = categoryEditText.text
                var isValid = true
                if (newCategory.isBlank() || newCategory.toString() == "") {
                    categoryEditText.error = "Invalid"
                    isValid = false
                }

                if (isValid) {
                    // do something
                    //toast(categoryEditText.text.toString())
                    rl_contact_number.visibility = View.VISIBLE
                    bt_claim.visibility = View.GONE
                }

                if (!isValid) {
                    dialog.dismiss()
                }
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
                dialog.cancel()
            }

            builder.show()
        }else{
            toast("Item Updated")
            onBackPressed()
        }

    }

    fun openMap(view: View){
        val gmmIntentUri = Uri.parse("geo:$lat,$long")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}
