package com.fan.mysleeptracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/3/10 16:40
 * @Modify:
 */

@Entity(tableName = "my_sleep_night")
class SleepNight {

    @PrimaryKey(autoGenerate = true)
    var nightId: Long = 0L

    @ColumnInfo
    var startTime: Long = System.currentTimeMillis()

    @ColumnInfo
    var endTime: Long = startTime

    @ColumnInfo
    var sleepQuality: Int = -1

    override fun toString(): String {
        return "SleepNight(nightId=$nightId, startTime=$startTime, endTime=$endTime, sleepQuality=$sleepQuality)"
    }
}