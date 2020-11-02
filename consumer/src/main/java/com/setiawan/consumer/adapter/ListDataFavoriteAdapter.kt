package com.setiawan.consumer.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.setiawan.consumer.R
import com.setiawan.consumer.repository.DataFavorite
import kotlinx.android.synthetic.main.rv_model.view.*

class ListDataFavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<ListDataFavoriteAdapter.ListFavViewHolder>() {

    var listFavorite = ArrayList<DataFavorite>()
        set(listFavorite) {
            if (listFavorite.size >0){
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

   inner class ListFavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data : DataFavorite){
            with(itemView){
                Glide.with(itemView.context)
                    .load(data.avatar)
                    .apply(RequestOptions().override(80,80))
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(user_image)

                name_id.text = data.username
                name.text = data.name
                tv_repos.text = resources.getString(R.string.repo,data.repository)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_model,parent,false)
        return ListFavViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListFavViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }
}