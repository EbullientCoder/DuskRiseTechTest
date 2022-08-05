package com.example.techtest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Author(
    @SerializedName("name")
    val name: String, // Apple
    @SerializedName("url")
    val url: String // https://www.apple.com/
)