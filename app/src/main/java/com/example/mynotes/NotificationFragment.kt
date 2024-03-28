package com.example.mynotes

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.mynotes.databinding.FragmentNotificationBinding
import com.example.mynotes.utils.PasswordManager
import com.example.mynotes.viewmodel.NotificationViewModel
import java.util.Calendar
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class NotificationFragment : Fragment() {

    private var selectedHour = 0
    private var selectedMinute = 0

    lateinit var binding:FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var passwordManager : PasswordManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordManager = PasswordManager(requireContext())
        binding.switchOnForNotification.isChecked = isDailyNotificationScheduled()
        if(isDailyNotificationScheduled()){
            binding.llSetNotification.visibility = View.VISIBLE
        }else{
            binding.llSetNotification.visibility = View.GONE
        }
        updateTime()
        initClickListeners()

    }

    private fun initClickListeners(){
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.switchOnForNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                //showTimePickerDialog()
                binding.llSetNotification.visibility = View.VISIBLE
            }else{
                cancelScheduledNotification()
                passwordManager.setNotificationTime("")
                binding.llSetNotification.visibility = View.GONE
                Toast.makeText(requireContext(),"Work Manager Cancel",Toast.LENGTH_SHORT).show()            }
        }
        binding.btnScheduleNotification.setOnClickListener {
            showTimePickerDialog()
        }

        Log.d(TAG, "initClickListeners: ${isWorkScheduled()}")
    }

    private fun isDailyNotificationScheduled(): Boolean {
        return !passwordManager.getNotificationTime().isNullOrEmpty()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                val selectedTime = "$selectedHour:$selectedMinute"
                viewModel.setTime(selectedTime)
                passwordManager.setNotificationTime(selectedTime)
                scheduleNotification(selectedHour,selectedMinute)
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        viewModel.selectedTime.observe(viewLifecycleOwner){
            binding.tvNotificationTime.text = "Daily Notification time scheduled at ${convertTo12HourFormat(it)}"
        }
        binding.tvNotificationTime.text = "Daily Notification time time scheduled at ${convertTo12HourFormat(passwordManager.getNotificationTime()!!)}"

    }
    private fun convertTo12HourFormat(time24: String): String {
        if (time24 == ""){
            return "__:__"
        }
        val timeParts = time24.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1]

        val amPm = if (hour < 12) "AM" else "PM"
        val hour12 = if (hour == 0) 12 else if (hour <= 12) hour else hour - 12

        return String.format("%d:%s %s", hour12, minute, amPm)
    }

    private fun isWorkScheduled(): Boolean {
        val instance = WorkManager.getInstance()
        val statuses = instance.getWorkInfosByTag("DailyNotificationWork")
        return try {
            var running = false
            val workInfoList = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }

    private fun scheduleNotification(hour: Int, minute: Int) {
        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        ).setInitialDelay(calculateInitialDelay(hour, minute), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(requireContext().applicationContext).enqueueUniquePeriodicWork(
            "DailyNotificationWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWorkRequest
        )
    }

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return calendar.timeInMillis - System.currentTimeMillis()
    }

    private fun cancelScheduledNotification() {
        WorkManager.getInstance(requireContext().applicationContext).cancelUniqueWork("DailyNotificationWork")
    }

    companion object {
        const val CHANNEL_ID = "DailyNotifications"
        const val NOTIFICATION_ID = 1
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}