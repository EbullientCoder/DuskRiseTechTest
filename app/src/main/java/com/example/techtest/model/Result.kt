package com.example.techtest.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Result(
    @SerializedName("artistId")
    val artistId: String, // 1419227
    @SerializedName("artistName")
    val artistName: String, // Beyoncé
    @SerializedName("artistUrl")
    val artistUrl: String, // https://music.apple.com/us/artist/beyonc%C3%A9/1419227
    @SerializedName("artworkUrl100")
    val artworkUrl100: String, // https://is5-ssl.mzstatic.com/image/thumb/Music112/v4/05/05/f3/0505f338-9873-feb4-af7f-27a470405e5f/196589246974.jpg/100x100bb.jpg
    @SerializedName("contentAdvisoryRating")
    val contentAdvisoryRating: String, // Explict
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("id")
    val id: String, // 1630005848
    @SerializedName("kind")
    val kind: String, // songs
    @SerializedName("name")
    val name: String, // ALIEN SUPERSTAR
    @SerializedName("releaseDate")
    val releaseDate: String, // 2022-07-29
    @SerializedName("url")
    val url: String // https://music.apple.com/us/album/alien-superstar/1630005298?i=1630005848
)