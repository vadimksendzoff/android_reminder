package com.qrolic.reminderapp.util

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.Patterns
import android.widget.Toast
import java.text.SimpleDateFormat


fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.onSchedulAlarm(
    millies: Long,
    reminderTitle: String,
    selectedRepeat: String,
    alarmId: Int,
    selectedReportAs: String
){
    var intent = Intent(this, MyBroadCastReciver::class.java)
    intent.putExtra(REMINDER_TITLE, reminderTitle)
    intent.putExtra(SELECTED_REPORTAS, selectedReportAs)
    intent.putExtra(ALARM_ID, alarmId.toString())
    intent.putExtra(MILLIES, millies.toString())
    /*
    * alarm id must be unique for handle multiple alarms
    * if you want to handle only one alarm than use 0 instead of alarmId
    * */
        var pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (selectedRepeat.equals(ONCE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millies, pendingIntent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP, millies, pendingIntent)
            } else {
                am.set(AlarmManager.RTC_WAKEUP, millies, pendingIntent)
            }
        } else if (selectedRepeat.equals(HOURLY)) {
            am.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                millies,
                AlarmManager.INTERVAL_HOUR,
                pendingIntent
            )
        } else if (selectedRepeat.equals(DAILY)) {
            am.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                millies,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else if (selectedRepeat.equals(WEEKLY)) {
            //Do not use setInExactRepeating() method while using custom time interval
            am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                millies,
                WEEK_IN_MILLIES.toLong(),
                pendingIntent
            )
        } else if (selectedRepeat.equals(MONTHLY)) {
            //Do not use setInExactRepeating() method while using custom time interval
            am.setRepeating(AlarmManager.RTC_WAKEUP, millies, MONTH_IN_MILLIES, pendingIntent)
        } else if (selectedRepeat.equals(YEARLY)) {
            //Do not use setInExactRepeating() method while using custom time interval
            am.setRepeating(AlarmManager.RTC_WAKEUP, millies, YEAR_IN_MILLIES, pendingIntent)
        }

}

fun Context.onCancelAlarm(alarmId: Int){
    val alarmManager =
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    /*
    * alarm id must be unique for handle multiple alarms
    * if you want to handle only one alarm than use 0 instead of alarmId
    * */
    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext, alarmId, Intent(
            this,
            MyBroadCastReciver::class.java
        ), 0
    )
    alarmManager.cancel(pendingIntent)
}


/**
 * Returns true if the device is locked or screen turned off (in case password not set)
 */
fun isDeviceLocked(context: Context): Boolean {
    var isLocked = false

    // First we check the locked state
    val keyguardManager =
        context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    val inKeyguardRestrictedInputMode =
        keyguardManager.inKeyguardRestrictedInputMode()
    isLocked = if (inKeyguardRestrictedInputMode) {
        true
    } else {
        // If password is not set in the settings, the inKeyguardRestrictedInputMode() returns false,
        // so we need to check if screen on for this case
        val powerManager =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            !powerManager.isInteractive
        } else {
            !powerManager.isScreenOn
        }
    }
    return isLocked
}

val simpleDateFormat = SimpleDateFormat("hh:mm a")
val dateFormat = SimpleDateFormat("dd/MM/yyyy")
val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
val dateTime24HourFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

fun Context.onScheduleAlarmFor10Minutes(
    millies: Long,
    reminderTitle: String,
    alarmId: Int,
    selectedReportAs: String
){
    var intent = Intent(this, MyBroadCastReciver::class.java)
    intent.putExtra(REMINDER_TITLE, reminderTitle)
    intent.putExtra(SELECTED_REPORTAS, selectedReportAs)
    intent.putExtra(ALARM_ID, alarmId.toString())
    intent.putExtra(MILLIES, millies.toString())

    /*
   * alarm id must be unique for handle multiple alarms
   * if you want to handle only one alarm than use 0 instead of alarmId
   * */
    var pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0)
    val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                millies + INTERVAL_TEN_MINUTES,
                pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, millies + INTERVAL_TEN_MINUTES, pendingIntent)
        } else {
            am.set(AlarmManager.RTC_WAKEUP, millies + INTERVAL_TEN_MINUTES, pendingIntent)
        }
}

fun isValidMobile(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

fun Context.openDailer(phone:String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:"+phone)
        startActivity(intent)

}