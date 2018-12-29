package com.horizon.lostfound.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.horizon.lostfound.R
import com.horizon.lostfound.firebase.FirebaseHelper
import com.horizon.lostfound.firebase.SharedPreferencesHelper
import com.horizon.lostfound.model.LostItem
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*


class HomeActivity : AppCompatActivity() {

    lateinit var firebaseHelper: FirebaseHelper
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        firebaseHelper = FirebaseHelper(this)
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        var lostItem =
            LostItem("","123",125,1,2,"234","Sam","Kiran",789,true)

       // firebaseHelper.addLostItem(lostItem)


        // Initialize a new linear layout manager
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
            this, // Context
            LinearLayout.VERTICAL, // Orientation
            false // Reverse layout
        )

        // Specify the layout manager for recycler view
        recycler_view.layoutManager = linearLayoutManager
        // Finally, data bind the recycler view with adapter

        firebaseHelper.getLostItems(object : OnDataReceiveCallback {
            override fun onDataReceived(eventList: ArrayList<LostItem>) {

                if (eventList.size > 0) {
                    recycler_view.adapter = RecyclerViewAdapter(firebaseHelper.itemList)
                    recycler_view.visibility = View.VISIBLE
                    //lv_empty.visibility = View.GONE
                    //tv_noEvents.visibility = View.GONE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}




