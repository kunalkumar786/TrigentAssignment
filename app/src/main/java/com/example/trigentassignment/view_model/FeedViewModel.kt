package com.example.trigentassignment.view_model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.trigentassignment.model.FeedModel
import com.example.trigentassignment.network.NetworkClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG:String="FeedViewModel"
     var feed_data: MutableLiveData<ArrayList<FeedModel>>? = null

    fun FeedViewModel(application: Application) {
    }

    fun getFeedData(): MutableLiveData<ArrayList<FeedModel>> {
        if (feed_data == null) {
            feed_data = MutableLiveData<ArrayList<FeedModel>>()
            callFeedApi()
        }
        return feed_data as MutableLiveData<ArrayList<FeedModel>>
    }

    fun callFeedApi() {
        NetworkClient.NetworkObject.getNetworkInstance()
        val networkClient= NetworkClient.NetworkObject.getApiClient()
        networkClient?.callAPIExecutor()?.enqueue(object:Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG,call.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val jsonObject = JSONObject(response.body()!!.string())
                    val jsonArray = jsonObject.getJSONArray("rows")
                    if (jsonArray != null && jsonArray.length() > 0) {
                        val feed_result: ArrayList<FeedModel> =
                            ArrayList<FeedModel>()
                        for (i in 0 until jsonArray.length()) {

                             var  feedModel:FeedModel?=null
                            //feedModel?.Header_title =
                            val result = jsonArray.getJSONObject(i)
                            //feedModel?.title = ()
                           // feedModel?.description =
                            //feedModel?.imageHref =
                           // if (feedModel != null) {
                                feed_result.add(FeedModel(result.getString("imageHref"),result.getString("title"),
                                    result.getString("description"),jsonObject.getString("title")))
                            //}
                        }
                        feed_data?.value = feed_result
                        if(feed_result.size>0) {

                        }else{
                            Toast.makeText(getApplication(),"No data received",Toast.LENGTH_LONG).show()

                        }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
    }
}
