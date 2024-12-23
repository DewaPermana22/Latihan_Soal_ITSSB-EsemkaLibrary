package com.example.speed_auth

import android.content.Intent
import android.graphics.Interpolator
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.speed_auth.databinding.ActivityCreateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class CreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id_dari_detail = intent.getIntExtra("id", 0)
        Log.d("Id data", "Data Dari Detail : $id_dari_detail")
        if (id_dari_detail == null ) {
            val name = binding.titleCreate.text.toString()
            val author = binding.authorCreate.text.toString()
            val country = binding.countryCreate.text.toString()
            val lang = binding.langCreate.text.toString()
            val page = binding.pageCreate.text.toString().toInt()
            val year = binding.yearCreate.text.toString().toInt()
            val sumary = binding.sumCreate.text.toString()
            binding.saveBtn.setOnClickListener {
                insert(name, author, country, lang, year, page, sumary)
            }
        } else {

            binding.titleCreate.setText(intent.getStringExtra("title"))
            binding.authorCreate.setText(intent.getStringExtra("author"))
            binding.countryCreate.setText(intent.getStringExtra("country"))
            binding.langCreate.setText(intent.getStringExtra("languange"))
            binding.pageCreate.setText(intent.getIntExtra("pages", 0).toString())
            binding.yearCreate.setText(intent.getIntExtra("year", 0).toString())
            binding.sumCreate.setText(intent.getStringExtra("summary"))

            binding.saveBtn.setOnClickListener{
                val name = binding.titleCreate.text.toString()
                val author = binding.authorCreate.text.toString()
                val country = binding.countryCreate.text.toString()
                val lang = binding.langCreate.text.toString()
                val page = binding.pageCreate.text.toString().toInt()
                val year = binding.yearCreate.text.toString().toInt()
                val sumary = binding.sumCreate.text.toString()
                update(name, author, country, lang, year, page, sumary)
            }
        }
    }

    fun insert(title : String, author : String, country : String, lang : String, Year : Int, Pages : Int, sumary : String){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("https://6768c1dbcbf3d7cefd389bf2.mockapi.io/books").openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = "POST"
            conn.doOutput = true


            val reques = """
            {
                "title" : "$title",
                "author" : "$author",
                "country" : "$country",
                "languange" : "$lang",
                "year" : $Year,
                "page" : $Pages,
                "summary" : "$sumary"
            }
        """.trimIndent()

            conn.outputStream.use{ os -> os.write( reques.toString().toByteArray())}
            val res = conn.responseCode
            Log.d("responCode", "rescode : $res")

            if (res in 200..299){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@CreateActivity, "Succes Insert Data!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CreateActivity, MainActivity::class.java))
                }
            }
        }
    }

    fun update(title : String, author : String, country : String, lang : String, Year : Int, Pages : Int, sumary : String){
        val id_dari_detail = intent.getIntExtra("id", 0)
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("https://6768c1dbcbf3d7cefd389bf2.mockapi.io/books/$id_dari_detail").openConnection() as HttpURLConnection
            conn.requestMethod = "PUT"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            val reques = """
            {
                "title" : "$title",
                "author" : "$author",
                "country" : "$country",
                "languange" : "$lang",
                "year" : $Year,
                "page" : $Pages,
                "summary" : "$sumary"
            }
        """.trimIndent()
            conn.outputStream.use{ os -> os.write( reques.toString().toByteArray())}
            val res = conn.responseCode
            Log.d("responCode", "rescode : $res")

            if (res in 200..299){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@CreateActivity, "Succes Update Data!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CreateActivity, MainActivity::class.java))
                }
            }
        }
    }
}