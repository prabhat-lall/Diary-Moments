package com.example.mynotes.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this,"onCreate Login Activity",Toast.LENGTH_SHORT).show()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

    }
}