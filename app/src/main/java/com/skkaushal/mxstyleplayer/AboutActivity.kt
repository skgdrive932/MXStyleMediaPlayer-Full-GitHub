package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val btnEmail = findViewById<Button>(R.id.btnEmail)
        val btnCall = findViewById<Button>(R.id.btnCall)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)

        // Email Send Intent
        val sendEmailIntent = {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:skgdrive932@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, "MX Style Media Player Feedback")
            }
            startActivity(intent)
        }

        btnEmail.setOnClickListener { sendEmailIntent() }
        tvEmail.setOnClickListener { sendEmailIntent() }

        // Call Intent
        val makeCallIntent = {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:+919779371866")
            }
            startActivity(intent)
        }

        btnCall.setOnClickListener { makeCallIntent() }
        tvPhone.setOnClickListener { makeCallIntent() }
    }
}
