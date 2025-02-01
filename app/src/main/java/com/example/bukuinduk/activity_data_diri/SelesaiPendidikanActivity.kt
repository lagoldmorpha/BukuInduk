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
import com.example.bukuinduk.databinding.ActivitySelesaiPendidikanBinding
import com.example.bukuinduk.models.SetelahPendidikan
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.SiswaRequest
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelesaiPendidikanActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelesaiPendidikanBinding
    var tanggalLahir: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelesaiPendidikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("data_id", 0)

        binding.etTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    binding.etTanggalLahir.text =
                        "${String.format("%02d", dayOfMonth)}-${String.format("%02d", month)}-$year"
                    binding.etTanggalLahir.setTextColor(getColor(R.color.black))
                    tanggalLahir =
                        "$year-${String.format("%02d", month)}-${String.format("%02d", dayOfMonth)}"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)
        if (id != 0) {
            RunRetrofit().GetOneSiswa("Bearerr $token", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.setelahPendidikan as SetelahPendidikan
                        val tanggalMulai = item.bekerjaTanggalMulai!!.split("-")
                        binding.etMelanjutkanKe.setText(item.melanjutkanKe)
                        binding.etBekerjaNamaPerusahaan.setText(item.bekerjaNamaPerusahaan)
                        binding.etBekerjaPenghasilan.setText(item.bekerjaPenghasilan)
                        binding.etTanggalLahir.text = "${tanggalMulai[2]}-${tanggalMulai[1]}-${tanggalMulai[0]}"
                        tanggalLahir = item.bekerjaTanggalMulai
                        binding.etTanggalLahir.setTextColor(getColor(R.color.black))
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
            })
        }

        binding.btnSimpan.setOnClickListener {
            val data = SetelahPendidikan(
                melanjutkanKe = binding.etMelanjutkanKe.text.toString(),
                userId = id,
                bekerjaNamaPerusahaan = binding.etBekerjaNamaPerusahaan.text.toString(),
                bekerjaTanggalMulai = tanggalLahir.toString(),
                bekerjaPenghasilan = binding.etBekerjaPenghasilan.text.toString(),
            )
            if (id != 0) {
                RunRetrofit().UpdateDatadiri(
                    "Bearer $token",
                    UserDetail(setelahPendidikan = data),
                    id
                )
                    .enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            response.body()?.let {
                                if (response.code() in 200..299) {
                                    Toast.makeText(
                                        this@SelesaiPendidikanActivity,
                                        "Data berhasil di simpan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    setResult(Activity.RESULT_OK, Intent())
                                    finish()
                                } else Toast.makeText(
                                    this@SelesaiPendidikanActivity,
                                    "Data gagal di simpan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {}
                    })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}