package com.fan.mysleeptracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.fan.mysleeptracker.databinding.ActivityMainBinding

/**
 * 自己实现Google code lab 的 TrackMySleepQuality, 不使用 databinding
 */
class MainActivity : AppCompatActivity() {

    lateinit var contentView: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        contentView = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
}