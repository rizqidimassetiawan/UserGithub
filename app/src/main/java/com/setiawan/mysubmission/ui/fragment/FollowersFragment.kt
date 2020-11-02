package com.setiawan.mysubmission.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.setiawan.mysubmission.R
import com.setiawan.mysubmission.viewModel.FollowersViewModel
import com.setiawan.mysubmission.repository.DataUsers
import com.setiawan.mysubmission.ui.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    companion object{
        val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_DETAIL = "extra_detail"
    }

    private val listData : ArrayList<DataUsers> = ArrayList()
    private lateinit var adapter : UserAdapter
    private lateinit var followersViewModel : FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter(listData)
        followersViewModel = ViewModelProvider(
            this,ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        val dataUsers:DataUsers? = activity!!.intent.getParcelableExtra(EXTRA_DETAIL)
        config()

        followersViewModel.getDataAPI(activity!!.applicationContext,dataUsers?.username.toString())
        showProgresBar(true)
        setViewModel()


    }

    private fun config(){
        rv_followers.setHasFixedSize(true)
        rv_followers.layoutManager = LinearLayoutManager(activity)
        rv_followers.adapter = adapter
    }

    private fun showProgresBar(state : Boolean){
        if (state){
            progresBar.visibility = View.VISIBLE
        }else{
            progresBar.visibility = View.GONE
        }
    }

    private fun setLabel(state: Boolean){
        if (state){
            img_followers.visibility = View.VISIBLE
            tv_title_followers.visibility = View.VISIBLE
            tv_dsc_followers.visibility = View.VISIBLE
        }else{
            img_followers.visibility = View.GONE
            tv_title_followers.visibility = View.GONE
            tv_dsc_followers.visibility = View.GONE
        }
    }
    private fun setViewModel(){
        followersViewModel.getListFollowers().observe(activity!!,Observer{ listFollowing->
            if (listFollowing!=null && listFollowing.isNotEmpty()){
                adapter.setData(listFollowing)
                showProgresBar(false)
                setLabel(false)
            }else{
             setLabel(true)
            }
            showProgresBar(false)
        })
    }
}
