package com.example.bukuinduk.models

import com.google.gson.annotations.SerializedName

data class Dashboard(
    @SerializedName("count_datainputed")
    val countInputedSiswa: Int,
    @SerializedName("count_laki")
    val countLakiLaki: Int,
    @SerializedName("count_perempuan")
    val countPerempuan: Int,
    @SerializedName("count_siswa")
    val countSiswa: Int,
)
