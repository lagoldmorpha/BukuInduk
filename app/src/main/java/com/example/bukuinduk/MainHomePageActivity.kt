package com.example.bukuinduk

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.databinding.ActivityMainHomePageBinding
import com.example.bukuinduk.siswa.DashboardSiswaActivity

class MainHomePageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainHomePageBinding
    private val PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkStoragePermission()

        val dataBase = getSharedPreferences("Buku-Induk", MODE_PRIVATE)
        val token = dataBase.getString("token", null)
        if (token != null) {
            if (dataBase.getString("role", null) == "admin") startActivity(Intent(this, MainActivity::class.java))
            else startActivity(Intent(this, DashboardSiswaActivity::class.java))
            finish()
        }

        binding.btnMaasukAdmin.setOnClickListener {
            startActivity(Intent(this, LoginAdminActivity::class.java))
            finish()
        }

        binding.btnMaasukSiswa.setOnClickListener {
            startActivity(Intent(this, LoginSiswaActivity::class.java))
            finish()
        }
    }
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
        }
    }
}