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
import com.example.bukuinduk.databinding.ActivityDataDiriBinding
import com.example.bukuinduk.models.DataDiri
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataDiriActivity : AppCompatActivity() {
    lateinit var binding :ActivityDataDiriBinding
    val dataKelengkapanOrtu = arrayOf("Kelengkapan ortu", "yatim", "piatu", "yatim piatu", "lengkap")
    val dataJenisKelamin = arrayOf("Jenis Kelamin", "laki-laki", "perempuan")
    var tanggalLahir: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataDiriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.spKelengkapanOrtu.adapter = ArrayAdapter(this,R.layout.layout_custom_spinners, R.id.customSpinnerItem, dataKelengkapanOrtu)
        binding.spJenisKelamin.adapter = ArrayAdapter(this,R.layout.layout_custom_spinners, R.id.customSpinnerItem, dataJenisKelamin)

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

        try {
            val id = intent.getIntExtra("data_id", 0)
            val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

            if (id != 0) {
                RunRetrofit().GetOneSiswa("Bearer $token", id)
                    .enqueue(object : Callback<SiswaByID> {
                        override fun onResponse(
                            call: Call<SiswaByID>,
                            response: Response<SiswaByID>
                        ) {
                            response.body()?.let {
                                val item = it.dataDiri as DataDiri
                                binding.etNamaLengkap.setText(item.namaLengkap)
                                binding.etNamaPanggilan.setText(item.namaPanggilan)
                                binding.spKelengkapanOrtu.setSelection(
                                    dataKelengkapanOrtu.indexOf(
                                        item.kelengkapanOrtu
                                    )
                                )
                                val tanggal = item.tanggalLahir!!.split("-")
                                binding.etTanggalLahir.text = "${tanggal[2]}-${tanggal[1]}-${tanggal[0]}"
                                binding.etTanggalLahir.setTextColor(getColor(R.color.black))
                                binding.etAgama.setText(item.agama)
                                binding.etKewarganegaraan.setText(item.kewarganegaraan)
                                binding.etAnakKe.setText(item.anakKe.toString())
                                binding.etJmlSaudaraKandung.setText(item.jumlahSaudaraKandung.toString())
                                binding.etJmlSaudaraTiri.setText(item.jumlahSaudaraTiri.toString())
                                binding.etJmlSaudaraAngkat.setText(item.jumlahSaudaraAngkat.toString())
                                binding.etBahasaSehariHari.setText(item.bahasaSehariHari)
                                binding.spJenisKelamin.setSelection(dataJenisKelamin.indexOf(item.jenisKelamin))
                            }
                        }

                        override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
                    })
            }

            binding.btnSimpan.setOnClickListener {
                if (isFormValid()) {
                    val data = DataDiri(
                        userId = id,
                        tanggalLahir = tanggalLahir.toString(),
                        namaLengkap = binding.etNamaLengkap.text.toString(),
                        namaPanggilan = binding.etNamaPanggilan.text.toString(),
                        agama = binding.etAgama.text.toString(),
                        kewarganegaraan = binding.etKewarganegaraan.text.toString(),
                        anakKe = binding.etAnakKe.text.toString().toInt(),
                        jumlahSaudaraKandung = binding.etJmlSaudaraKandung.text.toString().toInt(),
                        jumlahSaudaraTiri = binding.etJmlSaudaraTiri.text.toString().toInt(),
                        jumlahSaudaraAngkat = binding.etJmlSaudaraAngkat.text.toString().toInt(),
                        kelengkapanOrtu = dataKelengkapanOrtu[binding.spKelengkapanOrtu.selectedItemPosition],
                        bahasaSehariHari = binding.etBahasaSehariHari.text.toString(),
                        tempatLahir = binding.etTanggalLahir.text.toString(),
                        jenisKelamin = dataJenisKelamin.get(binding.spJenisKelamin.selectedItemPosition)
                    )
                    if (id != 0) {
                        RunRetrofit().UpdateDatadiri(
                            "Bearer $token",
                            UserDetail(dataDiri = data),
                            id
                        ).enqueue(
                            object : Callback<Any> {
                                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                    response.body()?.let {
                                        if (response.code() in 200..299) {
                                            showToast("Data berhasil diupdate")
                                            setResult(Activity.RESULT_OK, Intent())
                                            finish()
                                        } else {
                                            showToast("Data gagal diupdate")
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<Any>, t: Throwable) {}
                            })
                    } else {
                        LoginSiswaActivity.dataRequestCreateSiswa.dataDiri = data
                        startActivity(Intent(this, TempatTinggalActivity::class.java))
                        finish()
                    }
                }
            }
        }catch (ex: Exception) {}
    }

    private fun isFormValid(): Boolean {
        return when {
            binding.etNamaLengkap.text.toString().isNullOrEmpty() -> {
                showToast("Nama Lengkap tidak boleh kosong")
                false
            }
            binding.etNamaPanggilan.text.toString().isNullOrEmpty() -> {
                showToast("Nama Panggilan tidak boleh kosong")
                false
            }
            binding.spKelengkapanOrtu.selectedItemPosition == 0 -> {
                showToast("Kelengkapan Orang Tua tidak boleh kosong")
                false
            }
            binding.etTanggalLahir.text.toString().isNullOrEmpty() -> {
                showToast("Tanggal Lahir tidak boleh kosong")
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
            binding.etAnakKe.text.toString().isNullOrEmpty() -> {
                showToast("Anak Ke tidak boleh kosong")
                false
            }
            binding.etJmlSaudaraKandung.text.toString().isNullOrEmpty() -> {
                showToast("Jumlah Saudara Kandung tidak boleh kosong")
                false
            }
            binding.etJmlSaudaraTiri.text.toString().isNullOrEmpty() -> {
                showToast("Jumlah Saudara Tiri tidak boleh kosong")
                false
            }
            binding.etJmlSaudaraAngkat.text.toString().isNullOrEmpty() -> {
                showToast("Jumlah Saudara Angkat tidak boleh kosong")
                false
            }
            binding.etBahasaSehariHari.text.toString().isNullOrEmpty() -> {
                showToast("Bahasa Sehari-hari tidak boleh kosong")
                false
            }
            binding.spKelengkapanOrtu.selectedItemPosition == 0 -> {
                showToast("Pilih Kelengkapan ortu")
                false
            }
            binding.spJenisKelamin.selectedItemPosition == 0 -> {
                showToast("Pilih Jenis Kelamin")
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