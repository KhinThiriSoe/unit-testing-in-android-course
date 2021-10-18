package com.techyourchance.testdrivendevelopment.exercise8

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint

class FetchContactsUseCase(private val getContactsHttpEndpoint: GetContactsHttpEndpoint) {

    interface Listener {
        fun onSuccess(contacts: List<Contact>)
        fun onFailed()
    }

    private val listeners = mutableListOf<Listener>()

    fun fetchContacts(searchKeyword: String) {
        getContactsHttpEndpoint.getContacts(
            searchKeyword,
            object : GetContactsHttpEndpoint.Callback {
                override fun onGetContactsSucceeded(contactSchemas: MutableList<ContactSchema>?) {
                    val contacts = contactSchemas?.toContacts() ?: emptyList()
                    listeners.forEach {
                        it.onSuccess(contacts)
                    }
                }

                override fun onGetContactsFailed(failReason: GetContactsHttpEndpoint.FailReason?) {
                    when (failReason) {
                        GetContactsHttpEndpoint.FailReason.GENERAL_ERROR,
                        GetContactsHttpEndpoint.FailReason.NETWORK_ERROR -> {
                            listeners.forEach {
                                it.onFailed()
                            }
                        }
                    }
                }
            })
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    private fun List<ContactSchema>.toContacts() = map { it.toContact() }

    private fun ContactSchema.toContact() = Contact(id, fullName, imageUrl)
}