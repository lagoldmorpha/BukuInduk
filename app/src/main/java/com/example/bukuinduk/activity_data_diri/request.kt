import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import com.example.bukuinduk.R

class request : AppCompatActivity() {

    private lateinit var formContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request) // Menggunakan layout activity_request

        val formSpinner: Spinner = findViewById(R.id.form_spinner)
        formContainer = findViewById(R.id.form_container)

        // Menyiapkan data untuk Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.form_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        formSpinner.adapter = adapter

        // Menangani pemilihan item dari Spinner
        formSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        // Menampilkan Form Ibu
                        formContainer.removeAllViews()
                        layoutInflater.inflate(R.layout.activity_hobi, formContainer, true)
                    }
                    1 -> {
                        // Menampilkan Form Ayah
                        formContainer.removeAllViews()
                        layoutInflater.inflate(R.layout.activity_ibu_kandung, formContainer, true)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada tindakan jika tidak ada yang dipilih
            }
        }
    }
}
