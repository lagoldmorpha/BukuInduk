package com.example.bukuinduk.models

import com.google.gson.annotations.SerializedName

data class Jurusan(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama")
    val nama: String
)

data class Angkatan(
    @SerializedName("id")
    val id: Int,
    @SerializedName("tahun")
    val tahun: Int
)
