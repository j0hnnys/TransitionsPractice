package com.johnny.practice.transitionspractice

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.johnny.practice.transitionspractice.R
import com.johnny.practice.transitionspractice.activity.MainActivity
import com.johnny.practice.transitionspractice.fragment.FTransitionActivity
import com.johnny.practice.transitionspractice.pager.PagerActivity
import kotlinx.android.synthetic.main.activity_starting.*

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)

        activityTransitionButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fragmentTransitionButton.setOnClickListener {
            val intent = Intent(this, FTransitionActivity::class.java)
            startActivity(intent)
        }

        pagerTransitionButton.setOnClickListener {
            val intent = Intent(this, PagerActivity::class.java)
            startActivity(intent)
        }
    }
}