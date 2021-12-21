package com.qrolic.reminderapp.model

import androidx.room.*

@Dao
interface AddNewReminderDao {

    @Insert
    suspend fun insertAll(addNewReminderTable: AddNewReminderTable):Long

    @Query("SELECT * FROM AddNewReminderTable")
    suspend fun fetchAllData(): List<AddNewReminderTable>

    @Query("SELECT * FROM AddNewReminderTable WHERE isFav=:isFav")
    suspend fun fetchFavouritesReminder(isFav:Boolean): List<AddNewReminderTable>

    @Query("SELECT * FROM AddNewReminderTable WHERE isDone=:isDone")
    suspend fun fetchDoneReminder(isDone:Boolean): List<AddNewReminderTable>

    @Query("SELECT * FROM AddNewReminderTable WHERE date=:date")
    suspend fun fetchDateWiseReminder(date:String): List<AddNewReminderTable>

    @Update
    suspend fun updateAll(addNewReminderTable: AddNewReminderTable):Int

    @Delete
    suspend fun deleteData(addNewReminderTable: AddNewReminderTable): Int
}