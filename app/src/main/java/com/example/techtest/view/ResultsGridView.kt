package com.example.techtest.view


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.techtest.interfaces.OpenMusicWebViewInterface
import com.example.techtest.model.Result


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun resultsGrid(
    results: List<Result>,
    openMusicWebViewInterface: OpenMusicWebViewInterface
){
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        content = {
            items(results.size){ index ->
                musicContainer(
                    result = results[index],
                    onClick = {
                        //Callback the function that will open the WebView
                        openMusicWebViewInterface.imageClicked(results[index].url)
                    }
                )
            }
        }
    )
}