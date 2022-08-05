package com.example.techtest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Feed(
    @SerializedName("author")
    val author: Author,
    @SerializedName("copyright")
    val copyright: String, // Copyright © 2022 Apple Inc. All rights reserved.
    @SerializedName("country")
    val country: String, // us
    @SerializedName("icon")
    val icon: String, // https://www.apple.com/favicon.ico
    @SerializedName("id")
    val id: String, // https://rss.applemarketingtools.com/api/v2/us/music/most-played/30/songs.json
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("title")
    val title: String, // Top Songs
    @SerializedName("updated")
    val updated: String // Tue, 2 Aug 2022 18:03:09 +0000
)