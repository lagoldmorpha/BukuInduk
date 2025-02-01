package com.example.bukuinduk.activity_data_diri

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.LoginSiswaActivity
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityWaliBinding
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.models.Wali
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WaliActivity : AppCompatActivity() {
    lateinit var binding: ActivityWaliBinding
    var tanggalLahir: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("data_id", 0)
        val teken = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

        binding.etTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                binding.etTanggalLahir.text = "${String.format("%02d", dayOfMonth)}-${String.format("%02d", month)}-$year"
                binding.etTanggalLahir.setTextColor(getColor(R.color.black))
                tanggalLahir = "$year-${String.format("%02d", month)}-${String.format("%02d", dayOfMonth)}"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        if (id != 0) {
            RunRetrofit().GetOneSiswa("Bearerr $teken", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.wali as Wali
                        binding.etTanggalLahir.text = item.tanggalLahir
                        tanggalLahir = item.tanggalLahir
                        binding.etNama.setText(item.nama)
                        binding.etAgama.setText(item.agama)
                        binding.etKewarganegaraan.setText(item.kewarganegaraan)
                        binding.etPendidikan.setText(item.pendidikan)
                        binding.etPekerjaan.setText(item.pekerjaan)
                        binding.etPengeluaranPerBulan.setText(item.pengeluaranPerBulan)
                        binding.etAlamatDanNoTelepon.setText(item.alamatDanNoTelepon)
                        binding.etTempatLahir.setText(item.tempatLahir)
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) { }
            })
        }

        binding.btnSimpan.setOnClickListener {
            val data = Wali(
                nama = binding.etNama.text.toString(),
                tanggalLahir = tanggalLahir.toString(),
                agama = binding.etAgama.text.toString(),
                kewarganegaraan = binding.etKewarganegaraan.text.toString(),
                pendidikan = binding.etPendidikan.text.toString(),
                pekerjaan = binding.etPekerjaan.text.toString(),
                pengeluaranPerBulan = binding.etPengeluaranPerBulan.text.toString(),
                alamatDanNoTelepon = binding.etAlamatDanNoTelepon.text.toString(),
                tempatLahir = binding.etTempatLahir.text.toString(),
            )
                if (id != 0) {
                    RunRetrofit().UpdateDatadiri("Bearer $teken",UserDetail(wali = data), id).enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@WaliActivity, "Data berhasil di ubah", Toast.LENGTH_SHORT).show()
                                setResult(Activity.RESULT_OK, Intent())
                                finish()
                            } else showToast("GAGal mengubah")
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) { }
                    })
                } else {
                    LoginSiswaActivity.dataRequestCreateSiswa.wali = data
                    RunRetrofit().CreateDataDiri(LoginSiswaActivity.dataRequestCreateSiswa).enqueue(
                        object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                Toast.makeText(
                                    this@WaliActivity,
                                    if (response.code() in 200..299) "Data ditri bserhasil di tambahkan" else "data diri gagal di tambahkan",
                                    Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {
                                Toast.makeText(this@WaliActivity, "Data diri gagal di tambahkan", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        })
                }
        }
    }
    private fun isFormValid(): Boolean {
        return when {
            binding.etNama.text.isNullOrEmpty() -> {
                showToast("Nama tidak boleh kosong")
                false
            }
            tanggalLahir.isNullOrEmpty() -> {
                showToast("Tempat lahir tidak boleh kosong")
                false
            }
            binding.etTanggalLahir.text.isNullOrEmpty() -> {
                showToast("Tanggal lahir tidak boleh kosong")
                false
            }
            binding.etAgama.text.isNullOrEmpty() -> {
                showToast("Agama tidak boleh kosong")
                false
            }
            binding.etKewarganegaraan.text.isNullOrEmpty() -> {
                showToast("Kewarganegaraan tidak boleh kosong")
                false
            }
            binding.etPendidikan.text.isNullOrEmpty() -> {
                showToast("Pendidikan tidak boleh kosong")
                false
            }
            binding.etPekerjaan.text.isNullOrEmpty() -> {
                showToast("Pekerjaan tidak boleh kosong")
                false
            }
            binding.etPengeluaranPerBulan.text.isNullOrEmpty() -> {
                showToast("Pengeluaran per bulan tidak boleh kosong")
                false
            }
            binding.etAlamatDanNoTelepon.text.isNullOrEmpty() -> {
                showToast("Alamat dan No Telepon tidak boleh kosong")
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