package com.example.techtest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class MusicFeed(
    @SerializedName("feed")
    val feed: Feed
)