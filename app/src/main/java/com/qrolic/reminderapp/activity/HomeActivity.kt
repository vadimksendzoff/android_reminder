package com.qrolic.reminderapp.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.navigation.NavigationView
import com.qrolic.reminderapp.BuildConfig
import com.qrolic.reminderapp.R
import com.qrolic.reminderapp.util.MARKET_URL
import com.qrolic.reminderapp.util.PLAYSTORE_BASE_URL


class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val mAdView:AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_calendar,
                R.id.nav_done,
                R.id.nav_over_due,
                R.id.nav_today,
                R.id.nav_tomorrow,
                R.id.nav_upcoming,
                R.id.nav_settings,
                R.id.nav_help,
                R.id.nav_feedback,
                R.id.nav_rate,
                R.id.nav_share
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var menu = navView.menu
        var share = menu.findItem(R.id.nav_share)
        var rate = menu.findItem(R.id.nav_rate)
        var feedback = menu.findItem(R.id.nav_feedback)


        share.setOnMenuItemClickListener { menuItem ->
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, "Android Studio Pro")
            intent.putExtra(
                Intent.EXTRA_TEXT,
                PLAYSTORE_BASE_URL + BuildConfig.APPLICATION_ID
            )
            intent.type = "text/plain"
            startActivity(intent)

            return@setOnMenuItemClickListener true
        }

        rate.setOnMenuItemClickListener { menuItem ->
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(MARKET_URL + BuildConfig.APPLICATION_ID)
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(PLAYSTORE_BASE_URL + BuildConfig.APPLICATION_ID)
                    )
                )
            }
            return@setOnMenuItemClickListener true
        }

        feedback.setOnMenuItemClickListener { menuItem ->
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("info@qrolic.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Reminder App")
            intent.putExtra(Intent.EXTRA_TEXT, "Feedback : ")
            intent.putExtra(Intent.EXTRA_CC, "info@qrolic.com")
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
            return@setOnMenuItemClickListener true
        }

    }
    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}