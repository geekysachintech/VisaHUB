package com.example.visahub.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import com.example.visahub.data.ContactModel

class ContactDataSource(private val contentResolver: ContentResolver) {
    fun fetchContacts(): List<ContactModel> {
        val result: MutableList<ContactModel> = mutableListOf()
        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
            )
        , null
        , null
        , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )
        cursor?.let {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                result.add(
                    ContactModel(
                        cursor.getString(0),
                        cursor.getString(1)
                    )
                )
                cursor.moveToNext()
            }
            cursor.close()
        }
        return result.toList()
    }
}