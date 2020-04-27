package com.example.playlist.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlist.R
import com.example.quipper.model.entity.Course
import kotlinx.android.synthetic.main.item_adapter.view.*

class MainAdapter(private val playlist: MutableList<Course> = mutableListOf()): RecyclerView.Adapter<MainAdapter.PlaylistViewHolder>() {

    lateinit var onClickListener: OnClickListener

    fun setItems(newData: List<Course>) {
        playlist.clear()
        playlist.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnclickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(course: Course)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int = playlist.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(playlist[position])
        }
    }

    inner class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(course : Course){
            itemView.title.text = course.title
            itemView.presenter.text = course.presenter_name
            Glide.with(itemView.context)
                .load(course.thumbnail_url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(itemView.imageView)

        }
    }
}
