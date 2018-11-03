package com.aawebdesign.sindikatzdravstva.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.aawebdesign.sindikatzdravstva.R


class ContactActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ContactActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        val websiteLink = findViewById<TextView>(R.id.developed_by_link)
        websiteLink.movementMethod = LinkMovementMethod.getInstance()
        stripUnderlines(websiteLink)
    }

    private fun stripUnderlines(textView: TextView) {
        val spannableString = SpannableString(textView.text)
        for (span in spannableString.getSpans(0, spannableString.length, URLSpan::class.java)) {
            spannableString.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                    tp.color = Color.parseColor("#0B6BBF")
                    tp.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            }, spannableString.getSpanStart(span), spannableString.getSpanEnd(span), 0)
        }
        textView.text = spannableString
    }
}
