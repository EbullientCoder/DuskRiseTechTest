package com.example.techtest.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.techtest.R
import com.example.techtest.model.Result


@ExperimentalMaterialApi
@Composable
fun musicContainer(
    result: Result,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 5.dp, end = 5.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        Column {
            //Check if the result has an Image Url
            result.artworkUrl100?.let {
                //Display the Image
                result.artworkUrl100?.let{ url ->
                    val artwork = loadImage(url = url, defaultImage = R.drawable.image_not_found).value

                    artwork?.let {
                        Image(
                            bitmap = artwork.asImageBitmap(),
                            contentDescription = "Image not Found",
                            modifier = Modifier
                                //.height(100.dp)
                                //.width(100.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(15.dp))
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            //Check if the result has an Artist Name
            result.artistName?.let { it ->
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(Color.Transparent)
                        .padding(horizontal = 5.dp)
                ){
                    //Even if the majority of the Results have the Artist Name, the Song Name and the
                    //Release Date, there are some of them that don't have this field (it happened),
                    //so a NullPointerException is thrown. To evade this problem an "elvis operator"
                    //is used.

                    //Artist Name
                    Text(
                        text = result.artistName ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(top = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    //Song Name
                    Text(
                        text = result.name ?: "",
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    //Release Date
                    Text(
                        text = result.releaseDate ?: "",
                        color = Color.Gray,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}