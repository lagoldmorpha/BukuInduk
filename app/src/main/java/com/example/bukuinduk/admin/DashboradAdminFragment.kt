package com.example.bukuinduk.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bukuinduk.R
import com.example.bukuinduk.databinding.FragmentDashboradAdminBinding
import com.example.bukuinduk.models.AdminMe
import com.example.bukuinduk.models.Dashboard
import com.example.bukuinduk.services.RunRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "token"

class DashboradAdminFragment : Fragment() {
    private var token: String? = null
    lateinit var binding: FragmentDashboradAdminBinding

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
        binding = FragmentDashboradAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RunRetrofit().GetMeAdmin(token.toString()).enqueue(object : Callback<AdminMe> {
            override fun onResponse(call: Call<AdminMe>, response: Response<AdminMe>) {
                response.body()?.let {
                    binding.username.text = "Halo, ${it.username}"

                    if (response.code() in 400..499) {

                    }
                }
            }

            override fun onFailure(call: Call<AdminMe>, t: Throwable) { }
        })

        RunRetrofit().AdminDashboard(token.toString()).enqueue(object : Callback<Dashboard> {
            override fun onResponse(call: Call<Dashboard>, response: Response<Dashboard>) {
                response.body()?.let {
                    binding.totalSiswa.text = it.countLakiLaki.toString()
                    binding.totalSiswi.text= it.countPerempuan.toString()
                    binding.dataDiInputkan.text = it.countInputedSiswa.toString()
                    binding.totalDataSiswa.text = it.countSiswa.toString()
                }
            }

            override fun onFailure(call: Call<Dashboard>, t: Throwable) {
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            DashboradAdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}