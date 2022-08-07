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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.techtest.interfaces.ActivePaginationInterface
import com.example.techtest.interfaces.OpenMusicWebViewInterface
import com.example.techtest.view.resultsGrid
import com.example.techtest.model.Result
import com.example.techtest.network.ConnectionLiveData
import com.example.techtest.view.musicWebView
import com.example.techtest.view.loadingBar
import com.example.techtest.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity(), OpenMusicWebViewInterface, ActivePaginationInterface {
    //ViewModel Instance. It will be used to get and paginate the results to show, and to store the
    //state of the application.
    private lateinit var mainViewModel: MainViewModel

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get network connection
        var network = ConnectionLiveData(application)

        network.observe(this, { isConnected ->
            if (isConnected){
                Toast.makeText(this, "YOU ARE ONLINE", Toast.LENGTH_LONG).show()

                //If it's the first time that the app is connected than create initialize the viewModel
                //and display the results
                if(!this::mainViewModel.isInitialized){
                    //Initializing the ViewModel
                    mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

                    setContent {
                        //Call the Composable Function to show the title and to compose the LazyVerticalGrid
                        //This function will get as parameters the resultsList, of course, and two instance of
                        //the MainActivity that implements the function of two Interfaces. Thanks to these
                        //instances the LazyVerticalGrid will be able to perform the function needed to open the
                        //WebView, when an artWork is clicked, and to perform the pagination to get the following
                        //20 items.
                        musicResultsView(
                            liveResults = mainViewModel.liveResults,
                            openMusicWebViewInterface = this,
                            activePaginationInterface = this,
                            isLoading = mainViewModel.isLoading
                        )
                    }
                }
            }
            else{
                Toast.makeText(this, "YOU ARE OFFLINE", Toast.LENGTH_LONG).show()

                //If there's no connection and the viewModel has not been initialized yet, than we
                //can only display the title
                if(!this::mainViewModel.isInitialized){
                    setContent {
                        musicResultsView(
                            liveResults = emptyList(),
                            openMusicWebViewInterface = this,
                            activePaginationInterface = this,
                            isLoading = false
                        )
                    }
                }
            }
        })

    }



    //Interface Methods-----------------------------------------------------------------------------
    //OpenMusicWebViewInterface
    //This function will call a Composable Function to open a very very simple WebView
    override fun imageClicked(url: String) {
        setContent {
            musicWebView(url = url)
        }
    }

    //Active PaginationInterface
    //The last index has been reached, so the results list must be updated
    override fun lastIndexReached() {
        GlobalScope.launch {
            mainViewModel.pagination()
        }

        if(mainViewModel.liveResults.size < mainViewModel.resultsSize)
            Toast.makeText(this, "Loading more Items", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "All the items have been loaded", Toast.LENGTH_SHORT).show()
    }
}



@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun musicResultsView(
    liveResults: List<Result>?,
    openMusicWebViewInterface: OpenMusicWebViewInterface,
    activePaginationInterface: ActivePaginationInterface,
    isLoading: Boolean
){
    Column() {
        //Display the Title
        Text(
            text = "Play List",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp)
        )

        //Cool animation to add whenever the items are being loaded
        loadingBar(isDisplayed = isLoading)

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