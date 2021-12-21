package com.qrolic.reminderapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddNewReminderTable(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "date") var date: String?,
    @ColumnInfo(name = "time") var time: String?,
    @ColumnInfo(name = "repeat") var repeat: String?,
    @ColumnInfo(name = "marker") var marker: String?,
    @ColumnInfo(name = "reportAs") var reportAs: String?,
    @ColumnInfo(name = "notifyAdv") var notifyAdv: String?,
    @ColumnInfo(name = "isFav") var isFav:Boolean,
    @ColumnInfo(name = "alarmId") var alarmId:Int,
    @ColumnInfo(name = "isDone") var isDone:Boolean
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(repeat)
        parcel.writeString(marker)
        parcel.writeString(reportAs)
        parcel.writeString(notifyAdv)
        parcel.writeByte(if (isFav) 1 else 0)
        parcel.writeInt(alarmId)
        parcel.writeByte(if (isDone) 1 else 0)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddNewReminderTable> {
        override fun createFromParcel(parcel: Parcel): AddNewReminderTable {
            return AddNewReminderTable(parcel)
        }

        override fun newArray(size: Int): Array<AddNewReminderTable?> {
            return arrayOfNulls(size)
        }
    }
}