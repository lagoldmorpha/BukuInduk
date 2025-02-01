package com.example.bukuinduk.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.FragmentSiswaAdminBinding
import com.example.bukuinduk.databinding.LayoutItemSiswaBinding
import com.example.bukuinduk.models.Angkatan
import com.example.bukuinduk.models.Jurusan
import com.example.bukuinduk.models.Siswa
import com.example.bukuinduk.models.SiswaDeleteResponse
import com.example.bukuinduk.services.RunRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ARG_PARAM1 = "token"

class SiswaAdminFragment : Fragment() {
    private var token: String? = null
    lateinit var binding: FragmentSiswaAdminBinding
    var jurusan: String? = null
    var angkatan: String? = null

    val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) refresh()
    }

    private lateinit var mContext: Context // Variabel untuk menyimpan referensi konteks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context // Menyimpan konteks saat fragment melekat pada aktivitas
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSiswaAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dataJurusan: List<Jurusan> = emptyList()
        var dataAngkatan: List<Angkatan> = emptyList()

        RunRetrofit().GetJurusan(token.toString()).enqueue(object : Callback<List<Jurusan>> {
            override fun onResponse(call: Call<List<Jurusan>>, response: Response<List<Jurusan>>) {
                response.body()?.let { listJurusan ->
                    dataJurusan = listJurusan
                    val data = MutableList(listJurusan.size) { listJurusan[it].nama }
                    data.add(0, "Jurusan")
                    binding.spJurusan.adapter =
                        ArrayAdapter(
                            view.context,
                            R.layout.layout_custom_spinners,
                            R.id.customSpinnerItem,
                            data
                        )
                }
            }

            override fun onFailure(call: Call<List<Jurusan>>, t: Throwable) {
            }
        })

        RunRetrofit().GetAngkatn(token.toString()).enqueue(object : Callback<List<Angkatan>> {
            override fun onResponse(
                call: Call<List<Angkatan>>,
                response: Response<List<Angkatan>>
            ) {
                response.body()?.let { listAngkatan ->
                    dataAngkatan = listAngkatan
                    val data = MutableList(listAngkatan.size) { listAngkatan[it].tahun.toString() }
                    data.add(0, "Angkatan")
                    binding.tahun.adapter =
                        ArrayAdapter(
                            view.context,
                            R.layout.layout_custom_spinners,
                            R.id.customSpinnerItem,
                            data
                        )
                }
            }

            override fun onFailure(call: Call<List<Angkatan>>, t: Throwable) {}
        })

        refresh()

        binding.spJurusan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) jurusan = dataJurusan[position - 1].nama
                else jurusan = null

                refresh()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.tahun.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) angkatan = dataAngkatan[position - 1].tahun.toString()
                else angkatan = null

                refresh()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.search.addTextChangedListener {
            if (binding.search.text.length <= 3) refresh()
        }

        binding.btnSave.setOnClickListener {
            RunRetrofit().exportExcel(token.toString(), jurusan, angkatan,if (binding.search.text.isNullOrEmpty()) null else binding.search.text.toString()).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    response.body()?.let {
                        saveExcelFile(it)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })
        }
    }
    private fun saveExcelFile(body: ResponseBody) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Ganti nama file sesuai kebutuhan
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val fileName = "${if (jurusan.isNullOrEmpty()) "Semua" else jurusan} - ${if (angkatan.isNullOrEmpty()) "Semua Angkatan" else angkatan}-$time.xlsx"

                val file = File(mContext.getExternalFilesDir(null), fileName)
                file.outputStream().use { output ->
                    body.byteStream().use { input ->
                        input.copyTo(output)
                    }
                }
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(mContext, "Data berhasil di simpan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(mContext, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun savePdfFile(body: ResponseBody, nama: String) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val fileName = "$nama - $time.pdf"

                val file = File(context?.getExternalFilesDir(null), fileName)
                file.outputStream().use { output ->
                    body.byteStream().use { input ->
                        input.copyTo(output)
                    }
                }

                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Data berhasil di simpan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun refresh() {
        RunRetrofit().GetAllSiswa(token.toString(), jurusan, angkatan, binding.search.text.toString())
            .enqueue(object : Callback<List<Siswa>> {
                override fun onResponse(call: Call<List<Siswa>>, response: Response<List<Siswa>>) {
                    response.body()?.let { listSiswa ->
                        class HolderSiswa(val binding: LayoutItemSiswaBinding) :
                            RecyclerView.ViewHolder(binding.root)
                        binding.dataSiswa.adapter = object : RecyclerView.Adapter<HolderSiswa>() {
                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): HolderSiswa {
                                return HolderSiswa(
                                    LayoutItemSiswaBinding.inflate(
                                        LayoutInflater.from(
                                            parent.context
                                        ), parent, false
                                    )
                                )
                            }

                            override fun getItemCount(): Int = listSiswa.size

                            override fun onBindViewHolder(holder: HolderSiswa, position: Int) {
                                val item = listSiswa[position]

                                holder.binding.namaSiswa.text = item.nama
                                holder.binding.nisnSiswa.text = "NISN: ${item.nisn}"

                                holder.binding.toolbar.setOnMenuItemClickListener {
                                    when (it.itemId) {
                                        R.id.menu_item_hapus -> {
                                            val builder =
                                                AlertDialog.Builder(holder.itemView.context)
                                            builder.setTitle("Apakah kamu  yakin menghapus dengan NISN: ${item.nisn} ?")
                                            builder.setNegativeButton("Ya") { _, _ ->
                                                RunRetrofit().DeleteSiswa(token.toString(), item.id)
                                                    .enqueue(
                                                        object : Callback<SiswaDeleteResponse> {
                                                            override fun onResponse(
                                                                call: Call<SiswaDeleteResponse>,
                                                                response: Response<SiswaDeleteResponse>
                                                            ) {
                                                                if (response.code() in 200..299) {
                                                                    Toast.makeText(
                                                                        holder.itemView.context,
                                                                        "Siswa berhasil di hapus",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    refresh()
                                                                }
                                                            }
                                                            override fun onFailure(
                                                                call: Call<SiswaDeleteResponse>,
                                                                t: Throwable
                                                            ) {
                                                                Toast.makeText(
                                                                    view?.context,
                                                                    "Gagal menghapus",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        })
                                            }
                                            builder.setPositiveButton("Tidak", null)
                                            builder.create().show()
                                        }

                                        R.id.menu_item_data_diri -> {
                                            getResult.launch(Intent(view?.context, EditSiswaAdminActivity::class.java).apply {
                                                putExtra("data_id", item.id)
                                            })
                                        }

                                        R.id.menu_item_download_pdf -> {
                                            RunRetrofit().exportPDF(token.toString(), item.id).enqueue(
                                                object : Callback<ResponseBody> {
                                                    override fun onResponse(
                                                        call: Call<ResponseBody>,
                                                        response: Response<ResponseBody>
                                                    ) {
                                                        response.body()?.let {
                                                            savePdfFile(it, item.nama)
                                                            if (response.code() in 400..499) Toast.makeText(
                                                                context,
                                                                "Gagal mendownload data",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<ResponseBody>,
                                                        t: Throwable
                                                    ) { }
                                                })
                                        }
                                    }
                                    return@setOnMenuItemClickListener true
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Siswa>>, t: Throwable) {
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            SiswaAdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}