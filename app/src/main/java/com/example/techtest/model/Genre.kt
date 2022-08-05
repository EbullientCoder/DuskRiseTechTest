package com.example.techtest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Genre(
    @SerializedName("genreId")
    val genreId: String, // 14
    @SerializedName("name")
    val name: String, // Pop
    @SerializedName("url")
    val url: String // https://itunes.apple.com/us/genre/id14
)