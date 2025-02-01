package com.example.bukuinduk.activity_data_diri

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.LoginSiswaActivity
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityKesehatanBinding
import com.example.bukuinduk.models.Kesehatan
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KesehatanActivity : AppCompatActivity() {
    lateinit var binding: ActivityKesehatanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKesehatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("data_id", 0)

        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)
        if (id != 0) {
            RunRetrofit().GetOneSiswa("Bearer $token", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.kesehatan as Kesehatan
                        binding.etGolDarah.setText(item.golDarah)
                        binding.etKelainan.setText(item.kelainanJasmani)
                        binding.etPenyakit.setText(item.penyakitPernahDiderita)
                        binding.etTinggiBadan.setText(item.tinggi)
                        binding.etBeratBadan.setText(item.beratBadan)
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) { }
            })
        }

        binding.btnSimpan.setOnClickListener {
            if(isFormValid()) {
                val data = Kesehatan(
                    userId = id,
                    tinggi = binding.etTinggiBadan.text.toString(),
                    beratBadan = binding.etBeratBadan.text.toString(),
                    kelainanJasmani = binding.etKelainan.text.toString(),
                    penyakitPernahDiderita = binding.etPenyakit.text.toString(),
                    golDarah = binding.etGolDarah.text.toString()
                )
                if (id != 0) {
                    RunRetrofit().UpdateDatadiri("Baerer $token", UserDetail( kesehatan = data), id).enqueue(
                        object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                if (response.isSuccessful) {
                                    showToast("Data berhasil diubah")
                                    setResult(Activity.RESULT_OK, Intent())
                                    finish()
                                } else showToast("Data gagal diubah")
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) { }
                        })
                } else {
                    LoginSiswaActivity.dataRequestCreateSiswa.kesehatan = data
                    startActivity(Intent(this, PendidikanActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            binding.etGolDarah.text.isNullOrEmpty() -> {
                showToast("Golongan darah tidak boleh kosong")
                false
            }
            binding.etTinggiBadan.text.isNullOrEmpty() -> {
                showToast("Tinggi badan tidak boleh kosong")
                false
            }
            binding.etBeratBadan.text.isNullOrEmpty() -> {
                showToast("Berat badan tidak boleh kosong")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}