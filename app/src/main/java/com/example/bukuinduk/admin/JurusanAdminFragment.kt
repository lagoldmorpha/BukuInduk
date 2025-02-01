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
import com.example.bukuinduk.databinding.FragmentJurusanAdminBinding
import com.example.bukuinduk.databinding.LayoutItemSingleBinding
import com.example.bukuinduk.databinding.LayoutItemSingleInputBinding
import com.example.bukuinduk.models.Jurusan
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "token"

class JurusanAdminFragment : Fragment() {
    private var token: String? = null
    lateinit var binding: FragmentJurusanAdminBinding

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
        binding = FragmentJurusanAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreh()

        binding.buttonAddPage.setOnClickListener {
            val alert = AlertDialog.Builder(view.context)
            val viewBuilder = LayoutItemSingleInputBinding.inflate(LayoutInflater.from(view.context))
            alert.setView(viewBuilder.root)
            viewBuilder.title.text = "Tambah Jurusan"
            viewBuilder.inputData.hint = "Nama Jurusan"
            val alertBuilder = alert.create()
            viewBuilder.buttonSimpan.setOnClickListener {
                if (viewBuilder.inputData.text.isNullOrEmpty()) Toast.makeText(
                    view.context,
                    "Nama jurusan tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    val data = Jurusan(
                        id = 0,
                        nama = viewBuilder.inputData.text.toString()
                    )
                    RunRetrofit().CreateJurusan(token.toString(), data).enqueue(object : Callback<Jurusan> {
                        override fun onResponse(call: Call<Jurusan>, response: Response<Jurusan>) {
                            response.body()?.let {
                                if (response.code() in 200..299){
                                    Toast.makeText(view.context, "Jurusan berhasil di tambahkan", Toast.LENGTH_SHORT).show()
                                    alertBuilder.dismiss()
                                    refreh()
                                }else Toast.makeText(view.context, "jurusan gagal di tambahkan", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Jurusan>, t: Throwable) {}
                    })
                }
            }
            alertBuilder.show()
        }
    }

    fun refreh() {
        RunRetrofit().GetJurusan(token.toString()).enqueue(object : Callback<List<Jurusan>> {
            override fun onResponse(call: Call<List<Jurusan>>, response: Response<List<Jurusan>>) {
                response.body()?.let {
                    class HolderData(val binding: LayoutItemSingleBinding) :
                        RecyclerView.ViewHolder(binding.root)
                    binding.dataJurusan.adapter = object : RecyclerView.Adapter<HolderData>() {
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
                            holder.binding.title.text = item.nama

                            holder.binding.menuToolbar.setOnMenuItemClickListener { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.menu_item_edit -> {
                                        val alertBuilder = AlertDialog.Builder(holder.itemView.context)
                                        val viewBuilder = LayoutItemSingleInputBinding.inflate(LayoutInflater.from(holder.itemView.context))
                                        alertBuilder.setView(viewBuilder.root)

                                        viewBuilder.title.text = "Ubah Jurusan"
                                        viewBuilder.inputData.setText(item.nama)

                                        val alert = alertBuilder.create()
                                        viewBuilder.buttonSimpan.setOnClickListener {
                                            val data = Jurusan(
                                                id = it.id,
                                                nama = viewBuilder.inputData.text.toString()
                                            )
                                            RunRetrofit().UpdateJurusan(token.toString(), item.id, data).enqueue(
                                                object : Callback<Jurusan> {
                                                    override fun onResponse(
                                                        call: Call<Jurusan>,
                                                        response: Response<Jurusan>
                                                    ) {
                                                        response.body()?.let {
                                                            if (response.code() in 200..299) {
                                                                Toast.makeText(
                                                                    holder.itemView.context,
                                                                    "Jurusan berhasil di ubah",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                alert.dismiss()
                                                                refreh()
                                                            }else Toast.makeText(
                                                                holder.itemView.context,
                                                                "Jurusan Gagal di ubah",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<Jurusan>,
                                                        t: Throwable
                                                    ) { }
                                                })
                                        }
                                        alert.show()
                                    }


                                    R.id.menu_item_hapus -> {
                                        val alertBuilder =
                                            AlertDialog.Builder(holder.itemView.context)
                                        alertBuilder.setTitle("Apakah Kamu yakin akan menghapus ${item.nama}")
                                        alertBuilder.setPositiveButton("Ya") { _, _ ->
                                            RunRetrofit().DeleteJurusan(token.toString(), item.id)
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
                                                                        "Jurusan berhasil di hapus",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    refreh()
                                                                } else Toast.makeText(
                                                                    holder.itemView.context,
                                                                    "Jurusan Gagal di hapus",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }

                                                        override fun onFailure(
                                                            call: Call<Any>,
                                                            t: Throwable
                                                        ) {
                                                        }
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

            override fun onFailure(call: Call<List<Jurusan>>, t: Throwable) {}
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            JurusanAdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}