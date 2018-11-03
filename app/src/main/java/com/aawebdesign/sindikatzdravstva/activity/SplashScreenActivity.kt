package com.aawebdesign.sindikatzdravstva.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aawebdesign.sindikatzdravstva.R
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        const val DURATION = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        thread(start = true) {
            sleep(DURATION)
            val intent = MainActivity.newIntent(this)
            startActivity(intent)
            finish()
        }
    }

}
