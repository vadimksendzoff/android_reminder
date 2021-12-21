package com.qrolic.reminderapp.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.qrolic.reminderapp.R
import com.qrolic.reminderapp.database.MySharedPreferences
import com.qrolic.reminderapp.util.CHANNEL_ID


class SplashActivity : AppCompatActivity() {

    lateinit var mySharedPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initAll()
    }

    private fun initAll() {
        MobileAds.initialize(this) {}
        supportActionBar?.hide()

        requestPermission()

    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                openSettings()
            } else {
                openMainActivity()
            }
        } else {
            openMainActivity()
        }
    }

    private fun openSettings() {

        val materialDialogBuilder =
            MaterialAlertDialogBuilder(this@SplashActivity)
        materialDialogBuilder.setTitle("Grant Permission")
        materialDialogBuilder.setMessage("Allow Reminder App to show reminder in your device")
        materialDialogBuilder.setNegativeButton("Deny") { dialog, which ->
            // Respond to neutral button press
            requestPermission()
        }
        materialDialogBuilder.setPositiveButton("Allow") { dialog, which ->
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + this.packageName)

            )
            startActivityForResult(intent, 100)

        }
        materialDialogBuilder.show()
        materialDialogBuilder.setCancelable(false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){
            requestPermission()
        }
    }


    private fun openMainActivity() {
        createNotiChannel()
        mySharedPreferences= MySharedPreferences(this)
        val currentTone: Uri = RingtoneManager.getActualDefaultRingtoneUri(
            this,
            RingtoneManager.TYPE_ALARM
        )
        mySharedPreferences.setAlarmSound(currentTone.path.toString())
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 30)
    }

    private fun createNotiChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = "Reminder channel"
            var desc = "Reminder description"
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc

            var notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


}