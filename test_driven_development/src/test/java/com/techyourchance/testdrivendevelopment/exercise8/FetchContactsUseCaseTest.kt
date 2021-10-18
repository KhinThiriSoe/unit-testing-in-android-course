package com.techyourchance.testdrivendevelopment.exercise8

import com.nhaarman.mockitokotlin2.*
import com.techyourchance.testdrivendevelopment.exercise8.FetchContactsUseCase.Listener
import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor

class FetchContactsUseCaseTest {

    companion object {
        private const val SEARCH_KEYWORD = "search_keyword"
        private const val ID = "id"
        private const val FULL_NAME = "fullName"
        private const val FULL_PHONE_NUMBER = "fullPhoneNumber"
        private const val IMAGE_URL = "imageUrl"
        private const val AGE = 16.0
    }

    private val getContactsHttpEndpointMock: GetContactsHttpEndpoint = mock()
    private val listenerMock1: Listener = mock()
    private val listenerMock2: Listener = mock()
    private val acContacts = argumentCaptor<List<Contact>>()

    private lateinit var SUT: FetchContactsUseCase

    @Before
    fun setup() {
        SUT = FetchContactsUseCase(getContactsHttpEndpointMock)
    }

    // TODO update name
    @Test
    fun `fetchContacts - correct filter terms passed to the endpoint`() {
        // given
        success()
        // when
        SUT.fetchContacts(SEARCH_KEYWORD)
        // then
        val argumentCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(getContactsHttpEndpointMock).getContacts(argumentCaptor.capture(), any())
        assertEquals(SEARCH_KEYWORD, argumentCaptor.value)

        // Aung edit
        verify(getContactsHttpEndpointMock).getContacts(eq(SEARCH_KEYWORD), any())
    }

    @Test
    fun `success - fetchContacts - observers notified with correct data`() {
        // given
        success()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.fetchContacts(SEARCH_KEYWORD)
        // then
        verify(listenerMock1).onSuccess(acContacts.capture())
        verify(listenerMock2).onSuccess(acContacts.capture())
        val captures: List<List<Contact>> = acContacts.allValues
        val contacts = createContacts()
        assertEquals(contacts, captures[0])
        assertEquals(contacts, captures[0])

        // Aung edit
        verify(listenerMock1).onSuccess(contacts)
        verify(listenerMock2).onSuccess(contacts)
    }

    @Test
    fun `success - fetchContacts - unsubscribed observers not notified`() {
        // given
        success()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.unregisterListener(listenerMock1)
        SUT.fetchContacts(SEARCH_KEYWORD)
        // then
        verifyNoMoreInteractions(listenerMock1)
        verify(listenerMock2).onSuccess(acContacts.capture())
        val captures: List<List<Contact>> = acContacts.allValues
        assertEquals(createContacts(), captures[0])

        // Aung edit
        verify(listenerMock2).onSuccess(createContacts())
    }

    @Test
    fun `generalError - fetchContacts - observers notified of failure`() {
        // given
        generalError()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.fetchContacts(SEARCH_KEYWORD)
        // then
        verify(listenerMock1).onFailed()
        verify(listenerMock2).onFailed()
    }

    @Test
    fun `networkError - fetchContacts - observers notified of failure`() {
        // given
        networkError()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.fetchContacts(SEARCH_KEYWORD)
        // then
        verify(listenerMock1).onFailed()
        verify(listenerMock2).onFailed()
    }

    private fun generalError() {
        whenever(getContactsHttpEndpointMock.getContacts(any(), any()))
            .thenAnswer {
                val args = it.arguments
                val callback = args[1] as GetContactsHttpEndpoint.Callback
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR)
            }
    }

    private fun networkError() {
        whenever(getContactsHttpEndpointMock.getContacts(any(), any()))
            .thenAnswer {
                val args = it.arguments
                val callback = args[1] as GetContactsHttpEndpoint.Callback
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR)
            }
    }

    private fun success() {
        whenever(getContactsHttpEndpointMock.getContacts(any(), any()))
            .thenAnswer {
                val args = it.arguments
                val callback = args[1] as GetContactsHttpEndpoint.Callback
                callback.onGetContactsSucceeded(getContactSchema())
            }
    }

    private fun getContactSchema(): MutableList<ContactSchema> {
        return arrayListOf(ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE))
    }

    private fun createContacts(): List<Contact> {
        return arrayListOf(Contact(ID, FULL_NAME, IMAGE_URL))
    }
}