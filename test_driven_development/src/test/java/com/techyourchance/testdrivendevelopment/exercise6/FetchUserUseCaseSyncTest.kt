package com.techyourchance.testdrivendevelopment.exercise6

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.*
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException
import com.techyourchance.testdrivendevelopment.exercise6.users.User
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`

class FetchUserUseCaseSyncImplTest {

    companion object {
        private const val USER_ID = "user_id"
        private const val USERNAME = "username"
    }

    private val fetchUserHttpEndpointSyncMock: FetchUserHttpEndpointSync = mock()
    private val usersCacheMock: UsersCache = mock()

    private val SUT = FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSyncMock, usersCacheMock)

    @Test
    fun `success - fetchUserSync - userid passed into the endpoint`() {
        success()

        SUT.fetchUserSync(USER_ID)

        verify(fetchUserHttpEndpointSyncMock).fetchUserSync(USER_ID)
    }

    @Test
    fun `cached user already exists - fetchUserSync - user cached returned`() {
        stubCachedUser()

        val result = SUT.fetchUserSync(USER_ID)

        val user = User(USER_ID, USERNAME)
        verify(usersCacheMock).getUser(USER_ID)
        Assert.assertEquals(user, result.user)

        /*
        Mock
        1. You may need to verify if certain funcs/methods of the mock were called for certain number of times
        2. You may need to verify if certain funcs/method of the mock were called with correct params
        3. You never need to verify/assert if the return value is what you expect or not because that's what you define right inside your test

        SUT
        1. Need to verify the return value
        2. Sometimes, you may need to verify no exception will be thrown
        3. Sometimes, you may need to verify certain exceptions will be thrown
        * */
    }

    @Test
    fun `no cached user exists, success - fetchUserSync - success returned, user cached`() {
        success()

        val result = SUT.fetchUserSync(USER_ID)

        val user = User(USER_ID, USERNAME)
        Assert.assertEquals(UseCaseResult(Status.SUCCESS, user), result)
        verify(usersCacheMock).cacheUser(user)
    }

    @Test
    fun `generalError - fetchUserSync - failure returned`() {
        generalError()

        val result = SUT.fetchUserSync(USER_ID)

        Assert.assertEquals(UseCaseResult(Status.FAILURE, null), result)
    }


    @Test
    fun `authError - fetchUserSync - failure returned`() {
        authError()

        val result = SUT.fetchUserSync(USER_ID)

        Assert.assertEquals(UseCaseResult(Status.FAILURE, null), result)
    }

    @Test
    fun `serverError - fetchUserSync - failure returned`() {
        serverError()

        val result = SUT.fetchUserSync(USER_ID)

        Assert.assertEquals(UseCaseResult(Status.FAILURE, null), result)
    }

    @Test
    fun `networkError - fetchUserSync - failure returned`() {
        networkError()

        val result = SUT.fetchUserSync(USER_ID)

        Assert.assertEquals(UseCaseResult(Status.NETWORK_ERROR, null), result)
    }

    private fun success() {
        `when`(fetchUserHttpEndpointSyncMock.fetchUserSync(USER_ID)).thenReturn(
            FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USERNAME
            )
        )
    }

    private fun stubCachedUser() {
        `when`(usersCacheMock.getUser(USER_ID)).thenReturn(
           User(USER_ID, USERNAME)
        )
    }

    private fun generalError() {
        `when`(fetchUserHttpEndpointSyncMock.fetchUserSync(USER_ID)).thenReturn(
            FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR, null, null
            )
        )
    }

    private fun authError() {
        `when`(fetchUserHttpEndpointSyncMock.fetchUserSync(USER_ID)).thenReturn(
            FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR, null, null
            )
        )
    }

    private fun serverError() {
        `when`(fetchUserHttpEndpointSyncMock.fetchUserSync(USER_ID)).thenReturn(
            FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SERVER_ERROR, null, null
            )
        )
    }

    private fun networkError() {
        whenever(fetchUserHttpEndpointSyncMock.fetchUserSync(USER_ID)).thenThrow(
            NetworkErrorException()
        )
    }
}