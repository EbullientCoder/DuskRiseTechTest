package com.example.techtest.network

import android.util.Log
import com.example.techtest.model.MusicFeed
import com.example.techtest.model.Result
import com.google.gson.GsonBuilder
import okhttp3.*
import java.lang.Exception
import java.lang.IllegalArgumentException


class ServiceProvider private constructor(){

    //Used to Access to the Specified API
    //https://rss.applemarketingtools.com/api/v2/us/music/most-played/100/songs.json
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
    fun fetchResults(): List<Result>{
        var results: List<Result> = ArrayList()

        //If there's no internet connection the request will fail
        try {
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            val gson = GsonBuilder().create()

            val body = client.newCall(request).execute().body?.string()
            val musicFeed = gson.fromJson(body, MusicFeed::class.java)

            results = musicFeed.feed.results
        }
        catch (e: IllegalArgumentException){
            e.message?.let { Log.e(String(), it) }
        }
        catch (e: Exception){
            Log.e(String(), "ServiceProvider error: Request Failed")
        }

        return results
    }
}

