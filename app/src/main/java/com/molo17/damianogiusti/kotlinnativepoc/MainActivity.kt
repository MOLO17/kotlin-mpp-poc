package com.molo17.damianogiusti.kotlinnativepoc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.molo17.damianogiusti.getMessage

class MainActivity : AppCompatActivity() {

    private val textView by lazy { findViewById<TextView>(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = getMessage()
    }
}
