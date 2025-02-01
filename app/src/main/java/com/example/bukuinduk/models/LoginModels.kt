package com.example.bukuinduk.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AdminLoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class AdminLoginResponse(
    @SerializedName("code")
    val code: String
)

data class AdminCodeResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: String
)

data class SiswaLoginRequest(
    @SerializedName("nisn")
    val nisn: String
)

data class SiswaLoginResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("full_name")
    val fullName: String,
)

data class AdminMe(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: String
)

data class UserMe(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nisn")
    val nisn: String,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("jurusan")
    val jurusan: String,
    @SerializedName("angkatan")
    val angkatan: Int,
    @SerializedName("token")
    val token: String
)