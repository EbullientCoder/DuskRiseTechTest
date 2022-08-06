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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun resultsGrid(
    //liveResults: LiveData<List<Result>>,
    liveResults: List<Result>?,
    openMusicWebViewInterface: OpenMusicWebViewInterface,
    activePaginationInterface: ActivePaginationInterface
){
    //val results by liveResults.observeAsState(initial = emptyList())
    val listState = rememberLazyListState()

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        content = {
            liveResults?.let {
                itemsIndexed(it){ index, _ ->

                    if(index == liveResults.lastIndex){
                        GlobalScope.launch {
                            delay(5000)
                            activePaginationInterface.lastIndexReached()
                        }
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
        },
        state = listState
    )


}

/*
@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 1,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        /*snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                onLoadMore()
            }*/
        snapshotFlow { loadMore.value }
            .filter{it}
            .collect{onLoadMore()}
    }
}
*/

