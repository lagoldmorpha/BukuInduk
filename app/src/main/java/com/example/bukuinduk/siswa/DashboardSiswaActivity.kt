package com.example.bukuinduk.siswa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bukuinduk.LoginSiswaActivity
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.ActivityDashboardSiswaBinding
import com.example.bukuinduk.databinding.LayoutItemDashboardSiswaBinding
import com.example.bukuinduk.databinding.LayoutMenuHeaderBinding
import com.example.bukuinduk.models.UserMe
import com.example.bukuinduk.services.RunRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

class DashboardSiswaActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardSiswaBinding
    lateinit var toogle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tollbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toogle = ActionBarDrawerToggle(
            this,
            binding.navDrawer,
            binding.tollbar,
            android.R.string.yes,
            android.R.string.no
        )
        binding.navDrawer.addDrawerListener(toogle)

        toogle.syncState()

        val userID = intent.getIntExtra("siswa_id", 0)
        refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/data-diri/$userID")

        binding.navView.setNavigationItemSelectedListener {
            binding.tollbar.title = it.title.toString()
            when (it.itemId) {
                R.id.menu_logout -> {
                    getSharedPreferences("Buku-Induk", MODE_PRIVATE).edit().apply {
                        remove("token")
                        remove("role")
                    }.apply()
                    startActivity(Intent(this, LoginSiswaActivity::class.java))
                    finish()
                }

                R.id.menu_data_diri -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/data-diri/$userID")
                }

                R.id.menu_tempat_tinggal -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/tempat-tinggal/$userID")
                }

                R.id.menu_kesehatan -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/kesehatan/$userID")
                }

                R.id.menu_pendidikan -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/pendidikan/$userID")
                }

                R.id.menu_ayah_kandung -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/ayah-kandung/$userID")
                }

                R.id.menu_ibu_kandung -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/ibu-kandung/$userID")
                }

                R.id.menu_tentang_wali -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/wali/$userID")
                }

                R.id.menu_hobi -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/hobi/$userID")
                }

                R.id.menu_perkembangan -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/perkembangan/$userID")
                }

                R.id.menu_setelah_pendidikan -> {
                    refresh("https://grown-prepared-boxer.ngrok-free.app/siswa/setelah-pendidikan/$userID")
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        toogle.onOptionsItemSelected(item)
        return true
    }

    fun refresh(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val conn = URL(url).openConnection()
                val data = JSONObject(conn.getInputStream().bufferedReader().readText())

                runOnUiThread {
                    class HolderData(val binding: LayoutItemDashboardSiswaBinding) :
                        RecyclerView.ViewHolder(binding.root)
                    binding.dataAll.adapter = object : RecyclerView.Adapter<HolderData>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): HolderData {
                            return HolderData(
                                LayoutItemDashboardSiswaBinding.inflate(
                                    LayoutInflater.from(
                                        parent.context
                                    ), parent, false
                                )
                            )
                        }

                        override fun getItemCount(): Int = data.length() - 1

                        override fun onBindViewHolder(holder: HolderData, position: Int) {
                            val key = data.names().getString(position + 1)
                            val item = data.getString(key)
                            holder.binding.key.text = key.replace("_", " ").capitalize() + ":"
                            holder.binding.value.text = item
                        }
                    }
                }
            }catch (ex: Exception) {
                runOnUiThread {
                    class HolderData(val binding: LayoutItemDashboardSiswaBinding) :
                        RecyclerView.ViewHolder(binding.root)
                    binding.dataAll.adapter = object : RecyclerView.Adapter<HolderData>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): HolderData {
                            return HolderData(
                                LayoutItemDashboardSiswaBinding.inflate(
                                    LayoutInflater.from(
                                        parent.context
                                    ), parent, false
                                )
                            )
                        }
                        override fun getItemCount(): Int = 1

                        override fun onBindViewHolder(holder: HolderData, position: Int) {
                            val key = "data Masih Kosong"
                            holder.binding.key.text = key
                            holder.binding.value.text = ""
                        }
                    }
                }
            }
        }
    }
}