package com.example.bukuinduk.activity_data_diri

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.LoginSiswaActivity
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityPendidikanBinding
import com.example.bukuinduk.models.DataDiri
import com.example.bukuinduk.models.DataPendidikan
import com.example.bukuinduk.models.Pendidikan
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendidikanActivity : AppCompatActivity() {
    lateinit var binding: ActivityPendidikanBinding
    val kelasOptions = arrayOf("Pilih Kelas", "10", "11", "12")
    var diterimaTanggal: String? = null
    var bidangKeAhlian:String? = null
    var progamKeAhlian:String? = null
    var paketKeAhlian:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendidikanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.spDiterimaDiKelas.adapter = ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, kelasOptions)

        val id = intent.getIntExtra("data_id", 0)
        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

        val dataBidangKeahlian = MutableList(DataPendidikan.data.size) { DataPendidikan.data[it].bidangKeahlian }
        dataBidangKeahlian.add(0, "Bidang Ke Ahlian")

        binding.etDiterimaDiBidangKeahlian.adapter = ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, dataBidangKeahlian)
        binding.etDiterimaDiProgramKeahlian.adapter = ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, listOf("Progam Ke Ahlian"))
        binding.etDiterimaDiPaketKeahlian.adapter = ArrayAdapter(this, R.layout.layout_custom_spinners, R.id.customSpinnerItem, listOf("Paket Ke Ahlian"))

        binding.etDiterimaDiBidangKeahlian.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    binding.etDiterimaDiProgramKeahlian.adapter = ArrayAdapter(
                        this@PendidikanActivity,
                        R.layout.layout_custom_spinners,
                        R.id.customSpinnerItem,
                        MutableList(DataPendidikan.data[position - 1].programKeahlian.size) {
                            DataPendidikan.data[position - 1].programKeahlian[it].namaProgram
                        }.apply {
                            add(0, "Progam Ke Ahlian")
                        })
                    bidangKeAhlian = DataPendidikan.data[position - 1].bidangKeahlian

                    binding.etDiterimaDiProgramKeahlian.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position != 0) {
                                    progamKeAhlian = DataPendidikan.data[binding.etDiterimaDiBidangKeahlian.selectedItemPosition - 1]
                                        .programKeahlian[binding.etDiterimaDiProgramKeahlian.selectedItemPosition -1].namaProgram

                                    binding.etDiterimaDiPaketKeahlian.adapter = ArrayAdapter(this@PendidikanActivity, R.layout.layout_custom_spinners, R.id.customSpinnerItem,
                                        MutableList(
                                            DataPendidikan.data[binding.etDiterimaDiBidangKeahlian.selectedItemPosition - 1]
                                                .programKeahlian[binding.etDiterimaDiProgramKeahlian.selectedItemPosition -1].paketKeahlian.size
                                        ) {DataPendidikan.data[binding.etDiterimaDiBidangKeahlian.selectedItemPosition - 1]
                                            .programKeahlian[binding.etDiterimaDiProgramKeahlian.selectedItemPosition -1].paketKeahlian[it]
                                        }.apply { add(0, "Paket Keahlian") })
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) { }
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }


        binding.etDiterimaDiTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    binding.etDiterimaDiTanggal.text =
                        "${String.format("%02d", dayOfMonth)}-${String.format("%02d", month)}-$year"
                    binding.etDiterimaDiTanggal.setTextColor(getColor(R.color.black))
                    diterimaTanggal =
                        "$year-${String.format("%02d", month)}-${String.format("%02d", dayOfMonth)}"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        if (id != 0) {
            RunRetrofit().GetOneSiswa("Bearer $token", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.pendidikan as Pendidikan
                        val tanggal_diterima = item.diterimaTanggal!!.split("-")
                        binding.etSebelumnyaTamatanDari.setText(item.sebelumnyaTamatanDari)
                        binding.etSebelumnyaTanggalDanIjazah.setText(item.sebelumnyaTanggalDanIjazah)
                        binding.etSebelumnyaTanggalSkhunDan.setText(item.sebelumnyaTanggalSkhunDanIjazah)
                        binding.etSebelumnyaLamaBelajar.setText(item.sebelumnyaLamaBelajar)
                        binding.spDiterimaDiKelas.setSelection(kelasOptions.indexOf(item.diterimaDiKelas.toString()))
                        binding.etDiterimaDiTanggal.text = "${tanggal_diterima[2]}-${tanggal_diterima[1]}-${tanggal_diterima[0]}"
                        binding.etPindahanDariSekolah.setText(item.pindahanDariSekolah)
                        binding.etPindahanAlasan.setText(item.pindahanAlasan)
                        diterimaTanggal = item.diterimaTanggal
                        binding.etDiterimaDiTanggal.setTextColor(getColor(R.color.black))

                        val bidangKeahlianPosition = dataBidangKeahlian.indexOf(item.diterimaDiBidangKeahlian)
                        val progamKeahlianPosition = DataPendidikan.data[bidangKeahlianPosition - 1].programKeahlian.indexOfFirst { it.namaProgram == item.diterimaDiProgramKeahlian } + 1
                        val data1 = MutableList(DataPendidikan.data[bidangKeahlianPosition - 1].programKeahlian.size) { DataPendidikan.data[bidangKeahlianPosition - 1].programKeahlian[it].namaProgram }.apply {
                            add(0,"Progam Ke Ahlian")
                        }
                        val data2 = MutableList(DataPendidikan.data[bidangKeahlianPosition - 1].programKeahlian[progamKeahlianPosition - 1].paketKeahlian.size) { DataPendidikan.data[bidangKeahlianPosition - 1].programKeahlian[progamKeahlianPosition - 1].paketKeahlian[it] }.apply {
                            add(0, "Paket ke ahlian")
                        }
                        binding.etDiterimaDiBidangKeahlian.setSelection(bidangKeahlianPosition)
                        binding.etDiterimaDiProgramKeahlian.adapter = ArrayAdapter(this@PendidikanActivity, R.layout.layout_custom_spinners, R.id.customSpinnerItem, data1)
                        binding.etDiterimaDiPaketKeahlian.adapter = ArrayAdapter(this@PendidikanActivity, R.layout.layout_custom_spinners, R.id.customSpinnerItem, data2)

                        binding.etDiterimaDiPaketKeahlian.adapter.let {
                            binding.etDiterimaDiProgramKeahlian.setSelection(progamKeahlianPosition)
                        }
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
            })
        }

        binding.btnSimpan.setOnClickListener {
            if (isFormValid()) {
                val data = Pendidikan(
                    userId = id,
                    sebelumnyaLamaBelajar = binding.etSebelumnyaLamaBelajar.text.toString(),
                    sebelumnyaTamatanDari = binding.etSebelumnyaTamatanDari.text.toString(),
                    sebelumnyaTanggalDanIjazah = binding.etSebelumnyaTanggalDanIjazah.text.toString(),
                    sebelumnyaTanggalSkhunDanIjazah = binding.etSebelumnyaTanggalSkhunDan.text.toString(),
                    diterimaDiKelas = kelasOptions.get(binding.spDiterimaDiKelas.selectedItemPosition).toInt(),
                    diterimaDiBidangKeahlian = bidangKeAhlian,
                    diterimaDiProgramKeahlian = progamKeAhlian,
                    diterimaDiPaketKeahlian = paketKeAhlian,
                    diterimaTanggal = diterimaTanggal.toString(),
                    pindahanDariSekolah =  binding.etPindahanDariSekolah.text.toString(),
                    pindahanAlasan = binding.etPindahanAlasan.text.toString()
                )
                if (id != 0) {
                    RunRetrofit().UpdateDatadiri("Bearer $token", UserDetail(pendidikan = data), id).enqueue(
                        object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                response.body()?.let {
                                    if(response.code() in 200..299) {
                                        showToast("Data Berhasil Diubah")
                                        setResult(Activity.RESULT_OK, Intent())
                                        finish()
                                    } else {
                                        showToast("Data Gagal Diubah")
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {}
                        })
                } else {
                    LoginSiswaActivity.dataRequestCreateSiswa.pendidikan = data
                    startActivity(Intent(this, HobiActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        if (binding.etDiterimaDiPaketKeahlian.selectedItemPosition != 0) paketKeAhlian = DataPendidikan.data[binding.etDiterimaDiBidangKeahlian.selectedItemPosition - 1]
            .programKeahlian[binding.etDiterimaDiProgramKeahlian.selectedItemPosition -1].paketKeahlian[binding.etDiterimaDiPaketKeahlian.selectedItemPosition - 1]
        return when {
            binding.etSebelumnyaTamatanDari.text.toString().isEmpty() -> {
                showToast("Sebelumnya Tamatan Dari tidak boleh kosong")
                false
            }
            binding.etSebelumnyaTanggalDanIjazah.text.toString().isEmpty() -> {
                showToast("Sebelumnya Tanggal dan Ijazah tidak boleh kosong")
                false
            }
            binding.etSebelumnyaTanggalSkhunDan.text.toString().isEmpty() -> {
                showToast("Sebelumnya Tanggal SKHUN dan tidak boleh kosong")
                false
            }
            binding.etSebelumnyaLamaBelajar.text.toString().isEmpty() -> {
                showToast("Sebelumnya Lama Belajar tidak boleh kosong")
                false
            }
            binding.spDiterimaDiKelas.selectedItemPosition == 0 -> {
                showToast("Kelas tidak boleh kosong")
                false
            }
            binding.etDiterimaDiBidangKeahlian.selectedItemPosition == 0 -> {
                showToast("Diterima di Bidang Keahlian tidak boleh kosong")
                false
            }
            binding.etDiterimaDiProgramKeahlian.selectedItemPosition == 0 -> {
                showToast("Diterima di Program Keahlian tidak boleh kosong")
                false
            }
            binding.etDiterimaDiPaketKeahlian.selectedItemPosition == 0 -> {
                showToast("Diterima di Paket Keahlian tidak boleh kosong")
                false
            }
            diterimaTanggal.isNullOrEmpty() -> {
                showToast("Diterima tanggal tidak boleh kosong")
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