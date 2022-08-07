package com.example.techtest.view


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.techtest.interfaces.ActivePaginationInterface
import com.example.techtest.interfaces.OpenMusicWebViewInterface
import com.example.techtest.model.Result
import kotlinx.coroutines.*


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun resultsGrid(
    liveResults: List<Result>?,
    openMusicWebViewInterface: OpenMusicWebViewInterface,
    activePaginationInterface: ActivePaginationInterface
){
    //I tried a lot of things to make a fully function pagination service. I didn't expect it but it
    //was the most challenging part.
    //With a RecyclerView and an Adapter there are multiple solutions that could have resolved this
    //problem, but with Jetpack Compose the documentation about this topic is a little scarce.
    //I found an interesting library, Paging3, but I didn't have the time to master it, so I opted
    //for a more naive solution.

    //I even tried with LazyState to get the last index displayed, but it caused strange problems
    //of flickering...

    //val listState = rememberLazyListState()

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        content = {
            liveResults?.let {
                itemsIndexed(it){ index, _ ->

                    //Callback the function that will get the other 20 results
                    if(index == liveResults.lastIndex){
                        activePaginationInterface.lastIndexReached()
                    }

                    musicContainer(
                        result = liveResults[index],
                        onClick = {
                            //Callback the function that will open the WebView
                            openMusicWebViewInterface.imageClicked(liveResults[index].url)
                        }
                    )
                }
            }
        }
        //state = listState
    )

    //Flickering Artworks problems
    //if(listState.layoutInfo.totalItemsCount == listState.layoutInfo.visibleItemsInfo)
}



