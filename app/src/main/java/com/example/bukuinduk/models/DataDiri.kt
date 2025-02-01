package com.example.bukuinduk.models

import com.google.gson.annotations.SerializedName

data class AyahKandung(
    @SerializedName("nama")
    val nama: String? = null,
    @SerializedName("tempat_lahir")
    val tempatLahir: String? = null,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,
    @SerializedName("agama")
    val agama: String? = null,
    @SerializedName("kewarganegaraan")
    val kewarganegaraan: String? = null,
    @SerializedName("pendidikan")
    val pendidikan: String? = null,
    @SerializedName("pekerjaan")
    val pekerjaan: String? = null,
    @SerializedName("pengeluaran_per_bulan")
    val pengeluaranPerBulan: String? = null,
    @SerializedName("alamat_dan_no_telepon")
    val alamatDanNoTelepon: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("user_id")
    val userId: Int
)

data class DataDiri(
    @SerializedName("nama_lengkap")
    val namaLengkap: String? = null,
    @SerializedName("nama_panggilan")
    val namaPanggilan: String? = null,
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String? = null,
    @SerializedName("tempat_lahir")
    val tempatLahir: String? = null,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,
    @SerializedName("agama")
    val agama: String? = null,
    @SerializedName("kewarganegaraan")
    val kewarganegaraan: String? = null,
    @SerializedName("anak_ke")
    val anakKe: Int? = null,
    @SerializedName("jml_saudara_kandung")
    val jumlahSaudaraKandung: Int? = null,
    @SerializedName("jml_saudara_tiri")
    val jumlahSaudaraTiri: Int? = null,
    @SerializedName("jml_saudara_angkat")
    val jumlahSaudaraAngkat: Int? = null,
    @SerializedName("kelengkapan_ortu")
    val kelengkapanOrtu: String? = null,
    @SerializedName("bahasa_sehari_hari")
    val bahasaSehariHari: String? = null,
    @SerializedName("user_id")
    val userId: Int? = null
)

data class Hobi(
    @SerializedName("kesenian")
    val kesenian: String? = null,
    @SerializedName("olahraga")
    val olahraga: String? = null,
    @SerializedName("organisasi")
    val organisasi: String? = null,
    @SerializedName("lain_lain")
    val lainLain: String? = null,
    @SerializedName("user_id")
    val userId: Int
)

data class IbuKandung(
    @SerializedName("nama")
    val nama: String? = null,
    @SerializedName("tempat_lahir")
    val tempatLahir: String? = null,
    @SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,
    @SerializedName("agama")
    val agama: String? = null,
    @SerializedName("kewarganegaraan")
    val kewarganegaraan: String? = null,
    @SerializedName("pendidikan")
    val pendidikan: String? = null,
    @SerializedName("pekerjaan")
    val pekerjaan: String? = null,
    @SerializedName("pengeluaran_per_bulan")
    val pengeluaranPerBulan: String? = null,
    @SerializedName("alamat_dan_no_telepon")
    val alamatDanNoTelepon: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("user_id")
    val userId: Int
)

data class Kesehatan(
    @SerializedName("gol_darah")
    val golDarah: String? = null,
    @SerializedName("penyakit_pernah_diderita")
    val penyakitPernahDiderita: String? = null,
    @SerializedName("kelainan_jasmani")
    val kelainanJasmani: String? = null,
    @SerializedName("tinggi")
    val tinggi: String? = null,
    @SerializedName("berat_badan")
    val beratBadan: String? = null,
    @SerializedName("user_id")
    val userId: Int
)

