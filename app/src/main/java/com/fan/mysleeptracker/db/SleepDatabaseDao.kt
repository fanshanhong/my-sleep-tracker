package com.fan.mysleeptracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/3/10 16:54
 * @Modify:
 */

@Dao
interface SleepDatabaseDao {

    @Insert
    suspend fun insert(night: SleepNight)

    @Update
    suspend fun update(night: SleepNight)

    @Query("SELECT * FROM my_sleep_night where nightId = :nightId")
    suspend fun get(nightId: Long): SleepNight

    @Query("SELECT * FROM my_sleep_night")
    fun getAll(): LiveData<List<SleepNight>>

    /**
     * Selects and returns the latest night.
     */
    @Query("SELECT * FROM my_sleep_night ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    @Query("DELETE FROM my_sleep_night")
    suspend fun clear()
}