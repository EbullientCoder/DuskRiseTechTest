package com.example.techtest.interfaces

interface ActivePaginationInterface {
    //Function called when the last index of the Results list is reached
    fun lastIndexReached(lastIndex: Int)
}