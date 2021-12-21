package com.qrolic.reminderapp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.qrolic.reminderapp.R
import com.qrolic.reminderapp.database.MySharedPreferences
import com.qrolic.reminderapp.databinding.FragmentSettingsBinding
import com.qrolic.reminderapp.util.*


class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding;
    private lateinit var mySharedPreferences: MySharedPreferences;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        initAllControls()
        return fragmentSettingsBinding.root;
    }

    private fun initAllControls() {
        mySharedPreferences = MySharedPreferences(requireContext());
        fragmentSettingsBinding.llFontSize.setOnClickListener(this)
        fragmentSettingsBinding.llAlarmSound.setOnClickListener(this)
        fragmentSettingsBinding.tvSettingsFontSize.setText(mySharedPreferences.getFontSize())

        fragmentSettingsBinding.tvAlarmSoundDefault.text =mySharedPreferences.getAlarmSound()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            fragmentSettingsBinding.llFontSize.id -> onFontSizeButtonClicked()
            fragmentSettingsBinding.llAlarmSound.id ->    settingPermission()
        }
    }

    private fun onAlarmSoundButtonClicked() {

        val currentTone: Uri = RingtoneManager.getActualDefaultRingtoneUri(
            context,
            RingtoneManager.TYPE_ALARM
        )
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        startActivityForResult(intent, 999)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 999 && resultCode == RESULT_OK) {
            val uri = data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            fragmentSettingsBinding.tvAlarmSoundDefault.setText(uri!!.path)
            mySharedPreferences.setAlarmSound(uri!!.path.toString())
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun settingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context)) {
                onAlarmSoundButtonClicked()

            } else {
                openSettings()

            }
        } else {
            onAlarmSoundButtonClicked()
        }
    }

    private fun openSettings() {

        val materialDialogBuilder =
            MaterialAlertDialogBuilder(requireContext())
        materialDialogBuilder.setTitle("Grant Permission")
        materialDialogBuilder.setMessage("Allow Reminder App to change ringtone of your device")
        materialDialogBuilder.setNegativeButton("Deny") { dialog, which ->

            settingPermission()
        }
        materialDialogBuilder.setPositiveButton("Allow") { dialog, which ->
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity?.getPackageName())
            startActivityForResult(intent, 999)

        }
        materialDialogBuilder.show()
        materialDialogBuilder.setCancelable(false)
    }


    private fun onFontSizeButtonClicked() {
        val materialDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext())
        materialDialogBuilder.setTitle("Font Size")
        val singleItems = arrayOf(
            FOUNT_SIZE_12_SP,
            FOUNT_SIZE_14_SP,
            FOUNT_SIZE_16_SP,
            FOUNT_SIZE_18_SP,
            FOUNT_SIZE_20_SP,
            FOUNT_SIZE_22_SP,
            FOUNT_SIZE_24_SP,
            FOUNT_SIZE_26_SP,
            FOUNT_SIZE_28_SP,
            FOUNT_SIZE_30_SP,
            FOUNT_SIZE_32_SP
        );
        var checkedItem = 0
        materialDialogBuilder.setNeutralButton("Cancel") { dialog, which ->
            // Respond to neutral button press
        }
        materialDialogBuilder.setPositiveButton("Okay") { dialog, which ->
            // Respond to positive button press
        }

        /*
        * default checked item
        * */
        checkedItem = when (mySharedPreferences.getFontSize()) {
            FOUNT_SIZE_12_SP -> 0
            FOUNT_SIZE_14_SP -> 1
            FOUNT_SIZE_16_SP -> 2
            FOUNT_SIZE_18_SP -> 3
            FOUNT_SIZE_20_SP -> 4
            FOUNT_SIZE_22_SP -> 5
            FOUNT_SIZE_24_SP -> 6
            FOUNT_SIZE_26_SP -> 7
            FOUNT_SIZE_28_SP -> 8
            FOUNT_SIZE_30_SP -> 9
            FOUNT_SIZE_32_SP -> 10
            else -> 0
        }


        // Single-choice items (initialized with checked item)
        materialDialogBuilder.setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->

            // Respond to item chosen
            mySharedPreferences.setFontSize(singleItems[which])
            fragmentSettingsBinding.tvSettingsFontSize.setText("${singleItems[which]}")
        }
        materialDialogBuilder.show()
    }


}