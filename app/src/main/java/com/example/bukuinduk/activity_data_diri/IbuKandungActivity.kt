package com.example.bukuinduk.activity_data_diri

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.LoginSiswaActivity
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityIbuKandungBinding
import com.example.bukuinduk.models.IbuKandung
import com.example.bukuinduk.models.Siswa
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IbuKandungActivity : AppCompatActivity() {
    lateinit var binding: ActivityIbuKandungBinding
    val dataStatus = listOf("Status", "masih hidup", "meninggal")
    var tanggalLahir: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIbuKandungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        val id = intent.getIntExtra("data_id", 0)
        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)
        if (id != 0) {
            RunRetrofit().GetOneSiswa("Bearer $token", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.ibuKandung as IbuKandung
                        binding.etNama.setText(item.nama)
                        val tanggal = item.tanggalLahir!!.split("-")
                        binding.etTanggalLahir.text = "${tanggal[2]}-${tanggal[1]}-${tanggal[0]}"
                        binding.etTanggalLahir.setTextColor(getColor(R.color.black))
                        binding.etTanggalLahir.text = item.tanggalLahir
                        tanggalLahir = item.tanggalLahir
                        binding.etAgama.setText(item.agama)
                        binding.etKewarganegaraan.setText(item.kewarganegaraan)
                        binding.etPendidikan.setText(item.pendidikan)
                        binding.etPekerjaan.setText(item.pekerjaan)
                        binding.etPengeluaranPerBulan.setText(item.pengeluaranPerBulan)
                        binding.etAlamatDanNoTelepon.setText(item.alamatDanNoTelepon)
                        binding.spStatus.setSelection(dataStatus.indexOf(item.status))
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
            })
        }

        binding.spStatus.adapter =
            ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, dataStatus)

        binding.btnSimpan.setOnClickListener {
            if (isFormValid()) {
                val data = IbuKandung(
                    userId = id,
                    nama = binding.etNama.text.toString(),
                    tempatLahir = binding.etTempatLahir.text.toString(),
                    pengeluaranPerBulan = binding.etPengeluaranPerBulan.text.toString(),
                    alamatDanNoTelepon = binding.etAlamatDanNoTelepon.text.toString(),
                    tanggalLahir = tanggalLahir.toString(),
                    agama = binding.etAgama.text.toString(),
                    kewarganegaraan = binding.etKewarganegaraan.text.toString(),
                    pekerjaan = binding.etKewarganegaraan.text.toString(),
                    pendidikan = binding.etPendidikan.text.toString(),
                    status = dataStatus.get(binding.spStatus.selectedItemPosition)
                )
                if (id != 0) {
                    RunRetrofit().UpdateDatadiri("Bearerr $token", UserDetail( ibuKandung = data), id).enqueue(
                        object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                response.body()?.let {
                                    if (response.code() in 200..299) {
                                        showToast("data berhasil diupdate")
                                        setResult(Activity.RESULT_OK, Intent())
                                        finish()
                                    } else showToast("data gagal di update")
                                }
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) { }
                        })
                } else {
                    LoginSiswaActivity.dataRequestCreateSiswa.ibuKandung = data
                    startActivity(Intent(this, WaliActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            binding.etNama.text.toString().isNullOrEmpty() -> {
                showToast("Nama tidak boleh kosong")
                false
            }

            binding.etTempatLahir.text.toString().isNullOrEmpty() -> {
                showToast("Tempat lahir tidak boleh kosong")
                false
            }

            tanggalLahir.isNullOrEmpty() -> {
                showToast("Tanggal lahir tidak boleh kosong")
                false
            }

            binding.etAgama.text.toString().isNullOrEmpty() -> {
                showToast("Agama tidak boleh kosong")
                false
            }

            binding.etKewarganegaraan.text.toString().isNullOrEmpty() -> {
                showToast("Kewarganegaraan tidak boleh kosong")
                false
            }

            binding.etPendidikan.text.toString().isNullOrEmpty() -> {
                showToast("Pendidikan tidak boleh kosong")
                false
            }

            binding.etPekerjaan.text.toString().isNullOrEmpty() -> {
                showToast("Pekerjaan tidak boleh kosong")
                false
            }

            binding.etPengeluaranPerBulan.text.toString().isNullOrEmpty() -> {
                showToast("Pengeluaran per bulan tidak boleh kosong")
                false
            }

            binding.etAlamatDanNoTelepon.text.toString().isNullOrEmpty() -> {
                showToast("Alamat dan No Telepon tidak boleh kosong")
                false
            }

            binding.spStatus.selectedItemPosition == 0 -> {
                showToast("Status tidak boleh kosong")
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