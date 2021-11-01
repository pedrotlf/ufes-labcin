package br.com.ufes.pedrotlf.pad.data

sealed class Resource<out R> {

    data class Success<out T>(val data: T): Resource<T>()
    data class Failure(val exception: Throwable): Resource<Nothing>()
    object Loading: Resource<Nothing>()
}