package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mynotes.databinding.FragmentProfileBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ivShare.setOnClickListener {
            share()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }



    }

    private fun share() {

        val bitmap = getBitmapFromView(binding.cvShare)
        try {
            val file : File = File(requireContext().externalCacheDir,"landscape.png")
            val fout = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout)
            fout.flush()
            fout.close()
            file.setReadable(true,false)

            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file))
            intent.type = "image/png"
            startActivity(Intent.createChooser(intent, "Share Image"))

        } catch (e : FileNotFoundException){

        } catch (e : IOException){

        }

    }

    @SuppressLint("ResourceAsColor")
    private fun getBitmapFromView(view: View) : Bitmap {
        val returnedBitmap : Bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)

        val canvas = Canvas(returnedBitmap)

        val bgDrawable = view.background

        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(R.color.white)
        }
        return returnedBitmap
    }



}