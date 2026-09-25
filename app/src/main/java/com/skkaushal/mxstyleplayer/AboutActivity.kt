package com.skkaushal.mxstyleplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val btnGithub = findViewById<Button>(R.id.btnGithub)

        // GitHub Profile Open Karne Ka Functionality
        btnGithub.setOnClickListener {
            val githubUrl = "https://github.com/skkaushal" // Apni URL yahan update kar sakte ho
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }
    }
}
