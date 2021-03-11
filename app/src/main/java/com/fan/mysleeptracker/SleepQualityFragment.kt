package com.fan.mysleeptracker

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.sleeptracker.SleepQualityViewModelFactory
import com.fan.mysleeptracker.databinding.FragmentSleepQualityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/3/10 15:40
 * @Modify:
 */
class SleepQualityFragment : Fragment() {

    private lateinit var viewModel: SleepQualityViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = DataBindingUtil.inflate<FragmentSleepQualityBinding>(
            inflater,
            R.layout.fragment_sleep_quality,
            container,
            false
        )


        val application = requireNotNull(activity).application


        // 获取上一个页面传入的参数
        val nightId = arguments?.getLong("nightId")

        if (nightId != null)
            viewModel =
                ViewModelProvider(this, SleepQualityViewModelFactory(application, nightId)).get(
                    SleepQualityViewModel::class.java
                )

        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                // 返回
                findNavController().navigate(R.id.action2)
//                // Reset state to make sure we only navigate once, even if the device
//                // has a configuration change.
                viewModel.doneNavigating()
            }
        })

        contentView.quality1.setOnClickListener { onQualitySelect(1) }

        contentView.quality2.setOnClickListener { onQualitySelect(2) }

        contentView.quality3.setOnClickListener { onQualitySelect(3) }
        return contentView.root
    }

    /**
     * 当选择了一个睡眠质量等级
     */
    private fun onQualitySelect(quality: Int) {

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.saveQuality(quality)

            viewModel.notifyNavigateBack()
        }
    }
}