package com.example.bukuinduk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.databinding.ActivityLoginAdminBinding
import com.example.bukuinduk.databinding.LayoutValidasiBinding
import com.example.bukuinduk.models.AdminCodeResponse
import com.example.bukuinduk.models.AdminLoginRequest
import com.example.bukuinduk.models.AdminLoginResponse
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginAdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKembali.setOnClickListener {
            startActivity(Intent(this, MainHomePageActivity::class.java))
            finish()
        }

        binding.btnMasuk.setOnClickListener {
            if (binding.etEmail.text.isNullOrEmpty()) Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
            else if (binding.etPassword.text.isNullOrEmpty()) Toast.makeText(
                this,
                "Password tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            else {
                val data = AdminLoginRequest(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                )
                RunRetrofit().LoginAdmin(data).enqueue(object : Callback<AdminLoginResponse> {
                    override fun onResponse(
                        call: Call<AdminLoginResponse>,
                        response: Response<AdminLoginResponse>
                    ) {
                        if (response.code() in 200..299) {
                            val builder = AlertDialog.Builder(this@LoginAdminActivity)
                            val alertView =
                                LayoutValidasiBinding.inflate(LayoutInflater.from(this@LoginAdminActivity))

                            builder.setView(alertView.root)
                            builder.create().show()

                            alertView.btnMasuk.setOnClickListener {
                                if (alertView.etKodeOTP.text.isNullOrEmpty()) Toast.makeText(
                                    this@LoginAdminActivity,
                                    "OTP tidak boleh kosong",
                                    Toast.LENGTH_SHORT
                                ).show()
                                else {
                                    RunRetrofit().CodeAdmin(
                                        AdminLoginResponse(
                                            code = alertView.etKodeOTP.text.toString()
                                        )
                                    ).enqueue(object : Callback<AdminCodeResponse> {
                                        override fun onResponse(
                                            call: Call<AdminCodeResponse>,
                                            response: Response<AdminCodeResponse>
                                        ) {
                                            response.body()?.let {
                                                getSharedPreferences(
                                                    "Buku-Induk",
                                                    MODE_PRIVATE
                                                ).edit().apply {
                                                    putString("token", it.token)
                                                    putString("role", "admin")
                                                }.apply()
                                                startActivity(
                                                    Intent(
                                                        this@LoginAdminActivity,
                                                        MainActivity::class.java
                                                    )
                                                )
                                                finish()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<AdminCodeResponse>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                this@LoginAdminActivity,
                                                "OTP Salah",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                                }
                            }
                        } else Toast.makeText(this@LoginAdminActivity, "Email atau Password salah", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<AdminLoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginAdminActivity, "Email atau Password salah", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}