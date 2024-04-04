package com.example.mynotes.utils

import android.content.Context
import com.example.mynotes.utils.Constants.IS_BIOMETRIC_LOCK_ON
import com.example.mynotes.utils.Constants.IS_LOCK_ON
import com.example.mynotes.utils.Constants.PREFS_PASS_KEY_FILE
import com.example.mynotes.utils.Constants.USER_ANSWER
import com.example.mynotes.utils.Constants.USER_PASSWORD
import com.example.mynotes.utils.Constants.USER_QUESTION

class PasswordManager(val context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_PASS_KEY_FILE,Context.MODE_PRIVATE)

    fun savePassword(password:String){
        val editor = prefs.edit()
        editor.putString(USER_PASSWORD,password)
        editor.apply()
    }
    fun getPassword() : String?{
        return prefs.getString(USER_PASSWORD,null)
    }

    fun saveQuestion(question:String) {
        val editor = prefs.edit()
        editor.putString(USER_QUESTION,question)
        editor.apply()
    }
    fun saveAnswer(answer: String) {
        val editor = prefs.edit()
        editor.putString(USER_ANSWER,answer)
        editor.apply()
    }

    fun getAnswer(): String?{
        return prefs.getString(USER_ANSWER,null)
    }

    fun getQuestion(): String?{
        return prefs.getString(USER_QUESTION,null)
    }

    fun saveLockOn(isLockOn:Boolean){
        val editor = prefs.edit()
        editor.putBoolean(IS_LOCK_ON,isLockOn)
        editor.apply()
    }
    fun isLockOn():Boolean{
        return prefs.getBoolean(IS_LOCK_ON,false)
    }

    fun saveBiometricLockOn(isBiometricLock:Boolean){
        val editor = prefs.edit()
        editor.putBoolean(IS_BIOMETRIC_LOCK_ON,isBiometricLock)
        editor.apply()
    }
    fun isBiometricLockOn():Boolean{
        return prefs.getBoolean(IS_BIOMETRIC_LOCK_ON,false)
    }


    fun saveTheme(theme:Int){
        val editor = prefs.edit()
        editor.putInt("APP_THEME",theme)
        editor.apply()
    }
    fun getTheme() : Int{
        return prefs.getInt("APP_THEME", 0)
    }

    fun setAutoThemeOn(autoTheme:Boolean){
        val editor = prefs.edit()
        editor.putBoolean("AUTO_THEME",autoTheme)
        editor.apply()
    }
    fun isAutoThemeOn() : Boolean{
        return prefs.getBoolean("AUTO_THEME", false)
    }

    fun setNotificationTime(notificationTime : String){
        val editor = prefs.edit()
        editor.putString("NOTIFICATION_TIME",notificationTime)
        editor.apply()
    }
    fun getNotificationTime(): String? {
        return prefs.getString("NOTIFICATION_TIME", "")
    }

    fun setLatestFirst(isLatest : Boolean){
        val editor = prefs.edit()
        editor.putBoolean("LATEST_FIRST",isLatest)
        editor.apply()
    }
    fun isLatestFirst(): Boolean {
        return prefs.getBoolean("LATEST_FIRST", true)
    }

    fun setName(name : String){
        val editor = prefs.edit()
        editor.putString("NAME",name)
        editor.apply()
    }
    fun getName(): String? {
        return prefs.getString("NAME", "")
    }

    fun setNumberOfDiary(noOfDiary : String){
        val editor = prefs.edit()
        editor.putString("NO_OF_DIARY",noOfDiary)
        editor.apply()
    }
    fun getNumberOfDiary(): String? {
        return prefs.getString("NO_OF_DIARY", "")
    }



}