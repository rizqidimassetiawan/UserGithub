package com.setiawan.mysubmission.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.setiawan.mysubmission.ui.DetailActivity
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.repository.DataUsers
import kotlinx.android.synthetic.main.rv_model.view.*


class UserAdapter(private val users : ArrayList<DataUsers>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    fun setData(items : ArrayList<DataUsers>){
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataUsers: DataUsers){
            with(itemView){
                Glide.with(itemView.context)
                    .load(dataUsers.avatar)
                    .apply(RequestOptions().override(84,84))
                    .into(user_image)

                name_id.text = dataUsers.username
                name.text = dataUsers.name
                tv_repos.text = itemView.context.getString(R.string.repo,dataUsers.repository)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_model,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])

        val data = users[position]
        holder.itemView.setOnClickListener{
           val dataUserIntent = DataUsers(
               data.name,
               data.username,
               data.avatar,
               data.company,
               data.location,
               data.repository,
               data.followers,
               data.following,
               data.bio
           )
            val mIntent = Intent(it.context, DetailActivity::class.java)
            mIntent.putExtra(DetailActivity.EXTRA_DETAIL,dataUserIntent)
            it.context.startActivity(mIntent)
        }
    }
}