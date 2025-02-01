package com.example.bukuinduk.models

import com.google.gson.annotations.SerializedName

data class Siswa(
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
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("status_perkembangan")
    val statusPerkembangan: Boolean,
    @SerializedName("token")
    val token: String
)

data class SiswaRequest(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nisn")
    val nisn: String,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("jurusan_id")
    val jurusanId: Int,
    @SerializedName("angkatan_id")
    val angkatanId: Int
)

data class SiswaDeleteResponse(
    @SerializedName("message")
    val message: String
)

data class SiswaByID(
    @SerializedName("id") val id: Int,
    @SerializedName("nisn") val nisn: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("angkatan_id") val angkatanId: Int,
    @SerializedName("jurusan_id") val jurusanId: Int,
    @SerializedName("data_diri") val dataDiri: DataDiri?,
    @SerializedName("perkembangan") val perkembangan: Perkembangan?,
    @SerializedName("ayah_kandung") val ayahKandung: AyahKandung?,
    @SerializedName("ibu_kandung") val ibuKandung: IbuKandung?,
    @SerializedName("kesehatan") val kesehatan: Kesehatan?,
    @SerializedName("pendidikan") val pendidikan: Pendidikan?,
    @SerializedName("setelah_pendidikan") val setelahPendidikan: SetelahPendidikan?,
    @SerializedName("tempat_tinggal") val tempatTinggal: TempatTinggal?,
    @SerializedName("wali") val wali: Wali?,
    @SerializedName("hobi_siswa") val hobiSiswa: Hobi?,
    @SerializedName("status_perkembangan") val statusPerkembangan: Boolean,
    @SerializedName("jurusan") val jurusan: Jurusan,
    @SerializedName("angkatan") val angkatan: Angkatan,
)

data class SiswaCreate(
    @SerializedName("nisn")
    val nisn: String,
    @SerializedName("jurusan_id")
    val jurusanId: Int,
    @SerializedName("angkatan_id")
    val angkatanId: Int,
)

data class SiswaRequestCreate(
    @SerializedName("siswa")
    var siswa: SiswaCreate?,
    @SerializedName("data_diri")
    var dataDiri: DataDiri?,
    @SerializedName("ayah_kandung")
    var ayahKandung: AyahKandung?,
    @SerializedName("ibu_kandung")
    var ibuKandung: IbuKandung?,
    @SerializedName("kesehatan")
    var kesehatan: Kesehatan?,
    @SerializedName("pendidikan")
    var pendidikan: Pendidikan?,
    @SerializedName("setelah_pendidikan")
    var setelahPendidikan: SetelahPendidikan?,
    @SerializedName("tempat_tinggal")
    var tempatTinggal: TempatTinggal?,
    @SerializedName("wali")
    var wali: Wali?,
    @SerializedName("hobi_siswa")
    var hobiSiswa: Hobi?,
)