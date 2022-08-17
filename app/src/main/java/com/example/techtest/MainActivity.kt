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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity(), OpenMusicWebViewInterface, ActivePaginationInterface {
    //ViewModel Instance. It will be used to get and paginate the results to show, and to store the
    //state of the application.
    private lateinit var mainViewModel: MainViewModel

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //View Model Instance
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Get network connection
        val network = ConnectionLiveData(this)


        //Observers---------------------------------------------------------------------------------
        //Observe network connectivity status
        network.observe(this, {connected ->
            if(connected){
                //Notify the user
                Toast.makeText(this, "You are online", Toast.LENGTH_SHORT).show()

                //Call the viewModel method to fetch the results
                mainViewModel.networkConnection()
            }
        })


        //Observe the Evolution of the Results List
        mainViewModel.results.observe(this, { results ->
            setContent {
                musicResultsView(
                    liveResults = results,
                    openMusicWebViewInterface = this,
                    activePaginationInterface = this,
                    isLoading = mainViewModel.isLoading
                )
            }
        })
    }



    //Interface Methods-----------------------------------------------------------------------------
    //OpenMusicWebViewInterface
    //This function will call a Composable Function to open a very very simple WebView
    override fun imageClicked(url: String) {
        //Notify the User that the song has been clicked
        Toast.makeText(applicationContext, "Opening the WebView", Toast.LENGTH_SHORT).show()

        //L

        GlobalScope.launch {
            try {
                //Connection Check
                val checkUrl = URL(url)
                val connection: HttpURLConnection = checkUrl.openConnection() as HttpURLConnection

                //Reachable url
                if(connection.responseCode == 200)
                    setContent { musicWebView(url = url) }
                else
                    runOnUiThread{
                        Toast.makeText(applicationContext, "ERROR: Can't open the WebView cause the url is unreachable", Toast.LENGTH_LONG).show()
                    }
            }
            //No Internet
            catch (e: Exception){
                runOnUiThread{
                    Toast.makeText(applicationContext, "ERROR: Can't open the connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    //Active PaginationInterface
    //The last index has been reached, so the results list must be updated
    override fun lastIndexReached(lastIndex: Int) {
        //Check if it's ok to call pagination function
        if(lastIndex < mainViewModel.resultsSize){
            //Notify the User
            Toast.makeText(this, "Loading more Items", Toast.LENGTH_SHORT).show()

            //Call the Pagination function
            lifecycleScope.launch {
                mainViewModel.pagination()
            }
        }
        else
            //Notify the User
            Toast.makeText(this, "All the items have been loaded", Toast.LENGTH_SHORT).show()
    }
}




@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun musicResultsView(
    liveResults: List<Result>,
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
        resultsGrid(
            liveResults = liveResults,
            openMusicWebViewInterface = openMusicWebViewInterface,
            activePaginationInterface = activePaginationInterface
        )
    }
}