package com.example.techtest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.techtest.interfaces.ActivePaginationInterface
import com.example.techtest.interfaces.OpenMusicWebViewInterface
import com.example.techtest.view.resultsGrid
import com.example.techtest.model.Result
import com.example.techtest.view.musicWebView
import com.example.techtest.viewmodel.MainViewModel


class MainActivity : ComponentActivity(), OpenMusicWebViewInterface, ActivePaginationInterface {

    private lateinit var mainViewModel: MainViewModel

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Instantiate the ViewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        setContent{
            mainViewModel.pagination()

            musicResultsView(
                liveResults = mainViewModel.liveResults,
                openMusicWebViewInterface = this,
                activePaginationInterface = this)
        }
    }

    //Interface Method
    //Open the WebView of the passed url
    override fun imageClicked(url: String) {
        setContent {
            musicWebView(url = url)
        }
    }

    //The last index has been reached, so the results list must be updated
    override fun lastIndexReached() {
        mainViewModel.updateItems()
        mainViewModel.pagination()

        //Toast.makeText(this, "Last Index Reached", Toast.LENGTH_SHORT).show()
    }
}



@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun musicResultsView(
    //liveResults: LiveData<List<Result>>,
    liveResults: List<Result>?,
    openMusicWebViewInterface: OpenMusicWebViewInterface,
    activePaginationInterface: ActivePaginationInterface
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
        liveResults?.let {
            resultsGrid(
                liveResults = liveResults,
                openMusicWebViewInterface = openMusicWebViewInterface,
                activePaginationInterface = activePaginationInterface
            )
        }
    }
}