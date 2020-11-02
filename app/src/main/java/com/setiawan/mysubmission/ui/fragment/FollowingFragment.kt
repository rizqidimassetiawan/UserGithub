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
import com.setiawan.mysubmission.viewModel.FollowingViewModel
import com.setiawan.mysubmission.repository.DataUsers
import com.setiawan.mysubmission.ui.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_following.progresBar

class FollowingFragment : Fragment() {

    companion object {
        val TAG = FollowingFragment::class.java.simpleName
        const val EXTRA_DETAIL = "extra_detail"
    }

    private val listData: ArrayList<DataUsers> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter(listData)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        val dataUsers : DataUsers? = activity!!.intent.getParcelableExtra(EXTRA_DETAIL)
        config()

        followingViewModel.getDataAPI(activity!!.applicationContext,dataUsers?.username.toString())
        showProgresBar(true)
        viewModel()

    }

    private fun viewModel(){
        followingViewModel.getListFollowing().observe(activity!!, Observer { listFollowing->
            if (listFollowing != null && listFollowing.isNotEmpty()){
                adapter.setData(listFollowing)
                showProgresBar(true)
                setLabel(false)
            }else{
                setLabel(true)
            }
            showProgresBar(false)
        })
    }
    private fun config() {
        rv_following.setHasFixedSize(true)
        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = adapter
    }
    private fun setLabel(state: Boolean){
        if (state){
            img_following.visibility = View.VISIBLE
            tv_title_following.visibility = View.VISIBLE
            tv_dsc_following.visibility = View.VISIBLE
        }else{
            img_following.visibility = View.GONE
            tv_title_following.visibility = View.GONE
            tv_dsc_following.visibility = View.GONE
        }
    }
    private fun showProgresBar(state: Boolean) {
        if (state) {
            progresBar.visibility = View.VISIBLE
        } else {
            progresBar.visibility = View.GONE
        }
    }
}