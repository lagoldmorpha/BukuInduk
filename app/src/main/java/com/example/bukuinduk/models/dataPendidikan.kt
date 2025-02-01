package com.example.bukuinduk.models

data class ProgramKeahlian(
    val namaProgram: String,
    val paketKeahlian: List<String>
)

data class BidangKeahlian(
    val bidangKeahlian: String,
    val programKeahlian: List<ProgramKeahlian>
)

class DataPendidikan {
    companion object {
        val data = listOf(
            BidangKeahlian(
                bidangKeahlian = "TEKNOLOGI DAN REKAYASA",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "TEKNIK BANGUNAN",
                        paketKeahlian = listOf(
                            "Teknik Konstruksi Baja",
                            "Teknik Konstruksi Kayu",
                            "Teknik Konstruksi Batu dan Beton",
                            "Teknik Gambar Bangunan"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK FURNITUR",
                        paketKeahlian = listOf("Teknik Furnitur")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK PLAMBING DAN SANITASI",
                        paketKeahlian = listOf("Teknik Plambing & Sanitasi")
                    ),
                    ProgramKeahlian(
                        namaProgram = "GEOMATIKA",
                        paketKeahlian = listOf("Geomatika")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK KETENAGALISTRIKAN",
                        paketKeahlian = listOf(
                            "Teknik Pembangkit Tenaga Listrik",
                            "Teknik Jaringan Tenaga Listrik",
                            "Teknik Instalasi Pemanfaatan Tenaga Listrik",
                            "Teknik Otomasi Industri",
                            "Teknik Pendingin dan Tata Udara"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK MESIN",
                        paketKeahlian = listOf(
                            "Teknik Pemesinan",
                            "Teknik Pengelasan",
                            "Teknik Fabrikasi Logam",
                            "Teknik Pengecoran Logam",
                            "Teknik Pemeliharaan Mekanik Industri",
                            "Teknik Gambar Mesin"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK PESAWAT UDARA",
                        paketKeahlian = listOf(
                            "Pemeliharaan dan Perbaikan Motor dan Rangka Pesawat Udara (Airframe Power Plant)",
                            "Pemeliharaan dan Perbaikan Instrumen Elektronika Pesawat Udara (Electrical Avionics)",
                            "Pemesinan Pesawat Udara (Aircraft Machining)",
                            "Konstruksi Badan Pesawat Udara (Aircraft Sheet Metal Forming)",
                            "Konstruksi Rangka Pesawat Udara (Airframe Mechanics)",
                            "Kelistrikan Pesawat Udara (Aircraft Electricity)",
                            "Elektronika Pesawat Udara (Aviation Electronics)"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK GRAFIKA",
                        paketKeahlian = listOf("Persiapan Grafika", "Produksi Grafika")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK INSTRUMENTASI INDUSTRI",
                        paketKeahlian = listOf(
                            "Teknik Instrumentasi Logam",
                            "Kontrol Proses",
                            "Kontrol Mekanik"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK INDUSTRI",
                        paketKeahlian = listOf("Teknik Pelayanan Produksi", "Teknik Pergudangan")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNOLOGI TEKSTIL",
                        paketKeahlian = listOf(
                            "Teknik Pemintalan Serat Buatan",
                            "Teknik Pembuatan Benang",
                            "Teknik Pembuatan Kain",
                            "Teknik Penyempurnaan Tekstil"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK PERMINYAKAN",
                        paketKeahlian = listOf(
                            "Teknik Produksi Minyak dan Gas",
                            "Teknik Pemboran Minyak dan Gas",
                            "Teknik Pengolahan Minyak, Gas dan Petrokimia"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "GEOLOGI PERTAMBANGAN",
                        paketKeahlian = listOf("Geologi Pertambangan")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK KIMIA",
                        paketKeahlian = listOf("Kimia Analisis", "Kimia Industri")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK OTOMOTIF",
                        paketKeahlian = listOf(
                            "Teknik Kendaraan Ringan",
                            "Teknik Sepeda Motor",
                            "Teknik Perbaikan Bodi Otomotif",
                            "Teknik Alat Berat"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK PERKAPALAN",
                        paketKeahlian = listOf(
                            "Teknik Konstruksi Kapal Baja",
                            "Teknik Konstruksi Kapal Kayu",
                            "Teknik Konstruksi Kapal Fiberglass",
                            "Teknik Instalasi Pemesinan Kapal",
                            "Teknik Pengelasan Kapal",
                            "Kelistrikan Kapal",
                            "Teknik Gambar Rancang Bangun Kapal",
                            "Interior Kapal"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK ELEKTRONIKA",
                        paketKeahlian = listOf(
                            "Teknik Audio Video",
                            "Teknik Elektronika Industri",
                            "Teknik Elektronika Komunikasi",
                            "Teknik Mekatronika",
                            "Teknik Ototronik"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK ENERGI TERBARUKAN",
                        paketKeahlian = listOf(
                            "Teknik Energi Hidro",
                            "Teknik Energi Surya dan Angin",
                            "Teknik Energi Biomassa"
                        )
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "TEKNOLOGI INFORMASI DAN KOMUNIKASI",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "TEKNIK KOMPUTER DAN INFORMATIKA",
                        paketKeahlian = listOf(
                            "Rekayasa Perangkat Lunak",
                            "Teknik Komputer dan Jaringan",
                            "Multimedia"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK TELEKOMUNIKASI",
                        paketKeahlian = listOf(
                            "Teknik Transmisi Telekomunikasi",
                            "Teknik Suitsing",
                            "Teknik Jaringan Akses"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNIK BROADCASTING",
                        paketKeahlian = listOf("Pertelevisian dan Radio")
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "KESEHATAN",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "KESEHATAN",
                        paketKeahlian = listOf(
                            "Keperawatan Kesehatan",
                            "Keperawatan Gigi",
                            "Analis Kesehatan"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "FARMASI",
                        paketKeahlian = listOf("Farmasi Umum", "Farmasi Industri")
                    ),
                    ProgramKeahlian(
                        namaProgram = "PEKERJAAN SOSIAL",
                        paketKeahlian = listOf("Pekerjaan Sosial")
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "AGRIBISNIS DAN AGROTEKNOLOGI",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "AGRIBISNIS PRODUKSI TANAMAN",
                        paketKeahlian = listOf(
                            "Agribisnis Tanaman Pangan dan Hortikultura",
                            "Agribisnis Tanaman Perkebunan",
                            "Agribisnis Perbenihan dan Kultur Jaringan Tanaman"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "AGRIBISNIS PRODUKSI TERNAK",
                        paketKeahlian = listOf(
                            "Agribisnis Ternak Ruminansia",
                            "Agribisnis Ternak Unggas",
                            "Agribisnis Aneka Ternak"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "KESEHATAN HEWAN",
                        paketKeahlian = listOf("Kesehatan Hewan")
                    ),
                    ProgramKeahlian(
                        namaProgram = "AGRIBISNIS PENGOLAHAN HASIL PERTANIAN DAN PERIKANAN",
                        paketKeahlian = listOf(
                            "Teknologi Pengolahan Hasil Pertanian",
                            "Teknologi Pengolahan Hasil Perikanan",
                            "Pengawasan Mutu Hasil Pertanian dan Perikanan"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "MEKANISASI PERTANIAN",
                        paketKeahlian = listOf("Alat Mesin Pertanian", "Teknik Tanah dan Air")
                    ),
                    ProgramKeahlian(
                        namaProgram = "KEHUTANAN",
                        paketKeahlian = listOf(
                            "Teknik Inventarisasi dan Pemetaan Hutan",
                            "Teknik Rehabilitasi dan Reklamasi Hutan",
                            "Teknik Produksi Hasil Hutan",
                            "Teknik Konservasi Sumberdaya Hutan"
                        )
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "PERIKANAN DAN KELAUTAN",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "TEKNOLOGI PENANGKAPAN IKAN",
                        paketKeahlian = listOf(
                            "Nautika Kapal Penangkap Ikan",
                            "Teknika Kapal Penangkap Ikan"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNOLOGI DAN PRODUKSI PERIKANAN BUDIDAYA",
                        paketKeahlian = listOf("Budidaya Ikan")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TEKNOLOGI DAN PRODUKSI PERIKANAN BUDIDAYA",
                        paketKeahlian = listOf(
                            "Budidaya Krustacea",
                            "Budidaya Kekerangan",
                            "Budidaya Rumput Laut"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "PELAYARAN",
                        paketKeahlian = listOf("Nautika Kapal Niaga", "Teknika Kapal Niaga")
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "BISNIS DAN MANAJEMEN",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "ADMINISTRASI",
                        paketKeahlian = listOf("Administrasi Perkantoran")
                    ),
                    ProgramKeahlian(
                        namaProgram = "KEUANGAN",
                        paketKeahlian = listOf("Akuntansi", "Perbankan Umum", "Perbankan Syariah")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TATA NIAGA",
                        paketKeahlian = listOf("Pemasaran")
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "PARIWISATA",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "KEPARIWISATAAN",
                        paketKeahlian = listOf("Akomodasi Perhotelan", "Usaha Perjalanan Wisata")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TATA BOGA",
                        paketKeahlian = listOf("Jasa Boga", "Patiseri")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TATA KECANTIKAN",
                        paketKeahlian = listOf("Kecantikan Rambut", "Kecantikan Kulit")
                    ),
                    ProgramKeahlian(
                        namaProgram = "TATA BUSANA",
                        paketKeahlian = listOf("Tata Busana")
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "SENI RUPA DAN KERAJINAN",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "SENI RUPA",
                        paketKeahlian = listOf(
                            "Seni Lukis",
                            "Seni Patung",
                            "Desain Komunikasi Visual",
                            "Animasi",
                            "Desain Produk Interior & Landscaping"
                        )
                    ),
                    ProgramKeahlian(
                        namaProgram = "DESAIN DAN PRODUKSI KRIA",
                        paketKeahlian = listOf(
                            "Desain dan Produksi Kriya Kayu",
                            "Desain dan Produksi Kriya Kulit",
                            "Desain dan Produksi Kriya Logam",
                            "Desain dan Produksi Kriya Keramik",
                            "Desain dan Produksi Kriya Tekstil"
                        )
                    )
                )
            ),
            BidangKeahlian(
                bidangKeahlian = "SENI PERTUNJUKAN",
                programKeahlian = listOf(
                    ProgramKeahlian(
                        namaProgram = "SENI MUSIK",
                        paketKeahlian = listOf("Musik Klasik", "Musik Non Klasik")
                    ),
                    ProgramKeahlian(
                        namaProgram = "SENI TARI",
                        paketKeahlian = listOf("Seni Tari")
                    ),
                    ProgramKeahlian(
                        namaProgram = "SENI KARAWITAN",
                        paketKeahlian = listOf("Karawitan Tradisi")
                    ),
                    ProgramKeahlian(
                        namaProgram = "SENI PEDALANGAN",
                        paketKeahlian = listOf("Seni Pedalangan")
                    ),
                    ProgramKeahlian(
                        namaProgram = "SENI TEATER",
                        paketKeahlian = listOf("Pemeranan", "Tata Artistik")
                    )
                )
            )
        )

        fun getProgamKeahlian(bidangKeahlian: String): List<ProgramKeahlian>? {
            val result = data.find { it.bidangKeahlian == bidangKeahlian }?.programKeahlian
            return result
        }

        fun getPaketKeahlian(idBidangKeahlian: Int, namaProgram: String): List<String>? {
            return data[idBidangKeahlian].programKeahlian.find { it.namaProgram == namaProgram }?.paketKeahlian
        }
    }
}
