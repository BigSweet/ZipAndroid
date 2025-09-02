package com.zip.zipandroid.utils.phonedate.contact

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import android.text.TextUtils
import androidx.core.content.PermissionChecker
import com.zip.zipandroid.utils.phonedate.contact.Contact.ContactItem
import java.util.*

class ContactsProvider(private val context: Context) {
    private var listeners: ArrayList<ContactListener>
    private var isFetching = false
    @SuppressLint("WrongConstant")
    fun fetchAllContacts(list: Array<ContactListener>) {
        synchronized(this) {
            for (listener in list) {
                if (!listeners.contains(listener)) {
                    listeners.add(listener)
                }
            }
            if (isFetching) {
                return
            }
            isFetching = true
        }
        if (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            doFetchData()
        } else {
            onFinishedWithFailure("permission denied")
        }
    }

    private fun doFetchData() {
        Thread(Runnable {
            try {
                val contacts = fetchContacts()
                onFinishedWithContacts(contacts)
            } catch (e: Exception) {
                e.printStackTrace()
                onFinishedWithFailure(e.message)
            }
        }).start()
    }

    private fun syncGetListenersAndClear(): ArrayList<ContactListener> {
        var list: ArrayList<ContactListener>
        synchronized(this) {
            list = listeners
            listeners = ArrayList()
            isFetching = false
        }
        return list
    }

    private fun onFinishedWithContacts(contacts: Array<Contact?>) {
        val listenerList = syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onContactFetched(contacts)
            }
        }
    }

    private fun onFinishedWithFailure(message: String?) {
        val listenerList = syncGetListenersAndClear()
        if (listenerList != null && listenerList.size > 0) {
            for (i in listenerList.indices) {
                listenerList[i].onError(message)
            }
        }
    }

    private fun fetchContacts(): Array<Contact?> {
        var everyoneElse: Map<String?, Contact?>
        run {
            val cursor = this.context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                COLUMN_PROJECTION.toTypedArray(),
                ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?",
                arrayOf(
                    Email.CONTENT_ITEM_TYPE,
                    Phone.CONTENT_ITEM_TYPE,
                    StructuredName.CONTENT_ITEM_TYPE,
                    Organization.CONTENT_ITEM_TYPE,
                    StructuredPostal.CONTENT_ITEM_TYPE
                ),
                null
            )
            everyoneElse = try {
                loadContactsFrom(cursor)
            } finally {
                cursor?.close()
            }
        }
        val contacts =
            ArrayList(everyoneElse.values)
        val arrayContacts = arrayOfNulls<Contact>(contacts.size)
        return contacts.toArray(arrayContacts)
    }

    private fun loadContactsFrom(cursor: Cursor?): Map<String?, Contact?> {
        val map: MutableMap<String?, Contact?> =
            LinkedHashMap()
        while (cursor != null && cursor.moveToNext()) {
            val columnIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
            var contactId: String?
            if (columnIndex != -1) {
                contactId = cursor.getString(columnIndex)
            } else {
                //todo - double check this, it may not be necessary any more
                contactId = (-1).toString() //no contact id for 'ME' user
            }
            if (!map.containsKey(contactId)) {
                map[contactId] = Contact()
            }
            val contact = map[contactId]
            val mimeType =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
            val name =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(contact!!.displayName)) {
                contact.displayName = name
            }
            if (TextUtils.isEmpty(contact!!.photoURI)) {
                val rawPhotoURI =
                    cursor.getString(cursor.getColumnIndex(Contactables.PHOTO_URI))
                if (!TextUtils.isEmpty(rawPhotoURI)) {
                    contact.photoURI = rawPhotoURI
                }
            }
            if (mimeType == StructuredName.CONTENT_ITEM_TYPE) {
                contact.firstName =
                    cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME))
                contact.middleName =
                    cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME))
                contact.lastName =
                    cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME))
                //                contact.prefix = cursor.getString(cursor.getColumnIndex(StructuredName.PREFIX));
