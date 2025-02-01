package com.example.bukuinduk.activity_data_diri

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityPerkembanganBinding
import com.example.bukuinduk.models.Perkembangan
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerkembanganActivity : AppCompatActivity() {
    lateinit var binding: ActivityPerkembanganBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerkembanganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("data_id", 0)
        val userID = intent.getIntExtra("user_id", 0)
        var status = id != 0
        var tanggalmeninggalkansekolah: String? = null
        val adminToken = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

        if (id != 0){
            RunRetrofit().GetOneSiswa("Bearer $adminToken", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.perkembangan as Perkembangan
                        binding.etAlasan.setText(item.meninggalkanSekolahIniAlasan)
                        binding.etTamahanTahun.setText(item.akhirPendidikanTamatBelajarLulusTahun)
                        binding.etTanggalSkhun.setText(item.akhirPendidikanNoTanggalSkhun)
                        binding.etTanggalLahir.text = item.meninggalkanSekolahIniTanggal
                        tanggalmeninggalkansekolah = item.meninggalkanSekolahIniTanggal
                        binding.etNoTanggalIjazah.setText(item.akhirPendidikanNoTanggalIjazah)
                        binding.etSiswaTahunKelasDari.setText(item.menerimaBeasiswaTahunKelasDari)
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) { }
            })
        }

        binding.etTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    binding.etTanggalLahir.text =
                        "${String.format("%02d", dayOfMonth)}-${String.format("%02d", month)}-$year"
                    binding.etTanggalLahir.setTextColor(getColor(R.color.black))
                    tanggalmeninggalkansekolah =
                        "$year-${String.format("%02d", month)}-${String.format("%02d", dayOfMonth)}"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }



        binding.btnPerkembangan.setOnClickListener {
            val data = Perkembangan(
                akhirPendidikanNoTanggalIjazah = binding.etNoTanggalIjazah.text.toString(),
                menerimaBeasiswaTahunKelasDari = binding.etSiswaTahunKelasDari.text.toString(),
                meninggalkanSekolahIniAlasan = binding.etAlasan.text.toString(),
                akhirPendidikanNoTanggalSkhun = binding.etTanggalSkhun.text.toString(),
                meninggalkanSekolahIniTanggal = tanggalmeninggalkansekolah.toString(),
                akhirPendidikanTamatBelajarLulusTahun = binding.etTamahanTahun.text.toString()
            )
            if (!status && !tanggalmeninggalkansekolah.isNullOrEmpty()) RunRetrofit().CreateSiswaPerkembangan(data, userID)
                .enqueue(object : Callback<Perkembangan> {
                    override fun onResponse(
                        call: Call<Perkembangan>,
                        response: Response<Perkembangan>
                    ) {
                        response.body()?.let {
                            if (response.code() in 200..299) {
                                Toast.makeText(
                                    this@PerkembanganActivity,
                                    "Data berhasil di simpan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(Activity.RESULT_OK, Intent())
                                finish()
                            } else Toast.makeText(
                                this@PerkembanganActivity,
                                "Data gagal di simpan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Perkembangan>, t: Throwable) {}
                })
            else if (!tanggalmeninggalkansekolah.isNullOrEmpty()) RunRetrofit().UpdateDatadiri("Bearer $adminToken", UserDetail(perkembangan = data), id)
                .enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        response.body()?.let {
                            if (response.code() in 200..299) {
                                Toast.makeText(
                                    this@PerkembanganActivity,
                                    "Data berhasil di simpan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(Activity.RESULT_OK, Intent())
                                finish()
                            } else Toast.makeText(
                                this@PerkembanganActivity,
                                "Data gagal di simpan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {}
                })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}