package com.example.githubactiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sum = addTwoNumbers(2, 3)
        findViewById<TextView>(R.id.total).text = "total" + sum.toString()
    }


    fun addTwoNumbers(a:Int,b:Int) : Int{
        return a+b
    }
}