//                contact.suffix = cursor.getString(cursor.getColumnIndex(StructuredName.SUFFIX));
            } else if (mimeType == Phone.CONTENT_ITEM_TYPE) {
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
                val type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE))
                if (!TextUtils.isEmpty(phoneNumber)) {
                    var label: String
                    label = when (type) {
                        Phone.TYPE_HOME -> "home"
                        Phone.TYPE_WORK -> "work"
                        Phone.TYPE_MOBILE -> "mobile"
                        else -> "other"
                    }
                    contact.phones.add(ContactItem(label, phoneNumber))
                }
            } else if (mimeType == Email.CONTENT_ITEM_TYPE) {
                val email =
                    cursor.getString(cursor.getColumnIndex(Email.ADDRESS))
                val type = cursor.getInt(cursor.getColumnIndex(Email.TYPE))
                if (!TextUtils.isEmpty(email)) {
                    var label: String
                    label = when (type) {
                        Email.TYPE_HOME -> "home"
                        Email.TYPE_WORK -> "work"
                        Email.TYPE_MOBILE -> "mobile"
                        Email.TYPE_CUSTOM -> if (cursor.getString(
                                cursor.getColumnIndex(
                                    Email.LABEL
                                )
                            ) != null
                        ) {
                            cursor.getString(cursor.getColumnIndex(Email.LABEL))
                                .toLowerCase()
                        } else {
                            ""
                        }
                        else -> "other"
                    }
                    contact.emails.add(ContactItem(label, email))
                }
            } else if (mimeType == Organization.CONTENT_ITEM_TYPE) {
                contact.companyName =
                    cursor.getString(cursor.getColumnIndex(Organization.COMPANY))
                contact.jobTitle =
                    cursor.getString(cursor.getColumnIndex(Organization.TITLE))
                //                contact.department = cursor.getString(cursor.getColumnIndex(Organization.DEPARTMENT));
            } else if (mimeType == StructuredPostal.CONTENT_ITEM_TYPE) {
                val address =
                    cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS))
                val type = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE))
                if (!TextUtils.isEmpty(address)) {
                    var label: String
                    label = when (type) {
                        StructuredPostal.TYPE_HOME -> "home"
                        StructuredPostal.TYPE_WORK -> "work"
                        StructuredPostal.TYPE_CUSTOM -> if (cursor.getString(
                                cursor.getColumnIndex(
                                    StructuredPostal.LABEL
                                )
                            ) != null
                        ) {
                            cursor.getString(cursor.getColumnIndex(StructuredPostal.LABEL))
                                .toLowerCase()
                        } else {
                            ""
                        }
                        else -> "other"
                    }
                    contact.postalAddresses.add(ContactItem(label, address))
                }
            }
        }
        return map
    }

    companion object {
        private val COLUMN_PROJECTION: List<String?> =
            object : ArrayList<String?>() {
                init {
                    add(ContactsContract.Data.CONTACT_ID)
                    add(ContactsContract.Data.LOOKUP_KEY)
                    add(ContactsContract.Contacts.Data.MIMETYPE)
                    add(ContactsContract.Profile.DISPLAY_NAME)
                    add(Contactables.PHOTO_URI)
                    add(StructuredName.DISPLAY_NAME)
                    add(StructuredName.GIVEN_NAME)
                    add(StructuredName.MIDDLE_NAME)
                    add(StructuredName.FAMILY_NAME)
                    add(StructuredName.PREFIX)
                    add(StructuredName.SUFFIX)
                    add(Phone.NUMBER)
                    add(Phone.TYPE)
                    add(Phone.LABEL)
                    add(Email.DATA)
                    add(Email.ADDRESS)
                    add(Email.TYPE)
                    add(Email.LABEL)
                    add(Organization.COMPANY)
                    add(Organization.TITLE)
                    add(Organization.DEPARTMENT)
                    add(StructuredPostal.FORMATTED_ADDRESS)
                    add(StructuredPostal.TYPE)
                    add(StructuredPostal.LABEL)
                    add(StructuredPostal.STREET)
                    add(StructuredPostal.POBOX)
                    add(StructuredPostal.NEIGHBORHOOD)
                    add(StructuredPostal.CITY)
                    add(StructuredPostal.REGION)
                    add(StructuredPostal.POSTCODE)
                    add(StructuredPostal.COUNTRY)
                    add(Contactables.PHOTO_URI)
                }
            }
    }

    init {
        listeners = ArrayList()
    }
}