package com.qrolic.reminderapp.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.qrolic.reminderapp.database.ReminderDatabase
import com.qrolic.reminderapp.util.*
import kotlinx.coroutines.launch


class BootActivity : AppCompatActivity() {
    private lateinit var s_intentFilter: IntentFilter
    lateinit var myBroadCastReciver: MyBroadCastReciver
    lateinit var reminderDatabase:ReminderDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBroadCastReciver = MyBroadCastReciver()
        Log.d("BootActivity", "onCreate: ")
        s_intentFilter = IntentFilter()
        s_intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        reminderDatabase = ReminderDatabase.invoke(this)
        if (intent != null && intent.hasExtra(BOOT)){
            getReminderList()
        }else if (intent!= null && intent?.getStringExtra(REMINDER_TITLE)!=null){
            var reminderTitle = intent?.getStringExtra(REMINDER_TITLE)
            var reportAs = intent?.getStringExtra(SELECTED_REPORTAS)
            var alarmId = intent?.getStringExtra(ALARM_ID)
            var millies = intent?.getStringExtra(MILLIES)
            var notificationId = intent?.getStringExtra(NOTIFICATION_ID)
            toast("Alarm is set for next 10 minutes")
            val notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId!!.toInt())
            onScheduleAlarmFor10Minutes(
                millies!!.toLong(),
                reminderTitle!!,
                alarmId!!.toInt(),
                reportAs!!.toString()
            )

            finish()
        }
        finish()
    }

    override fun onPostResume() {
        registerReceiver(myBroadCastReciver, s_intentFilter)
        super.onPostResume()
    }

    override fun onPause() {
        unregisterReceiver(myBroadCastReciver)
        super.onPause()
    }

    fun getReminderList() {
        lifecycleScope.launch {
            this.coroutineContext.let {
                var reminderTable  = reminderDatabase.addNewReminderDao().fetchDoneReminder(false)
                for (i in reminderTable){
                    val dateTime =  dateTimeFormat.parse(i.date + " " + i.time)
                    val millies = dateTime.time
                    val reminderTitle = i.title
                    val selectedRepeat =i.repeat
                    val alarmId = i.alarmId.toInt()
                    val selectedReportAs =i.reportAs
                    onSchedulAlarm(
                        millies,
                        reminderTitle!!,
                        selectedRepeat!!,
                        alarmId,
                        selectedReportAs!!
                    )
                }
                finish()
            }
        }
    }

    /*
   *
   * Notification snooze
   * */

    fun getSnoozeIntent(
        millies: String?,
        reminderTitle: String?,
        alarmId: String?,
        reportAs: String?,
        notificationId:String?,
        context: Context?
    ): PendingIntent? {
        val intent = Intent(context, BootActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(MILLIES, millies)
        intent.putExtra(REMINDER_TITLE, reminderTitle)
        intent.putExtra(ALARM_ID, alarmId)
        intent.putExtra(SELECTED_REPORTAS, reportAs)
        intent.putExtra(NOTIFICATION_ID, notificationId)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

}
