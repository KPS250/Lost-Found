package com.horizon.lostfound.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.horizon.lostfound.model.LostItem
import com.horizon.lostfound.view.OnDataReceiveCallback
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class FirebaseHelper(context: Context) {

    val db = FirebaseFirestore.getInstance()
    val sharedPreferencesHelper = SharedPreferencesHelper(context)
    val TAG = "FirebaseHelper"
    var itemList: ArrayList<LostItem> = ArrayList()

    lateinit var ids:String
    lateinit var context: Context

    fun addLostItem(lostItem: LostItem){
        // Add a new document with a generated ID
        db.collection("lostItem")
            .add(lostItem)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    fun getLostItems(callback: OnDataReceiveCallback){
        itemList.clear()
        db.collection("lostItem")
            .whereEqualTo("status", true)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // eventList = ArrayList()
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        itemList.add(
                            LostItem(
                            document.data["image"].toString(),
                            document.data["catTags"].toString(),
                            document.data["desc"].toString(),
                            document.data["dateTime"] as Long,
                            document.data["lat"] as Double,
                            document.data["long"] as Double,
                            document.data["complaintId"].toString(),
                            document.data["trainName"].toString(),
                            document.data["contactName"].toString(),
                            document.data["contactNo"].toString(),
                            document.data["status"] as Boolean
                            )
                        )
                    }

                    Log.d("Data is set","Outer Query")
                    /*
                    // Nesting Query for Events with attendees as the user
                    db.collection("event")
                        .whereEqualTo("attendee", sharedPreferencesHelper.getEmail())
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // eventList = ArrayList()
                                for (document in task.result!!) {
                                    Log.d(TAG, document.id + " => " + document.data)

                                    var event = Event(document.data.get("organizer").toString(),
                                        document.data.get("attendee").toString(),
                                        document.data.get("image").toString(),
                                        document.data.get("time") as Long,
                                        document.data.get("text").toString())

                                    eventList.add(event)

                                    sharedPreferencesHelper.setIds("")
                                    if(!ids.contains(document.id)){
                                        // ids.plus(document.id+",")
                                        nCallBack.onInitiateNotification(event)
                                    }
                                }
                                // Nesting Query for Events with attendees as the user
                                Log.d("Data is set","Inner Query")
                                callback.onDataReceived(eventList)
                            } else {
                                Log.w(TAG, "Error getting documents.", task.exception)
                            }
                        }// End of Inner Query
                        */
                    callback.onDataReceived(itemList)
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }// End of Outer Query
    }

    fun isInternetWorking(): Boolean {
        var success = false
        try {
            val url = URL("https://google.com")
            val connection = url.openConnection() as HttpURLConnection
            connection.setConnectTimeout(10000)
            connection.connect()
            success = connection.getResponseCode() === 200
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return success
    }

}