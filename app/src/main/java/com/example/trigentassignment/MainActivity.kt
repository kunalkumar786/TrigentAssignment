package com.example.trigentassignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.trigentassignment.adapter.MyFeedAdapter
import com.example.trigentassignment.database.DatabaseHandler
import com.example.trigentassignment.model.FeedModel
import com.example.trigentassignment.util.Utility
import com.example.trigentassignment.view_model.FeedViewModel
import java.util.*

class MainActivity : AppCompatActivity() {


    var pullToRefresh: SwipeRefreshLayout? = null


    var feed_list: RecyclerView? = null

    var feedViewModel: FeedViewModel? = null
    var header_title: String? = null
    var myFeedAdapter: MyFeedAdapter? = null
    var initial_data = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pullToRefresh=findViewById(R.id.pullToRefresh)
        feed_list=findViewById(R.id.feed_list)

        feedViewModel = ViewModelProviders.of(this)[FeedViewModel::class.java]
        val databaseHandler= DatabaseHandler(this)
        val feedData=databaseHandler.viewFeed()

        val utility=Utility(this)


        if(utility.hasActiveInternetConnection(this)&&feedData.size==0) {
            feedViewModel?.getFeedData()
                ?.observe(this, object : Observer<ArrayList<FeedModel>> {
                    @SuppressLint("WrongConstant")
                    override fun onChanged(feedModels: ArrayList<FeedModel>) {
                        loadData(feedModels)
                    }
                })

        }else{
            if(feedData!=null&&feedData.size>0){
                loadData(feedData as ArrayList<FeedModel>)
                
            }else{
            Toast.makeText(this,"No data Available",Toast.LENGTH_LONG).show()
            }
        }

        pullToRefresh?.setOnRefreshListener {
            refreshData()
            pullToRefresh?.isRefreshing = false
        }


    }
    private fun refreshData() {
        feedViewModel?.getFeedData()
            ?.observe(this,
                Observer { feedModels ->
                    myFeedAdapter = MyFeedAdapter(this@MainActivity, feedModels)
                    val layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                    feed_list?.adapter=myFeedAdapter
                    feed_list?.layoutManager=layoutManager
                })
    }

    @SuppressLint("WrongConstant")
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
            myFeedAdapter = MyFeedAdapter(this@MainActivity, feedModels)
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