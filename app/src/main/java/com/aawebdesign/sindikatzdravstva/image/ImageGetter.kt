package com.aawebdesign.sindikatzdravstva.image

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.WindowManager
import com.aawebdesign.sindikatzdravstva.R
import com.aawebdesign.sindikatzdravstva.db.ImageDBHandler

class ImageGetter(val context: Context) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        val imageHandler = ImageDBHandler(context, null, null, 1)
        var image = imageHandler.findImage(source)
        return return if (image != null) {
            val drawable = Drawable.createFromPath(image.internalLocation) as Drawable
            val widthD = getWidth()
            drawable.setBounds(0, 0, widthD, (widthD * drawable.intrinsicHeight / drawable.intrinsicWidth))
            drawable
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.resources.getDrawable(R.drawable.blank_square, null)
            } else {
                context.resources.getDrawable(R.drawable.blank_square)
            }
        }
    }

    private fun getWidth(): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        return size.x
    }

}