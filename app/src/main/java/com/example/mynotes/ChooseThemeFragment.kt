package com.example.mynotes

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mynotes.adapter.ThemeAdapter
import com.example.mynotes.databinding.FragmentChooseThemeBinding
import com.example.mynotes.model.Theme
import com.example.mynotes.utils.PasswordManager
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlin.math.abs


class ChooseThemeFragment : Fragment() {

    lateinit var binding:FragmentChooseThemeBinding
    private var pageSelected : Int = 0
    lateinit var passwordManager: PasswordManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChooseThemeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.S){
            binding.viewImage.setRenderEffect(RenderEffect.createBlurEffect(50f,50f,Shader.TileMode.MIRROR))
        }else{
            binding.viewImage.alpha = 0.5f
        }

        passwordManager = PasswordManager(requireContext())

        val list : MutableList<Theme> = mutableListOf()
        list.add(Theme(R.drawable.theme_landscape1))
        list.add(Theme(R.drawable.theme_landscape2))
        list.add(Theme(R.drawable.theme_landscape3))
        list.add(Theme(R.drawable.theme_landscape4))
        list.add(Theme(R.drawable.theme_landscape5))

        binding.vpImageSlider.adapter = ThemeAdapter(list,binding.vpImageSlider)

        binding.vpImageSlider.clipToPadding = false
        binding.vpImageSlider.clipChildren = false
        binding.vpImageSlider.offscreenPageLimit = 3
        binding.vpImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER



        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(100))

        compositePageTransformer.addTransformer(ZoomOutPageTransformer())
        binding.vpImageSlider.setPageTransformer(compositePageTransformer)

        binding.vpImageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //Toast.makeText(requireContext(),"Current page index: $position",Toast.LENGTH_SHORT).show()
                pageSelected = position
                setBackgroundTheme()

            }
        })




        binding.btnUseIt.setOnClickListener {
            if(passwordManager.isAutoThemeOn()){
                Toast.makeText(requireContext(), "Please off Auto Theme", Toast.LENGTH_SHORT)
                    .show()
                val alertDialog = AlertDialog.Builder(requireActivity())
                alertDialog.setTitle("Auto theme")
                alertDialog.setMessage("Do you want to off the Auto Theme")
                alertDialog.setCancelable(false)
                alertDialog.setPositiveButton("Yes"){_,_->
                    passwordManager.setAutoThemeOn(false)
                    passwordManager.saveTheme(pageSelected)
                }
                alertDialog.setNegativeButton("No"){_,_->
                }
                alertDialog.show()
            }else {
                Toast.makeText(requireContext(), "Theme selected successfully", Toast.LENGTH_SHORT)
                    .show()
                passwordManager.saveTheme(pageSelected)
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }



    }

    private fun setBackgroundTheme() {
        when(pageSelected){
            0 -> binding.viewImage.setBackgroundResource(R.drawable.theme_landscape1)
            1 -> binding.viewImage.setBackgroundResource(R.drawable.theme_landscape2)
            2 -> binding.viewImage.setBackgroundResource(R.drawable.theme_landscape3)
            3 -> binding.viewImage.setBackgroundResource(R.drawable.theme_landscape4)
            4 -> binding.viewImage.setBackgroundResource(R.drawable.theme_landscape5)
        }
    }

}

class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        var r = 1- abs(position)
        page.scaleY = (0.85f + r * 0.15f)
    }
}