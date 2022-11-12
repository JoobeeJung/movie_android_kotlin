package com.kbstar.movieproject.recyclerview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kbstar.movieproject.MovieDetailActivity
import com.kbstar.movieproject.R
import com.kbstar.movieproject.databinding.ItemMainBinding
import com.kbstar.movieproject.dto.BoxOffice

class MainViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)

class MainAdapter(val context: Context, val datas: MutableList<BoxOffice>): RecyclerView.Adapter<MainViewHolder>(){

    lateinit var detailLauncher: ActivityResultLauncher<Intent>
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        Glide.with(context)
            .load(datas[position].movie_thumbnail)
            .thumbnail(Glide.with(context).load(R.drawable.loading_unscreen))
            .fitCenter()
            .override(800, 800)
            .into(holder.binding.mainItemImageView)
//        holder.binding.mainItemRankTextView.text = datas[position].movie_rank.toString()
        holder.binding.mainItemTextView.text = datas[position].movie_name

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra("id", "${datas[position].movie_IMDbId}")
            intent.putExtra("title", "${datas[position].movie_name}")
            intent.putExtra("from","${datas[position].movie_idx}")
            intent.putExtra("image","${datas[position].movie_thumbnail}")

            Log.d("jbjung adapter", "${datas[position]}")
            context.startActivity(intent)

            //영화 상세페이지 이동
        }
    }

}