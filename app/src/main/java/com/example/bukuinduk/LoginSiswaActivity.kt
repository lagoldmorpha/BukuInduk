package com.example.bukuinduk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginSiswaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_siswa)

        val btnGoToSecondActivity: Button = findViewById(R.id.btn_masuk)
        btnGoToSecondActivity.setOnClickListener {
            // Membuat Intent untuk berpindah ke layout_siswa
            val intent = Intent(this, layout_siswa::class.java) // Pastikan LayoutSiswa adalah nama kelas yang benar
            startActivity(intent)
        }
    }
}
