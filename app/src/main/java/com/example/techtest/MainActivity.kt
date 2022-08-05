package com.example.techtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.techtest.interfaces.OpenMusicWebViewInterface
import com.example.techtest.view.resultsGrid
import com.example.techtest.model.Result
import com.example.techtest.view.musicWebView
import com.example.techtest.viewmodel.MainViewModel


class MainActivity : ComponentActivity(), OpenMusicWebViewInterface {

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel by viewModels<MainViewModel>()

        setContent{
            mainViewModel.getResults()

            musicResultsView(results = mainViewModel.resultsList, this)
        }
    }


    //Interface Method
    //Open the WebView of the passed url
    override fun imageClicked(url: String) {
        setContent {
            musicWebView(url = url)
        }
    }
}



@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun musicResultsView(
    results: List<Result>?,
    openMusicWebViewInterface: OpenMusicWebViewInterface
){
    Column() {
        //Title
        Text(
            text = "Play List",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp)
        )

        //LazyVerticalGrid to display all the results of the Music Feed
        results?.let {
            resultsGrid(results = results, openMusicWebViewInterface = openMusicWebViewInterface)
        }
    }
}