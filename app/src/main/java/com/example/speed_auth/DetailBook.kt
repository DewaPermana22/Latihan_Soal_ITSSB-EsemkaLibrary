package com.example.speed_auth

import android.content.Intent
import android.graphics.Interpolator
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.speed_auth.databinding.ActivityDetailBookBinding
import com.example.speed_auth.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class DetailBook : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra("id", 0)
        Log.d("Id Book", "Ini Idnya : $id")

        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("https://6768c1dbcbf3d7cefd389bf2.mockapi.io/books/$id").openStream().bufferedReader().readText()
            val data = JSONObject(conn)
            withContext(Dispatchers.Main){
                binding.titleBook.text = data.getString("title")
                binding.nameAuthor.text = data.getString("author")
                binding.nameCountry.text = data.getString("country")
                binding.nameLang.text = data.getString("languange")
                binding.nameYears.text = data.getString("year")
                binding.namePages.text = data.getString("pages")
                binding.nameSummary.text = data.getString("summary")
            }

            binding.deleteBtn.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch{
                    val koneksi = URL("https://6768c1dbcbf3d7cefd389bf2.mockapi.io/books/$id").openConnection() as HttpURLConnection
                    koneksi.requestMethod = "DELETE"
                    val res_code = koneksi.responseCode
                    if ( res_code in 200..299){
                        Toast.makeText(this@DetailBook, "Delete Succesfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DetailBook, MainActivity::class.java))
                    }
                }
            }

            binding.editBtn.setOnClickListener{
                val intent = Intent(this@DetailBook, CreateActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("title", data.getString("title"))
                intent.putExtra("author", data.getString("author"))
                intent.putExtra("country", data.getString("country"))
                intent.putExtra("languange", data.getString("languange"))
                intent.putExtra("year", data.getInt("year"))
                intent.putExtra("pages", data.getInt("pages"))
                intent.putExtra("summary", data.getString("summary"))
                startActivity(intent)
            }
        }
    }
}