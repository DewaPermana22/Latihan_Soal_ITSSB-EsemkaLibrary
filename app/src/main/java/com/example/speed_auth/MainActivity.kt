package com.example.speed_auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speed_auth.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newBook.setOnClickListener{
            startActivity(Intent(this@MainActivity, CreateActivity::class.java))
        }

        binding.etSearch.doOnTextChanged {
            item,_,_,_, -> search(item.toString())
        }

        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("https://6768c1dbcbf3d7cefd389bf2.mockapi.io/books").openStream().bufferedReader().readText()
            val respon = JSONArray(conn)
            withContext(Dispatchers.Main){
                binding.rcItemBook.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.rcItemBook.adapter = BookAdapter(respon){ item ->
                    val intent = Intent(this@MainActivity, DetailBook::class.java)
                    intent.putExtra("id", item.getInt("id"))
                    startActivity(intent)
                }
            }
        }
    }

    fun search(Keyword : String){
        try {
            val rc = findViewById<RecyclerView>(R.id.rc_item_book)
            CoroutineScope(Dispatchers.IO).launch{
                val conn = URL("https://6768c1dbcbf3d7cefd389bf2.mockapi.io/books?search=$Keyword").openStream().bufferedReader().readText()
                val respon = JSONArray(conn)
                withContext(Dispatchers.Main){
                    rc.layoutManager = LinearLayoutManager(this@MainActivity)
                    rc.adapter = BookAdapter(respon){ item ->
                        val intent = Intent(this@MainActivity, DetailBook::class.java)
                        intent.putExtra("id", item.getInt("id"))
                        startActivity(intent)
                    }
                }
            }
        } catch (e: Exception) {
            TODO("Not yet implemented")
        }
    }
}