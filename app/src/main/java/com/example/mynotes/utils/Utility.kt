package com.example.mynotes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.itextpdf.text.Document
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter

import java.io.File
import java.io.FileOutputStream

class Utility {

    fun createStyledPdf(context: Context, text: String): File? {
        val fileName = "styled_text_to_pdf.pdf"
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "PDFs")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)

        try {
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Define fonts
            val regularFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12f)
            val boldFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
            val italicFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)

            // Create paragraphs with different styles
            val paragraph1 = Paragraph(text, regularFont)
            val paragraph2 = Paragraph("This is bold text.", boldFont)
            val paragraph3 = Paragraph("This is italic text.", italicFont)

            // Add paragraphs to the document
            document.add(paragraph1)
            document.add(paragraph2)
            document.add(paragraph3)

            document.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun openPdfFileUsingUri(context: Context, fileUri: Uri) {
        val filePath = "/storage/emulated/0/Android/data/com.example.mynotes/files/Documents/PDFs/styled_text_to_pdf.pdf"
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse("file://$filePath")
        intent.setDataAndType(uri, "application/pdf")
        context.startActivity(intent)

//        if (intent.resolveActivity(packageManager) != null) {
//            context.startActivity(intent)
//        } else {
//           Toast.makeText(context,"Some thing went wrong",Toast.LENGTH_SHORT).show()
//        }
    }

    fun openPDF(context: Context, file: File) {
//        try {
//            val localUri = FileProvider.getUriForFile(context, context.applicationContext.packageName, file)
//            val mIntent = Intent(Intent.ACTION_VIEW)
//            mIntent.setDataAndType(localUri, "application/pdf")
//            mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            context.startActivity(mIntent)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    fun openPdfFileUsingFilePath(context: Context, filePath: File) {
    //    val file = File("file://$filePath")
    //    val fileUri = FileProvider.getUriForFile(context, "com.example.mynotes.fileprovider", filePath)

        try {
            val pdf = File(context.getExternalFilesDir("mypdfs"),"styled_text_to_pdf.pdf")
            val testPdfUri = FileProvider.getUriForFile(context.applicationContext,"com.example.mynotes.fileprovider",pdf)
            val pdfFilePath = "/storage/emulated/0/Android/data/com.example.mynotes/files/Documents/PDFs/styled_text_to_pdf.pdf"
            val pdfUri = Uri.parse(pdfFilePath)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(testPdfUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
//            if (intent.resolveActivity(context.packageManager) != null) {
//                context.startActivity(intent)
//            } else {
//                Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
//            }
        }catch (e:Exception){
            Log.e("Utility", "openPdfFileUsingFilePath: ${e.toString()} ", )
        }


    }

    fun showDialog(context: Context){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Do you want to delete the items")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Yes"){_,_->

        }
        alertDialog.setNegativeButton("No"){_,_->
        }
        alertDialog.show()
    }
}