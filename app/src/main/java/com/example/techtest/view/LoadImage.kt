package com.example.techtest.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@SuppressLint("UnrememberedMutableState")
@Composable
fun loadImage(url: String, @DrawableRes defaultImage: Int): MutableState<Bitmap?>{
    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

    //Download the Default Image to show when the URL image is not available. The IMG will be stored
    //into a bitmap object
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(defaultImage)
        .into(object: CustomTarget<Bitmap?>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                //GG
            }
        })

    //Download the Real Image and store it inside a bitmap object
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(url)
        .into(object: CustomTarget<Bitmap?>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                //GG
            }
        })

    //This is a mutable state, so even if the real image will be downloaded and stored asynchronously
    //the state will be updated immediately
    return bitmapState
}