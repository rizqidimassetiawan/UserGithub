package com.setiawan.mysubmission.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.repository.DataFavorite
import com.setiawan.mysubmission.repository.DataUsers
import com.setiawan.mysubmission.ui.DetailActivity
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
                    .error(R.drawable.ic_account_circle_black)
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

        val listItem = listFavorite[position]
        holder.itemView.setOnClickListener {
            val data = DataUsers(
                listItem.name,
                listItem.username,
                listItem.avatar,
                listItem.company,
                listItem.location,
                listItem.repository,
                listItem.followers,
                listItem.following,
                listItem.bio
            )
            val move = Intent(it.context,DetailActivity::class.java)
            move.putExtra(DetailActivity.EXTRA_DETAIL,data)
            it.context.startActivity(move)

        }

    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }
}