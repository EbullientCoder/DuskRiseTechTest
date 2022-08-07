package com.example.techtest.network

import com.example.techtest.model.MusicFeed
import com.example.techtest.model.Result
import com.google.gson.GsonBuilder
import okhttp3.*


class ServiceProvider private constructor(){

    //Used to Access to the Specified API
    //https://rss.applemarketingtools.com/api/v2/us/music/most-played/30/songs.json
    private val url = "https://rss.applemarketingtools.com/api/v2/us/music/most-played/100/songs.json"

    companion object{
        //Singleton Instance
        @Volatile
        private var INSTANCE : ServiceProvider? = null

        //Returns the Singleton Instance
        fun getInstance(): ServiceProvider? {
            //Manage the Multi Threads Access
            synchronized(this){
                if(INSTANCE == null) INSTANCE = ServiceProvider()

                return INSTANCE
            }
        }
    }

    //Function to fetch all the data from the Json file
    //In this function are used OkHttp3 and Gson
    fun fetchResults(): List<Result>?{
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        val body = client.newCall(request).execute().body?.string()
        val gson = GsonBuilder().create()
        val musicFeed = gson.fromJson(body, MusicFeed::class.java)

        return musicFeed?.feed?.results
    }
}

