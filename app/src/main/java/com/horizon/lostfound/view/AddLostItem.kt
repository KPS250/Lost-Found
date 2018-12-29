package com.horizon.lostfound.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Base64
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import com.horizon.lostfound.R
import com.horizon.lostfound.firebase.FirebaseHelper
import com.horizon.lostfound.model.LostItem
import kotlinx.android.synthetic.main.activity_add_lost_item.*
import kotlinx.android.synthetic.main.item_view.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddLostItem : AppCompatActivity() {

    val myStrings = arrayOf("Other", "Mobile", "Bag", "ID", "Wallet")
    var cat=""
    var year : Int = 0
    var month : Int = 0
    var day : Int = 0
    var hour : Int = 0
    var min : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lost_item)

        //Adapter for spinner
        catTags.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        //item selected listener for spinner
        catTags.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cat=myStrings[p2]
                //Toast.makeText(this@AddLostItem, myStrings[p2], LENGTH_SHORT).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        useImage(data?.data!!)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun timeToMillis(year:Int, month:Int, day:Int, hour:Int, min:Int):Long{
        val c = Calendar.getInstance()
        c.set(year,month-1,day,hour,min,0)

        return c.timeInMillis
    }

    fun useImage(uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        //use the bitmap as you like
        iv_image.setImageBitmap(bitmap)
    }

    fun getBase64String(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }

    fun moveToHome(){
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun timePicker(view:View){

        alert {
            isCancelable = false
            lateinit var timePicker: TimePicker
            customView {
                verticalLayout {
                    timePicker = timePicker{}
                }
            }
            yesButton {
                val parsedTime = "${timePicker.hour}:${timePicker.minute}"
                //et_time.setText(parsedTime)
                //hour = timePicker.hour
               // min = timePicker.minute
            }
            noButton { }
        }.show()
    }

    fun imagePicker(view:View){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123)
    }

    fun datePicker(view:View){

        alert {
            isCancelable = false
            lateinit var datePicker: DatePicker
            customView {
                verticalLayout {
                    datePicker = datePicker {
                        minDate = System.currentTimeMillis()
                    }
                }
            }
            yesButton {
                val parsedDate = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
                et_date.setText(parsedDate)
                year = datePicker.year
                month = datePicker.month+1
                day = datePicker.dayOfMonth

            }
            noButton { }
        }.show()
    }

    fun onSubmit(view: View){
        val drawable = iv_image.getDrawable() as BitmapDrawable
        val bitmap = drawable.bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 120, 90, false)

        val c = Calendar.getInstance()
        c.set(year,month-1,day,hour,min,0)

        var timeInMillis = c.timeInMillis
        var lostItem =
            LostItem(
                getBase64String(scaledBitmap),
                cat,
                et_description.text.toString(),
                timeInMillis,
                19.14463,
                72.948792,
                et_complaintId.text.toString(),
                et_trainName.text.toString(),
                et_contact_name.text.toString(),
                et_contact_number.text.toString(),
                true
                )

        var firebaseHelper = FirebaseHelper(this)
        firebaseHelper.addLostItem(lostItem)
        moveToHome()

    }
}
