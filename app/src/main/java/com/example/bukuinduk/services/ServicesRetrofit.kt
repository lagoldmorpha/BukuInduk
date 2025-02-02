package com.example.bukuinduk.services

import com.example.bukuinduk.models.AdminCodeResponse
import com.example.bukuinduk.models.AdminLoginRequest
import com.example.bukuinduk.models.AdminLoginResponse
import com.example.bukuinduk.models.AdminMe
import com.example.bukuinduk.models.Angkatan
import com.example.bukuinduk.models.AyahKandung
import com.example.bukuinduk.models.Dashboard
import com.example.bukuinduk.models.DataDiri
import com.example.bukuinduk.models.Hobi
import com.example.bukuinduk.models.IbuKandung
import com.example.bukuinduk.models.Jurusan
import com.example.bukuinduk.models.Kesehatan
import com.example.bukuinduk.models.Perkembangan
import com.example.bukuinduk.models.SetelahPendidikan
import com.example.bukuinduk.models.Siswa
import com.example.bukuinduk.models.SiswaByID
import com.example.bukuinduk.models.SiswaDeleteResponse
import com.example.bukuinduk.models.SiswaLoginRequest
import com.example.bukuinduk.models.SiswaLoginResponse
import com.example.bukuinduk.models.SiswaRequest
import com.example.bukuinduk.models.SiswaRequestCreate
import com.example.bukuinduk.models.TempatTinggal
import com.example.bukuinduk.models.UserDetail
import com.example.bukuinduk.models.UserMe
import com.example.bukuinduk.models.Wali
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ServicesRetrofit {
    @POST("/auth/login-admin")
    fun LoginAdmin(@Body data: AdminLoginRequest): Call<AdminLoginResponse>

    @POST("/auth/code-admin")
    fun CodeAdmin(@Body data: AdminLoginResponse): Call<AdminCodeResponse>

    @POST("/auth/login-siswa")
    fun LoginSiswa(@Body data: SiswaLoginRequest): Call<SiswaLoginResponse>

    @GET("/auth/me")
    fun GetMeAdmin(@Header("Authorization") token: String): Call<AdminMe>

    @GET("/auth/me")
    fun GetMeSiswa(@Header("Authorization") token: String): Call<UserMe>

    @GET("/admin/dashboard")
    fun AdminDashboard(@Header("Authorization") token: String): Call<Dashboard>

    @GET("/admin/akun")
    fun GetAllSiswa(@Header("Authorization") token: String, @Query("jurusan") jurusan: String?, @Query("angkatan") angkatan: String?, @Query("search") search:String?): Call<List<Siswa>>

    @GET("/admin/akun/{id}")
    fun GetOneSiswa(@Header("Authorization") token: String, @Path("id") id: Int): Call<SiswaByID>

    @GET("/siswa/jurusan")
    fun GetJurusanSiswa(): Call<List<Jurusan>>

    @GET("/siswa/angkatan")
    fun GetAngkatanSiswa(): Call<List<Angkatan>>

    @DELETE("/admin/akun/{id}")
    fun DeleteSiswa(@Header("Authorization") token: String, @Path("id") id: Int): Call<SiswaDeleteResponse>

    @POST("/siswa/perkembangan/{id}")
    fun CreateSiswaPerkembangan(@Body data: Perkembangan, @Path("id") id: Int): Call<Perkembangan>

    @PUT("/admin/data-diri/{id}")
    fun UpdateDatadiri(@Header("Authorization") token: String, @Body data: UserDetail, @Path("id")id: Int):Call<Any>

    @GET("/admin/jurusan")
    fun GetJurusan(@Header("Authorization") token: String): Call<List<Jurusan>>

    @GET("/admin/jurusan/{id}")
    fun  GetOneJurusan(@Header("Authorization") token: String, @Path("id") id: Int): Call<Jurusan>

    @POST("/admin/jurusan")
    fun CreateJurusan(@Header("Authorization") token: String, @Body data: Jurusan): Call<Jurusan>

    @PUT("/admin/jurusan/{id}")
    fun UpdateJurusan(@Header("Authorization") token: String, @Path("id") id: Int, @Body data: Jurusan): Call<Jurusan>

    @DELETE("/admin/jurusan/{id}")
    fun DeleteJurusan(@Header("Authorization") token: String, @Path("id") id: Int): Call<Any>

    @GET("/admin/angkatan")
    fun GetAngkatn(@Header("Authorization") token: String): Call<List<Angkatan>>

    @GET("/admin/angkatan/{id}")
    fun  GetOneAngkatan(@Header("Authorization") token: String, @Path("id") id: Int): Call<Angkatan>

    @POST("/admin/angkatan")
    fun CreateAngkatan(@Header("Authorization") token: String, @Body data: Angkatan): Call<Angkatan>

    @PUT("/admin/angkatan/{id}")
    fun UpdateAngkatan(@Header("Authorization") token: String, @Path("id") id: Int, @Body data: Angkatan): Call<Angkatan>

    @DELETE("/admin/angkatan/{id}")
    fun DeleteAngkatan(@Header("Authorization") token: String, @Path("id") id: Int): Call<Any>

    @POST("/siswa/data-diri")
    fun CreateDataDiri(@Body data: SiswaRequestCreate): Call<Any>

    @GET("/admin/export-excel")
    @Streaming
    fun exportExcel(@Header("Authorization") token: String, @Query("jurusan") jurusan: String?, @Query("angkatan") angkatan: String?, @Query("search") search:String?): Call<ResponseBody>

    @GET("/admin/export-pdf/{id}")
    @Streaming
    fun exportPDF(@Header("Authorization") token: String, @Path("id") id: Int): Call<ResponseBody>
}

fun RunRetrofit(): ServicesRetrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://1e48-2a09-bac1-3480-50-00-3c1-52.ngrok-free.app")
        .build().create(ServicesRetrofit::class.java)
}