package com.qrolic.reminderapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.qrolic.reminderapp.R
import com.qrolic.reminderapp.databinding.FragmentHelpBinding


class AboutFragment : Fragment() {

    private lateinit var fragmentHelpBinding: FragmentHelpBinding
    var i = 0
    var j = 0
    var z = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHelpBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false)
        initAll()
        return fragmentHelpBinding.root
    }

    private fun initAll() {

        fragmentHelpBinding.llDozeModeQue.setOnClickListener(View.OnClickListener { view ->

            if (i % 2 == 0) {
                fragmentHelpBinding.llDozeModeAnswer.visibility = View.VISIBLE
                fragmentHelpBinding.ivArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                i++
            } else {
                fragmentHelpBinding.llDozeModeAnswer.visibility = View.GONE
                fragmentHelpBinding.ivArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                i++
            }

        })

        fragmentHelpBinding.llRepeateIntervalQue.setOnClickListener(View.OnClickListener { view ->

            if (j % 2 == 0) {
                fragmentHelpBinding.llRepeateIntervalAnswer.visibility = View.VISIBLE
                fragmentHelpBinding.ivRepeateIntervalArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                j++
            } else {
                fragmentHelpBinding.llRepeateIntervalAnswer.visibility = View.GONE
                fragmentHelpBinding.ivRepeateIntervalArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                j++
            }

        })

        fragmentHelpBinding.llContactQue.setOnClickListener(View.OnClickListener { view ->

            if (z % 2 == 0) {
                fragmentHelpBinding.llContactAnswer.visibility = View.VISIBLE
                fragmentHelpBinding.ivContactArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                z++
            } else {
                fragmentHelpBinding.llContactAnswer.visibility = View.GONE
                fragmentHelpBinding.ivContactArrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                z++
            }

        })

        fragmentHelpBinding.tvContactUs.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("info@qrolic.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Reminder App")
            intent.putExtra(Intent.EXTRA_TEXT, "Feedback : ")
            intent.putExtra(Intent.EXTRA_CC, "info@qrolic.com")
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        })

    }

}