package com.example.mynotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class NotificationViewModel : ViewModel() {

    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String> get() = _selectedTime

    fun setTime(notificationTime:String) {
        _selectedTime.value = notificationTime
    }


}