package com.example.bukuinduk.admin

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.FragmentAngkatanAdminBinding
import com.example.bukuinduk.databinding.FragmentJurusanAdminBinding
import com.example.bukuinduk.databinding.LayoutItemSingleBinding
import com.example.bukuinduk.databinding.LayoutItemSingleInputBinding
import com.example.bukuinduk.models.Angkatan
import com.example.bukuinduk.models.Jurusan
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"

class AngkatanAdminFragment : Fragment() {
    private var token: String? = null
    lateinit var binding: FragmentAngkatanAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAngkatanAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh()
        binding.buttonAddPage.setOnClickListener {
            val alerts =  AlertDialog.Builder(view.context)
            val viewBuilder = LayoutItemSingleInputBinding.inflate(layoutInflater)
            alerts.setView(viewBuilder.root)
            viewBuilder.title.text = "Tambah Angkatan"
            viewBuilder.inputData.hint = "Tahun Angkatan"
            val alertsBuilder = alerts.create()
            viewBuilder.buttonSimpan.setOnClickListener {
                if (viewBuilder.inputData.text.isNullOrEmpty()) Toast.makeText(
                    view.context,
                    "Nama jurusan tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    val data = Angkatan(
                        id = 0,
                        tahun = viewBuilder.inputData.text.toString().toInt()
                    )
                    RunRetrofit().CreateAngkatan(token.toString(), data).enqueue(object : Callback<Angkatan> {
                        override fun onResponse(
                            call: Call<Angkatan>,
                            response: Response<Angkatan>
                        ) {
                            response.body()?.let {
                                if (response.code() in 200..299) {
                                    Toast.makeText(view.context, "Angkatan berhasil di tambah", Toast.LENGTH_SHORT).show()
                                    alertsBuilder.dismiss()
                                    refresh()
                                } else Toast.makeText(view.context, "Angkatan gagal di tambahkan", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Angkatan>, t: Throwable) { }
                    })
                }
            }
            alertsBuilder.show()
        }
    }

    fun refresh() {
        RunRetrofit().GetAngkatn(token.toString()).enqueue(object : Callback<List<Angkatan>> {
            override fun onResponse(call: Call<List<Angkatan>>, response: Response<List<Angkatan>>) {
                response.body()?.let {
                    class HolderData(val binding: LayoutItemSingleBinding) :
                        RecyclerView.ViewHolder(binding.root)
                    binding.dataAngkatan.adapter = object : RecyclerView.Adapter<HolderData>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): HolderData = HolderData(
                            LayoutItemSingleBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                            )
                        )

                        override fun getItemCount(): Int = it.size

                        override fun onBindViewHolder(holder: HolderData, position: Int) {
                            val item = it[position]
                            holder.binding.title.text = item.tahun.toString()

                            holder.binding.menuToolbar.setOnMenuItemClickListener { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.menu_item_edit -> {
                                        val alertBuilder = AlertDialog.Builder(holder.itemView.context)
                                        val viewBuilder = LayoutItemSingleInputBinding.inflate(LayoutInflater.from(holder.itemView.context))
                                        alertBuilder.setView(viewBuilder.root)

                                        viewBuilder.title.text = "Ubah angkatan"
                                        viewBuilder.inputData.setText(item.tahun.toString())

                                        val alert = alertBuilder.create()
                                        viewBuilder.buttonSimpan.setOnClickListener {
                                            val data = Angkatan(
                                                id = it.id,
                                                tahun = viewBuilder.inputData.text.toString().toInt()
                                            )
                                            RunRetrofit().UpdateAngkatan(token.toString(), item.id, data).enqueue(
                                                object : Callback<Angkatan> {
                                                    override fun onResponse(
                                                        call: Call<Angkatan>,
                                                        response: Response<Angkatan>
                                                    ) {
                                                        response.body()?.let {
                                                            if (response.code() in 200..299) {
                                                                Toast.makeText(
                                                                    holder.itemView.context,
                                                                    "Angkatan berhasil di ubah",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                alert.dismiss()
                                                                refresh()
                                                            }else Toast.makeText(
                                                                holder.itemView.context,
                                                                "Ankatan Gagal di ubah",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<Angkatan>,
                                                        t: Throwable
                                                    ) { }
                                                })
                                        }
                                        alert.show()
                                    }

                                    R.id.menu_item_hapus -> {
                                        val alertBuilder =
                                            AlertDialog.Builder(holder.itemView.context)
                                        alertBuilder.setTitle("Apakah Kamu yakin akan menghapus ${item.tahun}")
                                        alertBuilder.setPositiveButton("Ya") { _, _ ->
                                            RunRetrofit().DeleteAngkatan(token.toString(), item.id)
                                                .enqueue(
                                                    object : Callback<Any> {
                                                        override fun onResponse(
                                                            call: Call<Any>,
                                                            response: Response<Any>
                                                        ) {
                                                            response.body()?.let {
                                                                if (response.code() in 200..299) {
                                                                    Toast.makeText(
                                                                        holder.itemView.context,
                                                                        "Angkatan berhasil di hapus",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    refresh()
                                                                } else Toast.makeText(
                                                                    holder.itemView.context,
                                                                    "angkatan Gagal di hapus",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }

                                                        override fun onFailure(
                                                            call: Call<Any>,
                                                            t: Throwable
                                                        ) { }
                                                    })
                                        }
                                        alertBuilder.setNegativeButton("Tidak", null)
                                        alertBuilder.create().show()
                                    }
                                }
                                return@setOnMenuItemClickListener true
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Angkatan>>, t: Throwable) {}
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            AngkatanAdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}