data class Pendidikan(
    @SerializedName("sebelumnya_tamatan_dari")
    val sebelumnyaTamatanDari: String? = null,
    @SerializedName("sebelumnya_tanggal_dan_ijazah")
    val sebelumnyaTanggalDanIjazah: String? = null,
    @SerializedName("sebelumnya_tanggal_skhun_dan_")
    val sebelumnyaTanggalSkhunDanIjazah: String? = null,
    @SerializedName("sebelumnya_lama_belajar")
    val sebelumnyaLamaBelajar: String? = null,
    @SerializedName("pindahan_dari_sekolah")
    val pindahanDariSekolah: String? = null,
    @SerializedName("pindahan_alasan")
    val pindahanAlasan: String? = null,
    @SerializedName("diterima_di_kelas")
    val diterimaDiKelas: Int? = null,
    @SerializedName("diterima_di_bidang_keahlian")
    val diterimaDiBidangKeahlian: String? = null,
    @SerializedName("diterima_di_program_keahlian")
    val diterimaDiProgramKeahlian: String? = null,
    @SerializedName("diterima_di_paket_keahlian")
    val diterimaDiPaketKeahlian: String? = null,
    @SerializedName("diterima_tanggal")
    val diterimaTanggal: String? = null,
    @SerializedName("user_id")
    val userId: Int
)

data class Perkembangan(
    @SerializedName("menerima_bea_siswa_tahun_kelas_dari")
    val menerimaBeasiswaTahunKelasDari: String,
    @SerializedName("meninggalkan_sekolah_ini_tanggal")
    val meninggalkanSekolahIniTanggal: String,
    @SerializedName("meninggalkan_sekolah_ini_alasan")
    val meninggalkanSekolahIniAlasan: String,
    @SerializedName("akhir_pendidikan_tamat_belajar_lulus_tahun")
    val akhirPendidikanTamatBelajarLulusTahun: String,
    @SerializedName("akhir_pendidikan_no_tanggal_ijazah")
    val akhirPendidikanNoTanggalIjazah: String,
    @SerializedName("akhir_pendidikan_no_tanggal_skhun")
    val akhirPendidikanNoTanggalSkhun: String
)

data class SetelahPendidikan(
    @SerializedName("melanjutkan_ke")
    val melanjutkanKe: String? = null,
    @SerializedName("bekerja_nama_perusahaan")
    val bekerjaNamaPerusahaan: String? = null,
    @SerializedName("bekerja_tanggal_mulai")
    val bekerjaTanggalMulai: String? = null,
    @SerializedName("bekerja_penghasilan")
    val bekerjaPenghasilan: String? = null,
    @SerializedName("user_id")
    val userId: Int
)

data class TempatTinggal(
    @SerializedName("alamat")
    val alamat: String,
    @SerializedName("no_telepon")
    val noTelepon: String,
    @SerializedName("tinggal_dengan")
    val tinggalDengan: String,
    @SerializedName("jarak_ke_sekolah")
    val jarakKeSekolah: String,
    @SerializedName("user_id")
    val userId: Int
)


data class Wali(
    @SerializedName("nama") val nama: String,
    @SerializedName("tempat_lahir") val tempatLahir: String,
    @SerializedName("tanggal_lahir") val tanggalLahir: String,
    @SerializedName("agama") val agama: String,
    @SerializedName("kewarganegaraan") val kewarganegaraan: String,
    @SerializedName("pendidikan") val pendidikan: String,
    @SerializedName("pekerjaan") val pekerjaan: String,
    @SerializedName("pengeluaran_per_bulan") val pengeluaranPerBulan: String,
    @SerializedName("alamat_dan_no_telepon") val alamatDanNoTelepon: String,
)

data class UserDetail(
    @SerializedName("ayah_kandung")
    val ayahKandung: AyahKandung? = null,
    @SerializedName("data_diri")
    val dataDiri: DataDiri? = null,
    @SerializedName("hobi")
    val hobi: Hobi? = null,
    @SerializedName("ibu_kandung")
    val ibuKandung: IbuKandung? = null,
    @SerializedName("kesehatan")
    val kesehatan: Kesehatan? = null,
    @SerializedName("pendidikan")
    val pendidikan: Pendidikan? = null,
    @SerializedName("perkembangan")
    val perkembangan: Perkembangan? = null,
    @SerializedName("tempat_tinggal")
    val tempatTinggal: TempatTinggal? = null,
    @SerializedName("wali")
    val wali: Wali? = null,
    @SerializedName("setelah_pendidikan")
    val setelahPendidikan: SetelahPendidikan? = null,
)

