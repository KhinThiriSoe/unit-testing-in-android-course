package com.techyourchance.mockitofundamentals.exercise5

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.techyourchance.mockitofundamentals.exercise5.users.User
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`

class UpdateUsernameUseCaseSyncTest1 {


    companion object {
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
    }

    private val updateUsernameHttpEndpointSyncMock: UpdateUsernameHttpEndpointSync = mock()
    private val usersCacheMock: UsersCache = mock()
    private val eventBusPosterMock: EventBusPoster = mock()

    private val SUT = UpdateUsernameUseCaseSync(
        updateUsernameHttpEndpointSyncMock,
        usersCacheMock,
        eventBusPosterMock
    )

    @Test
    fun `success - UpdateUsernameUseCaseSync - userId and username passed into endpoint`() {
        success()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verify(updateUsernameHttpEndpointSyncMock).updateUsername(USER_ID, USER_NAME)
    }

    @Test
    fun `success - UpdateUsernameUseCaseSync - success returned, user cached, UserDetailsChangedEvent posted`() {
        success()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertEquals(UseCaseResult.SUCCESS, result)
        val user = User(USER_ID, USER_NAME)
        verify(usersCacheMock).cacheUser(user)
        verify(eventBusPosterMock).postEvent(UserDetailsChangedEvent(user))

    }

    @Test
    fun `generalError - UpdateUsernameUseCaseSync - failure returned, user not cached, UserDetailsChangedEvent not posted`() {
        generalError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertEquals(UseCaseResult.FAILURE, result)
        verifyNoMoreInteractions(usersCacheMock)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun `authError - UpdateUsernameUseCaseSync - failure returned, user not cached, UserDetailsChangedEvent not posted`() {
        authError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertEquals(UseCaseResult.FAILURE, result)
        verifyNoMoreInteractions(usersCacheMock)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun `serverError - UpdateUsernameUseCaseSync - failure returned, user not cached, UserDetailsChangedEvent not posted`() {
        serverError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertEquals(UseCaseResult.FAILURE, result)
        verifyNoMoreInteractions(usersCacheMock)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun `networkError - UpdateUsernameUseCaseSync - failure returned, user not cached, UserDetailsChangedEvent not posted`() {
        networkError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertEquals(UseCaseResult.NETWORK_ERROR, result)
        verifyNoMoreInteractions(usersCacheMock)
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    private fun success() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USER_NAME)
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,
                USER_ID, USER_NAME
            )
        )
    }

    private fun generalError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USER_NAME)
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                USER_ID, USER_NAME
            )
        )
    }

    private fun serverError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USER_NAME)
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                USER_ID, USER_NAME
            )
        )
    }

    private fun authError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USER_NAME)
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                USER_ID, USER_NAME
            )
        )
    }

    private fun networkError() {
        whenever(updateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USER_NAME)).thenThrow(
            NetworkErrorException()
        )
    }
}