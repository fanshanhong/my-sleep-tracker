package com.fan.mysleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.fan.mysleeptracker.db.SleepDatabase
import com.fan.mysleeptracker.db.SleepDatabaseDao
import com.fan.mysleeptracker.db.SleepNight

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/3/10 16:59
 * @Modify:
 */
class TrackerViewModel : AndroidViewModel {

    private lateinit var sleepDatabaseDao: SleepDatabaseDao

    constructor(application: Application) : super(application) {
        sleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        _nights = sleepDatabaseDao.getAll()
    }

    var tonight = MutableLiveData<SleepNight?>()

    init {
        tonight.value = null
    }

    private lateinit var _nights:LiveData<List<SleepNight>>
    val nights:LiveData<List<SleepNight>>
        get() = _nights



    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality


    suspend fun insert(night: SleepNight) {
        sleepDatabaseDao.insert(night)
    }

    suspend fun update(night: SleepNight) {
        sleepDatabaseDao.update(night)
    }

    fun getAll() {
        val all = sleepDatabaseDao.getAll()
        println(all)
    }

    suspend fun getTonight(): SleepNight? {
        return sleepDatabaseDao.getTonight()
    }

    fun notifyToNavigate(night: SleepNight?) {
        _navigateToSleepQuality.postValue(night)
    }

    suspend fun clear(){
        sleepDatabaseDao.clear()
    }
}