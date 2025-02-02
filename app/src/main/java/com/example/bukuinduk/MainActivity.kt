package com.example.bukuinduk

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bukuinduk.admin.AngkatanAdminFragment
import com.example.bukuinduk.admin.DashboradAdminFragment
import com.example.bukuinduk.admin.JurusanAdminFragment

import com.example.bukuinduk.databinding.ActivityMainBinding
import com.example.bukuinduk.databinding.LayoutMenuHeaderBinding
import com.example.bukuinduk.models.AdminMe
import com.example.bukuinduk.services.RunRetrofit
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val token = getSharedPreferences("Buku-Induk", MODE_PRIVATE).getString("token", null)

        val listFragment = listOf(
            DashboradAdminFragment.newInstance("Bearir $token"),

            JurusanAdminFragment.newInstance("Bearrir $token"),
            AngkatanAdminFragment.newInstance("Bearrir $token"),
        )
        val listTitle = listOf("Dashboard", "Siswa", "Jurusan", "Angkatan")
        val listIcon = listOf(R.drawable.baseline_home_24, R.drawable.pie_chart, R.drawable.mortarboard, R.drawable.bars)

        binding.menuVp.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int = listFragment.size
            override fun createFragment(position: Int): Fragment = listFragment[position]
        }

        TabLayoutMediator(binding.menuTab, binding.menuVp) { tab, position ->
            if (position == 0) tab.text = listTitle[0]
            tab.icon = getDrawable(listIcon[position])
        }.attach()

        binding.menuTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab!!.text = listTitle[tab.position]
                binding.toolbar.title =if (tab!!.position == 0) listTitle[tab!!.position] else "Data ${listTitle[tab!!.position]}"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.text = null
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            getSharedPreferences("Buku-Induk", MODE_PRIVATE).edit().apply {
                remove("token")
                remove("role")
                clear()
            }.apply()
            startActivity(Intent(this, MainHomePageActivity::class.java))
            finish()
        }
        return true
    }
}