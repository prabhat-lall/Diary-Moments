package com.example.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mynotes.ui.MainActivity
import com.example.mynotes.ui.login.LoginActivity
import com.example.mynotes.utils.PasswordManager

class SplashScreen : AppCompatActivity() {

    lateinit var passwordManager: PasswordManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        passwordManager = PasswordManager(this)
        supportActionBar?.hide()
        //setStatusBarColor()
        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        fadeOutAnimation()
        goToNextActivity()
    }

    private fun fadeOutAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 2000
        fadeOut.fillAfter = true
        findViewById<View>(R.id.view_alpha).startAnimation(fadeOut)
    }

    private fun goToNextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (passwordManager.isLockOn() || passwordManager.isBiometricLockOn()) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 3000)
    }

    private fun setStatusBarColor() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }
}