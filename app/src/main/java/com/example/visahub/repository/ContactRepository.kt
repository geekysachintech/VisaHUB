package com.example.visahub.repository

import com.example.visahub.data.ContactModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ContactRepository(private val source: ContactDataSource, private val myDispatcher: CoroutineDispatcher) {
    suspend fun fetchContacts() : List<ContactModel> {
        return withContext(myDispatcher){
            source.fetchContacts()
        }
    }
}