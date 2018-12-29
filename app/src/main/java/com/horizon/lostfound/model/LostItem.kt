package com.horizon.lostfound.model

class LostItem (
    val image: String,
    //val catTags: ArrayList<String>,
    val desc: String,
    val dateTime: Long,
    val lat:Long,
    val long:Long,
    val complaintId:String,
    val trainName:String,
    val contactName:String,
    val contactNo:Long,
    val status:Boolean
)