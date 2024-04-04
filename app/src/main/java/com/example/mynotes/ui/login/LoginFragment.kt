package com.example.mynotes.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentLoginBinding
import com.example.mynotes.ui.MainActivity
import com.example.mynotes.utils.PasswordManager
import java.util.concurrent.Executor


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var passwordManager: PasswordManager

    lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var periodicAnimation: Runnable

//    private lateinit var imageView: ImageView
//    private val imageResources = arrayOf(
//        R.drawable.baseline_lock_24,
//        R.drawable.baseline_fingerprint_24
//    )
//    private var currentIndex = 0
//    private lateinit var imageChanger: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passwordManager = PasswordManager(requireContext())
        //Toast.makeText(requireContext(), "${passwordManager.isLockOn()}", Toast.LENGTH_SHORT).show()
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //imageView = binding.lockIconImageView

        // Start the loop to change images
       // startImageLoop()
        setListeners()
        setZoomInZoomOutAnimation()
        biometricFingerPrintLock()
    }

    private fun startImageLoop() {
        // Create a Runnable to change the image
        val imageChanger = object : Runnable {
            override fun run() {
//                // Set the next image
//                imageView.setImageResource(imageResources[currentIndex])
//
//                // Increment index for the next image
//                currentIndex = (currentIndex + 1) % imageResources.size
//
//                // Delay for next image change (in milliseconds)
//                val delayMillis = 2000 // Change this value to adjust the delay
//                imageView.postDelayed(this, delayMillis.toLong())
            }
        }

        // Start the loop by posting the Runnable
//        imageView.post(imageChanger)
    }


    private fun biometricFingerPrintLock() {
        if (passwordManager.isBiometricLockOn()) {
            executor = ContextCompat.getMainExecutor(requireActivity())
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        //Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                       // Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
//                        Toast.makeText(
//                            requireContext(),
//                            "${errorCode.toString()} -> ${errString.toString()}",
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                }
            )

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Biometric login for my app.")
                .setNegativeButtonText("Use account Password")
                .setConfirmationRequired(false)
                .build()
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun setZoomInZoomOutAnimation() {
        val zoomInFactorX = 1.1f
        val zoomInFactorY = 1.1f
        val zoomOutFactorX = 1f
        val zoomOutFactorY = 1f

        val zoomInAnimation = ScaleAnimation(
            zoomOutFactorX, zoomInFactorX, zoomOutFactorY, zoomInFactorY,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        zoomInAnimation.duration = 10000
        zoomInAnimation.fillAfter = true
        val zoomOutAnimation = ScaleAnimation(
            zoomInFactorX, zoomOutFactorX, zoomInFactorY, zoomOutFactorY,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        zoomOutAnimation.duration = 10000 // Duration in milliseconds
        zoomOutAnimation.fillAfter = true // Keep the result of the animation

        // Apply zoom in and zoom out animations to the layout's background
        binding.viewBackground.startAnimation(zoomInAnimation)

        // Wait for the duration of zoom in animation
        zoomInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                // Zoom out animation
                binding.viewBackground.startAnimation(zoomOutAnimation)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

    }

    private fun setListeners() {
        binding.unlockButton.setOnClickListener {
          //  showProgressDialog(requireContext(),"checking rkjnfinrfjr")
            val pass = binding.passwordEditText.text.toString()
            if (pass.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill password", Toast.LENGTH_SHORT).show()
            } else if (pass == passwordManager.getPassword()) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Wrong PassWord", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forgotPasswordTextView.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment4_to_forgotPasswordFragment)
        }

    }

    private fun showProgressDialog(context: Context?, message: String?) {
        var mProgressDialog: ProgressDialog? = null

        if (context == null) return
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return
        }
        mProgressDialog = ProgressDialog(context)
        try {
            // mProgressDialog.setIcon(R.drawable.paytm_logo);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            mProgressDialog.setMessage(message)
            mProgressDialog.setCancelable(false)
            mProgressDialog.setCanceledOnTouchOutside(false)
            mProgressDialog.show()
        } catch (e: IllegalArgumentException) {

        } catch (e: Exception) {
        }
    }
//    override fun onDestroy() {
//        super.onDestroy()
//        // Remove the callbacks to prevent memory leaks
//        imageView.removeCallbacks(imageChanger)
//    }

}