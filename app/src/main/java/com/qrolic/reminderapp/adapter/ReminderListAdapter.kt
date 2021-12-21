package com.qrolic.reminderapp.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qrolic.reminderapp.R
import com.qrolic.reminderapp.database.MySharedPreferences
import com.qrolic.reminderapp.databinding.InflaterReminderListBinding
import com.qrolic.reminderapp.model.AddNewReminderTable
import com.qrolic.reminderapp.util.*
import java.util.*


internal class ReminderListAdapter(
    var context: Context, private var addNewReminderTableList: List<AddNewReminderTable>,
    val clickListener: OnClickListner, var sharedPreferences: MySharedPreferences
) : RecyclerView.Adapter<ReminderListAdapter.MyViewHolder>(),Filterable {


    var reminderFilterableList:List<AddNewReminderTable>

    init {
        reminderFilterableList = addNewReminderTableList
    }
    open interface OnClickListner {
        fun onStarClick(position: Int)
        fun onDotsClick(position: Int)
        fun onMobileClick(position: Int)
        fun onItemClick(position: Int)
    }

    internal inner class MyViewHolder(val inflaterReminderListBinding: InflaterReminderListBinding) : RecyclerView.ViewHolder(
        inflaterReminderListBinding.root
    ) {

        fun bind(addNewReminderTableList: AddNewReminderTable?) {
          with(inflaterReminderListBinding){
              tvReminderTitle.text = addNewReminderTableList?.title
              var fountSize:Float= when(sharedPreferences.getFontSize())
              {
                  FOUNT_SIZE_12_SP -> context.resources.getDimension(R.dimen._12sdp)
                  FOUNT_SIZE_14_SP -> context.resources.getDimension(R.dimen._14sdp)
                  FOUNT_SIZE_16_SP -> context.resources.getDimension(R.dimen._16sdp)
                  FOUNT_SIZE_18_SP -> context.resources.getDimension(R.dimen._18sdp)
                  FOUNT_SIZE_20_SP -> context.resources.getDimension(R.dimen._20sdp)
                  FOUNT_SIZE_22_SP -> context.resources.getDimension(R.dimen._22sdp)
                  FOUNT_SIZE_24_SP -> context.resources.getDimension(R.dimen._24sdp)
                  FOUNT_SIZE_26_SP -> context.resources.getDimension(R.dimen._26sdp)
                  FOUNT_SIZE_28_SP -> context.resources.getDimension(R.dimen._28sdp)
                  FOUNT_SIZE_30_SP -> context.resources.getDimension(R.dimen._30sdp)
                  FOUNT_SIZE_32_SP -> context.resources.getDimension(R.dimen._32sdp)
                  else->context.resources.getDimension(R.dimen._14sdp)
              }

              Log.d(
                  "ReminderListAdapter",
                  "$fountSize fount size:-${sharedPreferences.getFontSize()}"
              )

              tvReminderTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, fountSize)
              var time = addNewReminderTableList?.time.toString().replace(" ", "\n")
              tvTime.text = time
              tvDate.text = addNewReminderTableList?.date
              tvRepeat.text = addNewReminderTableList?.repeat


              var marker = addNewReminderTableList?.marker
              if (marker.equals("Pink")){
                  Glide
                      .with(context)
                      .load(R.drawable.pink_full_rounded_corner)
                      .centerCrop()
                      .into(ivReminderMarker)
                  ivReminderMarker.visibility = View.VISIBLE
                  cvReminder.setCardBackgroundColor(
                      ResourcesCompat.getColor(
                          context.resources,
                          R.color.pink_card_color,
                          null
                      )
                  )
              }else if (marker.equals("Green")){
                  Glide
                      .with(context)
                      .load(R.drawable.green_full_rounded_corner)
                      .centerCrop()
                      .into(ivReminderMarker)
                  ivReminderMarker.visibility = View.VISIBLE
                  cvReminder.setCardBackgroundColor(
                      ResourcesCompat.getColor(
                          context.resources,
                          R.color.green,
                          null
                      )
                  )
              }else{
                  Glide
                      .with(context)
                      .load(R.drawable.gray_full_rounded_corner)
                      .centerCrop()
                      .into(ivReminderMarker)
                  ivReminderMarker.visibility = View.VISIBLE
                  cvReminder.setCardBackgroundColor(
                      ResourcesCompat.getColor(
                          context.resources,
                          R.color.gray,
                          null
                      )
                  )

              }

              if(addNewReminderTableList?.reportAs.equals(ALARM)){
                  Glide
                      .with(context)
                      .load(R.drawable.ic_baseline_alarm_24)
                      .centerCrop()
                      .into(ivReportAs)
              }else{
                  Glide
                      .with(context)
                      .load(R.drawable.ic_baseline_notifications_none_24)
                      .centerCrop()
                      .into(ivReportAs)
              }
              if (addNewReminderTableList?.isFav!!){
                  ivFavReminder.setColorFilter(
                      ContextCompat.getColor(context, R.color.yellow),
                      android.graphics.PorterDuff.Mode.MULTIPLY
                  );
              }else{
                  ivFavReminder.setColorFilter(
                      ContextCompat.getColor(context, R.color.white),
                      android.graphics.PorterDuff.Mode.MULTIPLY
                  );
              }

              ivFavReminder.setOnClickListener{ view ->
                  clickListener.onStarClick(position)
              }

              ivDots.setOnClickListener{ view ->
                  clickListener.onDotsClick(position)
              }

              tvReminderTitle.setOnClickListener{ view ->
                  if (tvReminderTitle.text.toString().contains("Call: ")) {
                      clickListener.onMobileClick(position)
                  }
              }

              llItem.setOnClickListener{ view ->
                  clickListener.onItemClick(position)
              }
          }
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflaterReminderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.inflater_reminder_list, parent, false
        ) as InflaterReminderListBinding
        return MyViewHolder(inflaterReminderListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(
        reminderFilterableList[position]
    )

    override fun getItemCount(): Int = reminderFilterableList.size

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charsequence = charSequence.toString()
                if (charsequence.isEmpty()) {
                    reminderFilterableList = addNewReminderTableList
                } else {
                    val addNewReminderTable: MutableList<AddNewReminderTable> = ArrayList<AddNewReminderTable>()
                    for (row in addNewReminderTableList) {
                        if (row.title!!.toLowerCase().contains(charsequence.toLowerCase())) {
                            addNewReminderTable.add(row)
                        }
                    }
                    reminderFilterableList = addNewReminderTable
                }
                val filterResults = FilterResults()
                filterResults.values = reminderFilterableList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                reminderFilterableList = filterResults.values as ArrayList<AddNewReminderTable>
                notifyDataSetChanged()
            }
        }
    }

}