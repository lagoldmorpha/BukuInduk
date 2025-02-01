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
import com.example.bukuinduk.databinding.ActivityHobiBinding
import com.example.bukuinduk.models.Hobi
import com.example.bukuinduk.models.SetelahPendidikan
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HobiActivity : AppCompatActivity() {
    lateinit var binding: ActivityHobiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHobiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("data_id", 0)
        val token  =getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null).toString()

        if (id != 0){
            RunRetrofit().GetOneSiswa("Bearer $token", id).enqueue(object : Callback<SiswaByID> {
                override fun onResponse(call: Call<SiswaByID>, response: Response<SiswaByID>) {
                    response.body()?.let {
                        val item = it.hobiSiswa as Hobi
                        binding.etKesenian.setText(item.kesenian)
                        binding.etLainLain.setText(item.lainLain)
                        binding.etOlahraga.setText(item.olahraga)
                        binding.etOrganisasi.setText(item.organisasi)
                    }
                }

                override fun onFailure(call: Call<SiswaByID>, t: Throwable) {}
            })
        }

        binding.btnSimpan.setOnClickListener {
            val data = Hobi(
                kesenian = binding.etKesenian.text.toString(),
                olahraga = binding.etOlahraga.text.toString(),
                organisasi = binding.etOrganisasi.text.toString(),
                userId = id,
                lainLain = binding.etLainLain.text.toString()
            )
            if (id != 0) RunRetrofit().UpdateDatadiri("Bearer $token", UserDetail(hobi = data), id)
                .enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        response.body()?.let {
                            if(response.code() in 200 ..299) {
                                Toast.makeText(
                                    this@HobiActivity,
                                    "Data berhasil di ubah",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(Activity.RESULT_OK, Intent())
                                finish()
                            }
                            else Toast.makeText(this@HobiActivity, "Data gagal di ubah", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {}
                })
            else {
                LoginSiswaActivity.dataRequestCreateSiswa.hobiSiswa = data
                startActivity(Intent(this, AyahKandungActivity::class.java))
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}