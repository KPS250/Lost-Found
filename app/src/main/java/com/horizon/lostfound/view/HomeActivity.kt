package com.horizon.lostfound.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.horizon.lostfound.R
import com.horizon.lostfound.firebase.FirebaseHelper
import com.horizon.lostfound.firebase.SharedPreferencesHelper
import com.horizon.lostfound.model.LostItem
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import org.jetbrains.anko.toast


class HomeActivity : AppCompatActivity() {

    lateinit var firebaseHelper: FirebaseHelper
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    var start = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        firebaseHelper = FirebaseHelper(this)
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Initialize a new linear layout manager
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
            this, // Context
            LinearLayout.VERTICAL, // Orientation
            false // Reverse layout
        )

        // Specify the layout manager for recycler view
        recycler_view.layoutManager = linearLayoutManager
        // Finally, data bind the recycler view with adapter

        fab.setOnClickListener { view -> showFilterDialog()
           // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show()
        }

        val bundle=intent.extras
        if(bundle!=null && !bundle.getBoolean("start")) {
            start = false
            if(sharedPreferencesHelper.getMobileNumber()!="") {
               // actionBar.title="My Found Items"
                myFoundItems()
                title="My Found Items"
               fab.hide()


            }else{
                claimItem()
            }
        }
        else {
            splashDialog()
            start=true
            fab.show()
            firebaseHelper.getLostItems(object : OnDataReceiveCallback {
                override fun onDataReceived(eventList: ArrayList<LostItem>) {

                    if (eventList.size > 0) {
                        recycler_view.adapter = RecyclerViewAdapter(firebaseHelper.itemList)
                        recycler_view.visibility = View.VISIBLE
                        lv_empty.visibility = View.GONE
                    }
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(start)
            menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.add_found_item -> {

                var intent = Intent(this, AddLostItem::class.java)
                startActivity(intent)
                this.finish()
                true
            }
            R.id.my_found_items -> {

                var intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("start", false)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)


        }
    }

    fun showFilterDialog(){
        // Late initialize an alert dialog object
        var dialog: AlertDialog

        // Initialize an array of colors
        val arrayCateTags = arrayOf("Other", "Mobile", "Bag", "ID", "Wallet")

        // Initialize a boolean array of checked items
        var arrayChecked = booleanArrayOf(false,false,false,false,false)


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Filter Category")
        // Define multiple choice items for alert dialog
        builder.setMultiChoiceItems(arrayCateTags, arrayChecked) { dialog, which, isChecked->
            // Update the clicked item checked status
            arrayChecked[which] = isChecked

            // Get the clicked item
            val catTag = arrayCateTags[which]

            // Display the clicked item text
            toast("$catTag clicked.")
        }


        // Set the positive/yes button click listener
        builder.setPositiveButton("OK") { _, _ ->
            // Do something when click positive button
            //tv_cat_tags.text = "Your preferred category tags..... \n"
            for (i in 0 until arrayCateTags.size) {
                val checked = arrayChecked[i]
                if (checked) {
                    //tv_cat_tags.text = "${tv_cat_tags.text}  ${arrayCateTags[i]} \n"
                }
            }
        }


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    // Extension function to show toast message
    fun Context.toast(message: String) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun claimItem(){

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
            if (newCategory.isBlank() || newCategory.toString() == "")  {
                categoryEditText.error ="Invalid"
                isValid = false
            }

            if (isValid) {
                // do something
                //toast(categoryEditText.text.toString())
                sharedPreferencesHelper.setMobileNumber(categoryEditText.text.toString())
                myFoundItems()
            }

            if (!isValid) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()

    }

    fun myFoundItems(){
        firebaseHelper.getFoundItems(object : OnDataReceiveCallback1 {
            override fun onDataReceived1(eventList: ArrayList<LostItem>) {

                if (eventList.size > 0) {
                    recycler_view.adapter = RecyclerViewAdapter(firebaseHelper.itemList)
                    recycler_view.visibility = View.VISIBLE
                    lv_empty.visibility = View.GONE
                }
            }
        })
    }

    fun splashDialog(){
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.layout_splash, null)
            builder.setView(view)
            builder.show()
    }
}

interface OnDataReceiveCallback {
    fun onDataReceived(eventList: java.util.ArrayList<LostItem>)
}

interface OnDataReceiveCallback1 {
    fun onDataReceived1(eventList: java.util.ArrayList<LostItem>)
}





