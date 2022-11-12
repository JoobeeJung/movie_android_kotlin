package com.kbstar.movieproject.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kbstar.movieproject.MovieDetailActivity
import com.kbstar.movieproject.R
import com.kbstar.movieproject.databinding.ItemMainBinding
import com.kbstar.movieproject.dto.BoxOffice
import com.kbstar.movieproject.dto.MovieSearched

class SearchViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

class SearchAdapter(val context: Context, val datas: MutableList<MovieSearched>): RecyclerView.Adapter<SearchViewHolder>(){

    lateinit var detailLauncher: ActivityResultLauncher<Intent>
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        Glide.with(context)
            .load(datas[position].image)
            .thumbnail(Glide.with(context).load(R.drawable.loading_unscreen))
            .override(800, 800)
            .into(holder.binding.mainItemImageView)
        holder.binding.mainItemTextView.text = datas[position].title

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra("id", "${datas[position].id}")
            context.startActivity(intent)
            //영화 상세페이지 이동
        }
    }

}