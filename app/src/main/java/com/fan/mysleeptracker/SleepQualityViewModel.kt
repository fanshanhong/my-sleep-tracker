package com.fan.mysleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fan.mysleeptracker.db.SleepDatabase
import com.fan.mysleeptracker.db.SleepDatabaseDao
import com.fan.mysleeptracker.db.SleepNight

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/3/10 16:59
 * @Modify:
 */
class SleepQualityViewModel : ViewModel {

    private lateinit var sleepDatabaseDao: SleepDatabaseDao

    private var nightId: Long = 0

    /**
     * Variable that tells the fragment whether it should navigate to [SleepTrackerFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    /**
     * When true immediately navigate back to the [SleepTrackerFragment]
     */
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    /**
     * Call this immediately after navigating to [SleepTrackerFragment]
     */
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }


    constructor(application: Application, nightId: Long) {
        sleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        this.nightId = nightId
    }


    suspend fun saveQuality(quality: Int) {
        val get = sleepDatabaseDao.get(nightId)
        get.sleepQuality = quality
        sleepDatabaseDao.update(get)
        println("====saveQuality")
    }

    fun notifyNavigateBack() {
        println("====notifyNavigateBack")
        _navigateToSleepTracker.postValue(true)
    }


}