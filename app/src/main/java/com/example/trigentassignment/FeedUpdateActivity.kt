package com.example.trigentassignment

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anupcowkur.reservoir.Reservoir
import com.anupcowkur.reservoir.ReservoirGetCallback
import com.example.trigentassignment.adapter.MyFeedAdapter

import com.example.trigentassignment.model.FeedModel
import com.example.trigentassignment.util.Utility
import com.example.trigentassignment.view_model.Cache_Key
import com.example.trigentassignment.view_model.FeedViewModel
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList


class FeedUpdateActivity : AppCompatActivity() {
    var pullToRefresh: SwipeRefreshLayout? = null
    var feed_list: RecyclerView? = null
    var feedViewModel: FeedViewModel? = null
    var header_title: String? = null
    var myFeedAdapter: MyFeedAdapter? = null
    var initial_data = 4
   private lateinit var feedModel:ArrayList<FeedModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pullToRefresh=findViewById(R.id.pullToRefresh)
        feed_list=findViewById(R.id.feed_list)
       feedViewModel = ViewModelProviders.of(this)[FeedViewModel::class.java]
        val utility=Utility(this)
         setFeedata(utility)


        pullToRefresh?.setOnRefreshListener {
            refreshData(utility)
            pullToRefresh?.isRefreshing = false
        }
    }

    private fun setFeedata(utility: Utility) {

        when{
            utility.hasActiveInternetConnection(this)&&loadDataFromCache(false).size==0->{
                networkRequest()
            }
            loadDataFromCache(false)!=null&&loadDataFromCache(false).size>0->{
                loadDataFromCache(false)
            }
            utility.hasActiveInternetConnection(this)!=true->{
                utility.showNetworkDialog()
            }
        }
    }

    private fun networkRequest(){
        feedViewModel?.getFeedData()
            ?.observe(this, object : Observer<ArrayList<FeedModel>> {
                override fun onChanged(feedModels: ArrayList<FeedModel>) {
                    feedModel=feedModels
                    loadData(feedModel)
                }
            })

    }


    private fun loadDataFromCache(loadAll:Boolean):ArrayList<FeedModel>{
        feedModel= ArrayList<FeedModel>()
        val resultType: Type = object : TypeToken<ArrayList<FeedModel>>() {}.type
        Reservoir.getAsync(
            Cache_Key,
            resultType,
            object : ReservoirGetCallback<ArrayList<FeedModel>> {
                override fun onSuccess(strings:ArrayList<FeedModel>) {
                    //success
                    feedModel=strings
                    if(loadAll) {
                        myFeedAdapter = MyFeedAdapter(this@FeedUpdateActivity, feedModel)
                        val layoutManager =
                            LinearLayoutManager(this@FeedUpdateActivity, LinearLayoutManager.VERTICAL, false)
                        feed_list?.adapter = myFeedAdapter
                        feed_list?.layoutManager = layoutManager
                    }else{
                        loadData(feedModel)
                    }
                    }

                override fun onFailure(e: Exception) {
                e.printStackTrace()
                }
            })
    return feedModel
    }


    private fun refreshData(utility: Utility) {
     if(utility.hasActiveInternetConnection(this)&&loadDataFromCache(true).size==0) {
            feedViewModel?.getFeedData()
                ?.observe(this,
                    Observer { feedModels ->
                       myFeedAdapter = MyFeedAdapter(this@FeedUpdateActivity, feedModels)
                        val layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        feed_list?.adapter = myFeedAdapter
                        feed_list?.layoutManager = layoutManager
                    })
        }else{
            loadDataFromCache(true)
        }
    }

    fun loadData(feedModels: ArrayList<FeedModel>) {
        val feeddata = ArrayList<FeedModel>()
        var index_less = false
        for (i in 0..feedModels!!.size) {
            if (feedModels.size > initial_data && i <= initial_data) {
                header_title = feedModels.get(i)?.Header_title
                feeddata.add(feedModels.get(i)!!)
            }
            if (feedModels.size < initial_data) {
                index_less = true
            }
        }
        if (header_title != null) {
            supportActionBar?.setTitle(header_title)
        } else {
            supportActionBar?.setTitle(getString(R.string.app_name))
        }
        if (index_less) {
            myFeedAdapter = MyFeedAdapter(this@FeedUpdateActivity, feedModels)
            val layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            feed_list?.setAdapter(myFeedAdapter)
            feed_list?.layoutManager=layoutManager
        } else {
            Log.e("MainActivity",""+feed_list);
            feed_list?.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
            val  myFeedAdapter = MyFeedAdapter(this, feeddata)
             feed_list?.adapter=myFeedAdapter

        }
    }


}