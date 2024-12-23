package com.example.speed_auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import javax.xml.namespace.QName

class BookAdapter (val data : JSONArray, val onClick : (JSONObject) -> Unit ) : RecyclerView.Adapter<BookAdapter.BookHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookHolder {
        val views = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookHolder(views)
    }

    override fun onBindViewHolder(
        holder: BookHolder,
        position: Int
    ) {
       val jsonObj = data.getJSONObject(position)
        holder.name.text = jsonObj.getString("title")
        holder.author.text = jsonObj.getString("author")
        holder.detail.setOnClickListener{
            onClick(jsonObj)
        }
    }

    override fun getItemCount(): Int {
        return  data.length()
    }

    class BookHolder(view : View) : RecyclerView.ViewHolder(view.rootView){
        val name = view.findViewById<TextView>(R.id.name_book)
        val author = view.findViewById<TextView>(R.id.author_book)
        val detail = view.findViewById<Button>(R.id.detail_book)
    }
}