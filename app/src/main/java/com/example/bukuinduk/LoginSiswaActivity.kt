package com.example.bukuinduk

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.activity_data_diri.DataDiriActivity
import com.example.bukuinduk.activity_data_diri.PendidikanActivity
import com.example.bukuinduk.databinding.ActivityLoginSiswaBinding
import com.example.bukuinduk.databinding.LayoutItemCreateSiswaBinding
import com.example.bukuinduk.databinding.LayoutItemSingleInputBinding
import com.example.bukuinduk.models.Angkatan
import com.example.bukuinduk.models.Jurusan
import com.example.bukuinduk.models.Siswa
import com.example.bukuinduk.models.SiswaCreate
import com.example.bukuinduk.models.SiswaLoginRequest
import com.example.bukuinduk.models.SiswaLoginResponse
import com.example.bukuinduk.models.SiswaRequest
import com.example.bukuinduk.models.SiswaRequestCreate
import com.example.bukuinduk.services.RunRetrofit
import com.example.bukuinduk.siswa.DashboardSiswaActivity
import request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginSiswaActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        lateinit var dataRequestCreateSiswa: SiswaRequestCreate
    }

    lateinit var binding: ActivityLoginSiswaBinding
    var dataJurusan: List<Jurusan> = emptyList()
    var dataAngkatan: List<Angkatan> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RunRetrofit().GetJurusanSiswa().enqueue(object : Callback<List<Jurusan>> {
            override fun onResponse(call: Call<List<Jurusan>>, response: Response<List<Jurusan>>) {
                response.body()?.let {
                    dataJurusan = it
                }
            }

            override fun onFailure(call: Call<List<Jurusan>>, t: Throwable) {}
        })
        RunRetrofit().GetAngkatanSiswa().enqueue(object : Callback<List<Angkatan>> {
            override fun onResponse(
                call: Call<List<Angkatan>>,
                response: Response<List<Angkatan>>
            ) {
                response.body()?.let {
                    dataAngkatan = it
                }
            }

            override fun onFailure(call: Call<List<Angkatan>>, t: Throwable) {}
        })

        binding.btnKembali.setOnClickListener {
            startActivity(Intent(this, MainHomePageActivity::class.java))
            finish()
        }

        binding.btnCekDataDiri.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            val layoutBuilder = LayoutItemSingleInputBinding.inflate(LayoutInflater.from(this))

            layoutBuilder.title.text = "Masukan NISN"
            layoutBuilder.inputData.hint = "NISN"
            layoutBuilder.buttonSimpan.text = "Cari"

            alertBuilder.setView(layoutBuilder.root)
            val alert = alertBuilder.create()
            layoutBuilder.buttonSimpan.setOnClickListener {
                if (layoutBuilder.inputData.text.isNullOrEmpty()) Toast.makeText(
                    this@LoginSiswaActivity,
                    "Nisn tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    RunRetrofit().LoginSiswa(SiswaLoginRequest(nisn = layoutBuilder.inputData.text.toString()))
                        .enqueue(
                            object : Callback<SiswaLoginResponse> {
                                override fun onResponse(
                                    call: Call<SiswaLoginResponse>,
                                    response: Response<SiswaLoginResponse>
                                ) {
                                    response.body()?.let {
                                        if (response.code() in 200..299) {
                                            startActivity(
                                                Intent(
                                                    this@LoginSiswaActivity,
                                                    DashboardSiswaActivity::class.java
                                                ).apply {
                                                    putExtra("siswa_id", it.id)
                                                })
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@LoginSiswaActivity,
                                                "NISN tidak ditemukan",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    if (response.code() in 400..499) {
                                        alert.dismiss()
                                        Toast.makeText(
                                            this@LoginSiswaActivity,
                                            "NISN tidak di temukan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<SiswaLoginResponse>,
                                    t: Throwable
                                ) { }
                            })
                }
            }
            alert.show()
        }

        binding.btnIsiDataDiri.setOnClickListener {
            dataRequestCreateSiswa = SiswaRequestCreate(null, null, null, null, null, null, null, null, null, null)
            val alert = AlertDialog.Builder(this)
            val layoutBuilder = LayoutItemCreateSiswaBinding.inflate(LayoutInflater.from(this))

            val dataOne = MutableList(dataJurusan.size) { dataJurusan[it].nama }
            dataOne.add(0, "Pilih Jurusan")
            val dataTwo = MutableList(dataAngkatan.size) { dataAngkatan[it].tahun.toString() }
            dataTwo.add(0, "Pilih Angkatan")

            layoutBuilder.spJurusan.adapter =
                ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, dataOne)
            layoutBuilder.spAngkatan.adapter =
                ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, dataTwo)

            alert.setView(layoutBuilder.root)
            val alertDialog = alert.create()

            layoutBuilder.btnSimpan.setOnClickListener {
                if (layoutBuilder.etNisn.text.isNullOrEmpty()) layoutBuilder.dataResponse.text = "Nisn tidak boleh kosong"
                else if (layoutBuilder.spJurusan.selectedItemPosition == 0) layoutBuilder.dataResponse.text = "Pilih Jurusan"
                else if (layoutBuilder.spAngkatan.selectedItemPosition == 0) layoutBuilder.dataResponse.text = "Pilih Angkatan"
                else {
                    dataRequestCreateSiswa.siswa = SiswaCreate(
                        nisn = layoutBuilder.etNisn.text.toString(),
                        jurusanId = dataJurusan[layoutBuilder.spJurusan.selectedItemPosition - 1].id,
                        angkatanId = dataAngkatan[layoutBuilder.spAngkatan.selectedItemPosition - 1].id
                    )
                    startActivity(Intent(this@LoginSiswaActivity, request::class.java))
                    alertDialog.dismiss()
                }
            }

            alertDialog.show()
        }
    }
}