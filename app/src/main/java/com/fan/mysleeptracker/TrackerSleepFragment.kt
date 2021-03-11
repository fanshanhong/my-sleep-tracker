package com.fan.mysleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.sleeptracker.TrackerViewModelFactory
import com.fan.mysleeptracker.databinding.FragmentTrackerSleepBinding
import com.fan.mysleeptracker.db.SleepNight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @Description:
 * @Author: shanhongfan
 * @Date: 2021/3/10 15:39
 * @Modify:
 */
class TrackerSleepFragment : Fragment() {


    private lateinit var viewModel: TrackerViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = DataBindingUtil.inflate<FragmentTrackerSleepBinding>(
            inflater,
            R.layout.fragment_tracker_sleep,
            container,
            false
        )

        println("=====enter TrackerSleepFragment onCreateView")


        // 从 fragment 中获取所在的 activity 的 application
        val application = requireNotNull(this.activity).application

        // 获取 viewModel
        viewModel = ViewModelProvider(
            this,
            TrackerViewModelFactory(application)
        ).get(TrackerViewModel::class.java)



        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer {

            // 这个时候, it 就是 oldNight
            // oldNight已经有了 nightId 和 startTime 和 endTime
            // 跳转, 并把参数 nightId 传入到下一页面
            // 因为下一页面, 要使用nightId 来更新quality
            val bundle = Bundle()

            bundle.putLong("nightId", it.nightId)

            // 跳转, 这里应该是在主线程的
            println("====before navigate, nightId: ${it.nightId}, threadName : ${Thread.currentThread().name}")

            // 这里把 nightId 传入下一个页面了. 直接传入 it 也行.

            findNavController().navigate(R.id.action1, bundle)
        })

        Transformations.map(viewModel.nights) {
            formatNights(it)
        }.observe(viewLifecycleOwner, Observer {
            contentView.textview.text = it
        })

        /**
         * If tonight has not been set, then the START button should be visible.
         */
        Transformations.map(viewModel.tonight, { null == it })
            .observe(viewLifecycleOwner, Observer {
                contentView.startButton.isEnabled = it
            })

        /**
         * If tonight has been set, then the STOP button should be visible.
         */
        Transformations.map(viewModel.tonight, { null != it })
            .observe(viewLifecycleOwner, Observer {
                contentView.endButton.isEnabled = it
            })


    /**
     * If there are any nights in the database, show the CLEAR button.
     */

        Transformations.map(viewModel.nights) {
            it.isEmpty()
        }.observe(viewLifecycleOwner, Observer {
            contentView.clear.isEnabled = !it
        })


        contentView.startButton.setOnClickListener {

            // 1. 创建一个新的 nightSleep 记录, 插入表中.
            // 此时, 只有 startTime 是有效值. endTime = startTime. sleepNight的 nightId 是0, 插入数据库的时候会自动生成
            val sleepNight = SleepNight()

            // kotlin 协程, 指定在子线程上运行. 默认不指定的话, 是Dispatchers.MAIN, 就会在主线程上运行
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                // 这里打印一下线程
                println("====startButton:${Thread.currentThread().name}")

                // 2. 插入
                viewModel.insert(sleepNight)

                // 3. 把第一条作为 tonight, 在end的时候要用. end的时候, 要写入 endTime.
                // 这里之所以在再从数据库中取出来, 是因为 那个 nightId 是数据库自动生成的.
                // 这个时候, viewModel.tonight 已经有了 nightId 和 startTime
                viewModel.tonight.postValue(viewModel.getTonight())
            }
        }


        // 点击结束, 要把tonight 的 endTime 赋值, 然后存到数据库
        contentView.endButton.setOnClickListener {
            viewModel.viewModelScope.launch(Dispatchers.IO) {

                // 这里打印一下线程
                println("====endButton:${Thread.currentThread().name}")

                val oldNight = viewModel.tonight.value

                // 写入 endTime 值
                oldNight?.endTime = System.currentTimeMillis()

                // 这个时候, viewModel.tonight 已经有了 nightId 和 startTime
                //                    oldNight已经有了 nightId 和 startTime 和 endTime

                // 写入数据库
                if (oldNight != null)
                    viewModel.update(oldNight)


                // 写入完成后, 要通知页面跳转. 为了通知, 我们加个LiveData
                viewModel.notifyToNavigate(oldNight)
            }
        }

        contentView.selectAll.setOnClickListener {
            viewModel.viewModelScope.launch {
                //                viewModel.getAll()
            }
        }

        contentView.clear.setOnClickListener {

            viewModel.viewModelScope.launch {
                viewModel.clear()
                viewModel.tonight.value = null
            }

        }

        return contentView.root
    }
}