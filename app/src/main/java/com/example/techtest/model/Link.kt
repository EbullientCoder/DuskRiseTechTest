package com.example.techtest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Link(
    @SerializedName("self")
    val self: String // https://rss.applemarketingtools.com/api/v2/us/music/most-played/30/songs.json
)