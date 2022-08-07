package com.example.techtest.viewmodel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techtest.model.Result
import com.example.techtest.network.ServiceProvider
import kotlinx.coroutines.*


class MainViewModel: ViewModel() {
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


    //Service Provider
    private var serviceProvider: ServiceProvider? = null
    //Items to copy in the live results
    private var currentItems = 20

    //I could have used the LiveData, but the Jetpack Compose Documentation says that the 'mutableState'
    //is designed to work efficiently with the Composable functions
    private lateinit var results: Deferred<List<Result>?>
    //var liveResults: List<Result>? by mutableStateOf(listOf())
    var liveResults: MutableList<Result>? by mutableStateOf(mutableListOf())

    //Notify the Circular Loading Bar
    var isLoading: Boolean by mutableStateOf(false)


    init {
        //The ServiceProvider will provide the functions needed to make a connection a get the Json file
        serviceProvider = ServiceProvider.getInstance()

        //A Network Call is needed to fetch the Json File, and a Network Call can't be executed on
        //the main thread cause it could block the UI
        viewModelScope.launch(Dispatchers.IO) {
            //Here will be stored all the results fetched by the ServiceProvider, but the
            //LazyVerticalGrid cannot access them all together, or there will be lag problems,
            //especially because of the artworks to show (even if they are managed asynchronously)
            //results = serviceProvider?.fetchResults()
            results =  async { serviceProvider?.fetchResults() }
        }
    }


    //I'm not really proud of how I managed the pagination, but I don't have any more time to polish
    //it (because of the deadline). It's ugly but it works so...
    fun pagination(){
        //Simulate slow response
        viewModelScope.launch(Dispatchers.Default) {
            //The if statement will block the copy of the results inside the variable that will be
            //observed by the LazyVerticalGrid. The idea is to create a sort of buffer to get chunks of
            //20 results per time.
            if(liveResults?.size!! < results.await()?.size!!){

                //Simulating a slow response when paginating
                if(!liveResults.isNullOrEmpty())
                    delay(3000)

                //Assign the new items to the List
                liveResults = if(currentItems >= results.await()?.size!!)
                    results.await()?.slice(0 until results.await()?.size!!) as MutableList<Result>?
                else
                    results.await()?.slice(0 until currentItems) as MutableList<Result>?

                //var i = liveResults?.size ?: 0
                //while(i < currentItems && (i + currentItems) < results.await()?.size!!)
                //    liveResults?.add(results.await()!![i])

                Log.e(String(), "Coroutine\n " +
                        "liveResults size: ${liveResults?.size}\n " +
                        "results size: ${results.await()!!.size}\n")
            }
        }

        Log.e(String(), "Called")
    }

    //Update the currentItems to show 20 more results when needed.
    fun updateItems(){
        currentItems += 20
    }
}
