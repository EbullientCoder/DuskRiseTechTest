package com.example.techtest.viewmodel


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techtest.model.Result
import com.example.techtest.network.ConnectionLiveData
import com.example.techtest.network.ServiceProvider
import kotlinx.coroutines.*



class MainViewModel(): ViewModel() {
    //Here I have a big problem: I've never worked with pagination before.
    //I understand that the pagination is used to get only a fixed number of items when the user
    //scrolls down. In that way the App will not download all the items together, and this will
    //prevent crashes or slow responses if there are a lot of data to download. The problem is
    //that I can't specify the page I want to access, cause this API doesn't allow it... the only
    //idea that I have is to 'manually' write in the url the range of the items that I want to download
    //every time. For example:
    //First Scroll: download items from index 0 to 9
    //Second Scroll: download items from index 10 to 19 etc.

    //The problem is that I've never seen something like that and I don't know if it's possible.
    //Plus, data stored in Json file's array are not sorted when downloaded, so even if I manage to
    //download 10 items at time, those items will be random. They will not be sorted.
    //PLUS, the deadline is tomorrow.

    //Literally 3 hours later-----------------------------------------------------------------

    //Only now I realize that the pagination is a technique used by the recyclerView (lazyView in
    //Jetpack Compose) to display small groups of items, instead of all the items together,
    //to prevent lag...
    //I don't know if it's a good idea to share my flow of thoughts this time...
    //At least I hope you will appreciate my stubbornness and willpower.



    //The ServiceProvider will provide the functions needed to make a connection a get the Json file
    private lateinit var serviceProvider: ServiceProvider

    //Items to copy in the live results
    private var currentItems = 20

    //Results List
    private var _results: List<Result> = listOf()
    var results: MutableLiveData<List<Result>> = MutableLiveData(listOf())
    var resultsSize: Int = 0

    //Used to notify the Loading Bar when the Results are being fetched by the ServiceProvider
    var isLoading: Boolean by mutableStateOf(true)




    //Function to call when there's internet connection
    fun networkConnection(){
        //A Network Call is needed to fetch the Json File, and a Network Call can't be executed on
        //the main thread cause it could block the UI
        viewModelScope.launch(Dispatchers.IO) {
            //Create the ServiceProvider Instance
            serviceProvider = ServiceProvider.getInstance()!!

            //Here will be stored all the results fetched by the ServiceProvider, but the
            //LazyVerticalGrid cannot access them all together, or there will be lag problems,
            //especially because of the artworks to show (even if they are managed asynchronously)
            _results = serviceProvider.fetchResults()
            resultsSize = _results.size

            //Notify the Loading bar to stop it's animation
            isLoading = false

            //Call the pagination function to show the first 20 results
            pagination()
        }
    }


    //I'm not really proud of how I managed the pagination, but I don't have any more time to polish
    //it (because of the deadline). It's ugly but it works so...
    suspend fun pagination(){
        //The if statement will block the copy of the results inside the variable that will be
        //observed by the LazyVerticalGrid. The idea is to create a sort of buffer to get chunks of
        //20 results per time.

        //Simulating a slow response when paginating
        if(results.value!!.isNotEmpty()) delay(2000)

        //Assign the new items to the List
        withContext(Dispatchers.Main){
            results.value = if(currentItems >= _results.size) _results
            else _results.slice(0 until currentItems)
        }

        //Update the next items
        currentItems += 20
    }
}
