package com.zip.zipandroid.utils.phonedate.contact

import com.zip.zipandroid.utils.phonedate.contact.Contact


interface ContactListener {
    fun onContactFetched(contacts: Array<Contact?>?)
    fun onError(message: String?)
}