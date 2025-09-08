package com.zip.zipandroid.utils.phonedate.contact


interface ZipContactListener {
    fun onContactFetched(zipContacts: Array<ZipContact?>?)
    fun onError(message: String?)
}