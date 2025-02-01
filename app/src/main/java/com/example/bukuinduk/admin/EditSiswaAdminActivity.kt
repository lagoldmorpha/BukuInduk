package com.example.bukuinduk.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bukuinduk.R
import com.example.bukuinduk.activity_data_diri.AyahKandungActivity
import com.example.bukuinduk.activity_data_diri.DataDiriActivity
import com.example.bukuinduk.activity_data_diri.HobiActivity
import com.example.bukuinduk.activity_data_diri.IbuKandungActivity
import com.example.bukuinduk.activity_data_diri.KesehatanActivity
import com.example.bukuinduk.activity_data_diri.PendidikanActivity
import com.example.bukuinduk.activity_data_diri.PerkembanganActivity
import com.example.bukuinduk.activity_data_diri.SelesaiPendidikanActivity
import com.example.bukuinduk.activity_data_diri.TempatTinggalActivity
import com.example.bukuinduk.activity_data_diri.WaliActivity
import com.example.bukuinduk.databinding.ActivityEditSiswaAdminBinding
import com.example.bukuinduk.models.Perkembangan
import com.example.bukuinduk.models.Siswa
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditSiswaAdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditSiswaAdminBinding
    lateinit var dataSiswa: SiswaByID
    val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) refresh()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSiswaAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        refresh()

        binding.buttonPagePerkembangan.setOnClickListener {
            if (dataSiswa.statusPerkembangan) {
                register.launch(Intent(this, PerkembanganActivity::class.java).apply {
                    putExtra("data_id", dataSiswa.id)
                    putExtra("status", dataSiswa.statusPerkembangan)
                })
            } else {
                register.launch(Intent(this, PerkembanganActivity::class.java).apply {
                    putExtra("user_id", dataSiswa.id)
                    putExtra("status", dataSiswa.statusPerkembangan)
                })
            }
        }

        binding.buttonPageHobi.setOnClickListener {
            register.launch(Intent(this, HobiActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }

        binding.buttonPageTempatTinggal.setOnClickListener {
            register.launch(Intent(this, TempatTinggalActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }

        binding.buttonPageKesehatan.setOnClickListener {
            register.launch(Intent(this, KesehatanActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }

        binding.buttonPageSelesaiPendidikan.setOnClickListener {
            register.launch(Intent(this, SelesaiPendidikanActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }

        binding.buttonPageTentangWali.setOnClickListener {
            register.launch(Intent(this, WaliActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }
        binding.buttonPageAyahKandung.setOnClickListener {
            register.launch(Intent(this, AyahKandungActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }
        binding.buttonPageDataDiri.setOnClickListener {
            register.launch(Intent(this, DataDiriActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }
        binding.buttonPageIbuKandung.setOnClickListener {
            register.launch(Intent(this, IbuKandungActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }
        binding.buttonPagePendidikan.setOnClickListener {
            register.launch(Intent(this, PendidikanActivity::class.java).apply {
                putExtra("data_id", dataSiswa.id)
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    fun refresh() {
        val id= intent.getIntExtra("data_id", 0)
        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

        RunRetrofit().GetOneSiswa("Bearrer $token", id).enqueue(object : Callback<SiswaByID> {
            override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                if (response.code() in 200 .. 299) {
                    response.body()?.let {
                        dataSiswa = it

                        binding.nama.text = "Nama: ${it.dataDiri?.namaLengkap}"
                        binding.nisn.text = "NISN: ${it.nisn}"

                        if (it.dataDiri == null){
                            binding.buttonPageDataDiri.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageTempatTinggal.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageKesehatan.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPagePendidikan.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageIbuKandung.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageAyahKandung.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageTentangWali.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageHobi.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                            binding.buttonPageSelesaiPendidikan.apply {
                                isEnabled = false
                                background = getDrawable(R.drawable.baground_button_secondary)
                            }
                        }

                        if (!it.statusPerkembangan){
                            binding.buttonPagePerkembangan.text = "Isi"
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
        })
    }
}