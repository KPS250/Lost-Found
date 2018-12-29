package com.horizon.lostfound.firebase

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context){

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val key_mobileNumber = "mobileNumber"
    }

    fun setMobileNumber(mobileNumber: String){
        preferences.edit().putString(key_mobileNumber,mobileNumber).apply()
    }

    fun getMobileNumber():String{
        return preferences.getString(key_mobileNumber, "")
    }

    fun clearData(){
        preferences.edit().putString(key_mobileNumber,"").apply()
    }
}