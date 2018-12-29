package com.horizon.lostfound.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Base64
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import com.horizon.lostfound.R
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddLostItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lost_item)
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
        //iv_image.setImageBitmap(bitmap)
    }

    fun getBase64String(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }

    fun moveToHome(view: View){
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
                //et_date.setText(parsedDate)
               // year = datePicker.year
                //month = datePicker.month+1
                //day = datePicker.dayOfMonth

            }
            noButton { }
        }.show()
    }
}
