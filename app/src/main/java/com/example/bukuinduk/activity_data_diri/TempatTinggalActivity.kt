package com.example.bukuinduk.activity_data_diri

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.LoginSiswaActivity
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityTempatTinggalBinding
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.TempatTinggal
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TempatTinggalActivity : AppCompatActivity() {
    lateinit var binding: ActivityTempatTinggalBinding
    val dataTinggalDengan = listOf("Tinggal dengan", "ortu", "saudara", "lainnya","wali",)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTempatTinggalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("data_id", 0)
        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

        if (id != 0) {
            RunRetrofit().GetOneSiswa("Bearrer $token", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        binding.etAlamat.setText(it.tempatTinggal?.alamat)
                        binding.tinggalDengan.setSelection(dataTinggalDengan.indexOf(it.tempatTinggal?.tinggalDengan))
                        binding.etJarakKeSekolah.setText(it.tempatTinggal?.jarakKeSekolah)
                        binding.etTelepon.setText(it.tempatTinggal?.noTelepon)
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
            })
        }

        binding.tinggalDengan.adapter = ArrayAdapter(
            this,
            R.layout.layout_custom_spinners,
            R.id.customSpinnerItem,
            dataTinggalDengan
        )

        binding.btnSimpan.setOnClickListener {
            if (binding.etAlamat.text.isNullOrEmpty()) Toast.makeText(
                this,
                "Alamat tidak boleh kosong",
                Toast.LENGTH_SHORT
            )
                .show()
            else if (binding.etTelepon.text.isNullOrEmpty()) Toast.makeText(
                this,
                "Nomor telepon tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            else if (binding.tinggalDengan.selectedItemPosition == 0) Toast.makeText(
                this,
                "Pilih Tinggal dengan siapa terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            else if (binding.etJarakKeSekolah.text.isNullOrEmpty()) Toast.makeText(
                this,
                "Jarak ke sekolah tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            else {
                val data = TempatTinggal(
                    userId = id,
                    tinggalDengan = dataTinggalDengan[binding.tinggalDengan.selectedItemPosition],
                    alamat = binding.etAlamat.text.toString(),
                    jarakKeSekolah = binding.etJarakKeSekolah.text.toString(),
                    noTelepon = binding.etTelepon.text.toString()
                )
                if (id != 0) {
                    RunRetrofit().UpdateDatadiri(
                        "Bearer $token",
                        UserDetail(tempatTinggal = data),
                        id
                    )
                        .enqueue(object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                response.body()?.let {
                                    if (response.code() in 200..299) {
                                        Toast.makeText(
                                            this@TempatTinggalActivity,
                                            "Data berhasil di ubah",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        setResult(Activity.RESULT_OK, Intent())
                                        finish()
                                    } else Toast.makeText(
                                        this@TempatTinggalActivity,
                                        "Data gagal di ubah",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {}
                        })
                } else {
                    LoginSiswaActivity.dataRequestCreateSiswa.tempatTinggal = data
                    startActivity(Intent(this, KesehatanActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}