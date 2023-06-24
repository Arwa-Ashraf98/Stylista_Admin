package com.mad43.staylistaadmin.utils

sealed class ResourceState<out T>{
    class Success<T>(val data: T) : ResourceState<T>()
    class Failure(val msg: Throwable) : ResourceState<Nothing>()
    object Loading : ResourceState<Nothing>()
}
