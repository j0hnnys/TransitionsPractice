package com.johnny.practice.transitionspractice

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class Test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val async = object: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }
}