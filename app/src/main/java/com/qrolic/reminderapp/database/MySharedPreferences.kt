package com.qrolic.reminderapp.database

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences (context: Context) {

    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences(
                PREFERENCE_NAME,
                PRIVATE_MODE)
        editor = preferences.edit()
    }

    fun setNotiData(alarmId:String,millies:String,reminderTitle:String,reportAs:String){
        editor.putString("alarmId",alarmId)
        editor.putString("millies",millies)
        editor.putString("reminderTitle",reminderTitle)
        editor.putString("reportAs",reportAs)
        editor.commit()
    }


    fun setFontSize(fountSize:String){
        editor.putString("fount_size",fountSize)
        editor.commit()
    }

    fun getFontSize():String?{
        return preferences.getString("fount_size","14sp")
    }

    fun getAlarmId():String?{
        return preferences.getString("alarmId","")
    }

    fun getMillies():String?{
        return preferences.getString("millies","")
    }

    fun getReminderTitle():String?{
        return preferences.getString("reminderTitle","")
    }
    fun getReportAs():String?{
        return preferences.getString("reportAs","")
    }

    fun getAlarmSound():String?{
        return preferences.getString("alarm_sound","")
    }

    fun setAlarmSound(alarmSound:String){
        editor.putString("alarm_sound",alarmSound)
        editor.commit()
    }


    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREFERENCE_NAME = "reminder_app"
    }
}