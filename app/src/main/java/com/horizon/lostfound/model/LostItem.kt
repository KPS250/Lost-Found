package com.horizon.lostfound.model

class LostItem (
    val image: String,
    val catTags: String,
    val desc: String,
    val dateTime: Long,
    val lat:Double,
    val long:Double,
    val complaintId:String,
    val trainName:String,
    val contactName:String,
    val contactNo:String,
    val status:Boolean
)