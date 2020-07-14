package com.example.trigentassignment.network

import com.example.trigentassignment.model.FeedModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

class NetworkClient private constructor() {

    interface APIClient {
        @GET("2iodh4vg0eortkl/facts.json")
        fun callAPIExecutor(): Call<ResponseBody>
    }

     object NetworkObject {
         private var retrofit: Retrofit? = null
         private var networkClient: NetworkClient? = null
         fun getNetworkInstance(): NetworkClient {
             if (networkClient == null) {
                 networkClient = NetworkClient()
                 initializeRetrofitLib()
             }
             return networkClient as NetworkClient
         }
         private fun initializeRetrofitLib() {
             val apiBaseURL = "https://dl.dropboxusercontent.com/s/"
             val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
             val client = OkHttpClient.Builder()
                 .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                 .connectTimeout(2, TimeUnit.MINUTES)
                 .writeTimeout(2, TimeUnit.MINUTES)
                 .readTimeout(2, TimeUnit.MINUTES)
                 .addInterceptor { chain ->
                     val requestBuilder = chain.request().newBuilder()
                     requestBuilder.header("Content-Type", "text/plain")
                     chain.proceed(requestBuilder.build())
                 }
                 .build()
             retrofit = Retrofit.Builder()
                 .baseUrl(apiBaseURL)
                 .client(client)
                 .addConverterFactory(GsonConverterFactory.create(gson))
                 .build()
         }
         fun getApiClient(): APIClient? {
             return retrofit?.create(APIClient::class.java)
         }
     }
     }

