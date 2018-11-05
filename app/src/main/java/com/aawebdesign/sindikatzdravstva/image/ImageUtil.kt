package com.aawebdesign.sindikatzdravstva.image

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.aawebdesign.sindikatzdravstva.constants.Constants.Companion.IMAGE_LOCATION
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ImageUtil {

    companion object {
        fun saveImageToInternalStorage(context: Context, bitmap: Bitmap?, imgPath: String?): String {
            val wrapper = ContextWrapper(context)
            var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
            val lastIndexOf = imgPath?.lastIndexOf("/") as Int
            file = File(file, imgPath?.substring(lastIndexOf, imgPath?.length))
            try {
                var stream: OutputStream?
                stream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream!!.flush()
                stream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return file.absolutePath
        }

        fun getImagePath(imgPath: String?): String {
            val lastIndexOf = imgPath?.lastIndexOf("/") as Int
            val fileName = imgPath?.substring(lastIndexOf, imgPath?.length)
            return IMAGE_LOCATION + fileName
        }
    }


}