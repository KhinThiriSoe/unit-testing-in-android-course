package com.techyourchance.mockitofundamentals.exercise5

import com.nhaarman.mockitokotlin2.*
import com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.*
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.techyourchance.mockitofundamentals.exercise5.users.User
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`

class UpdateUsernameUseCaseSyncTest {

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
    }

    private val updateUsernameHttpEndpointSyncMock: UpdateUsernameHttpEndpointSync = mock()
    private val usersCacheMock: UsersCache = mock()
    private val eventBusPosterMock: EventBusPoster = mock()

    private val SUT: UpdateUsernameUseCaseSync =
        UpdateUsernameUseCaseSync(
            updateUsernameHttpEndpointSyncMock, usersCacheMock, eventBusPosterMock
        )

    @Before
    fun setup() {
        success()
    }

    // update username success passToEndpoint
    // given - when - expected
    @Test
//    fun `update username success pass To endpoint`() {
    fun `success - updateUsernameSync - correct args passed into updateUsernameHttpEndpointSyncMock`() {
        SUT.updateUsernameSync(USER_ID, USER_NAME)

        // changed from ArgumentCaptor.forClass(String::class.java)
        val ac = argumentCaptor<String>()
        verify(updateUsernameHttpEndpointSyncMock, times(1)).updateUsername(
            ac.capture(),
            ac.capture()
        )
        val capture = ac.allValues
        assertThat(capture[0], `is`(USER_ID))
        assertThat(capture[1], `is`(USER_NAME))

        // Aung update
        verify(updateUsernameHttpEndpointSyncMock).updateUsername(
            USER_ID,
            USER_NAME
        )
    }

    @Test
    fun `success - updateUsernameSync - usersCached`() {
        // act
        SUT.updateUsernameSync(USER_ID, USER_NAME)

        val ac = ArgumentCaptor.forClass(User::class.java)
        verify(usersCacheMock).cacheUser(ac.capture())
        assertThat(ac.value, `is`(User(USER_ID, USER_NAME)))

        // Aung edit
        verify(usersCacheMock).cacheUser(User(USER_ID, USER_NAME))
    }

    @Test
    fun `success - updateUsernameSync - success returned, user cached, UserDetailsChangedEvent posted`() {
        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        Assert.assertEquals(UseCaseResult.SUCCESS, result)
        val user = User(USER_ID, USER_NAME)
        verify(usersCacheMock).cacheUser(user)
        verify(eventBusPosterMock).postEvent(UserDetailsChangedEvent(user))
    }


    @Test
    fun `generalError - updateUsernameSync - user not cached`() {
        generalError()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun `authError - updateUsernameSync - user not cached`() {
        authError()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    fun `serverError - updateUsernameSync - user not cached`() {
        serverError()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verifyNoMoreInteractions(usersCacheMock)
    }

    @Test
    // fun `success - updateUsernameSync - eventBusPosted`() {
    fun `success - updateUsernameSync - UserDetailsChangedEvent posted with user`() {
        SUT.updateUsernameSync(USER_ID, USER_NAME)

        val ac = ArgumentCaptor.forClass(UserDetailsChangedEvent::class.java)
        verify(eventBusPosterMock).postEvent(ac.capture())
        assertThat(ac.value, `is`(instanceOf(UserDetailsChangedEvent::class.java)))
        // Aung added below line
        Assert.assertEquals(User(USER_ID, USER_NAME), ac.value.user)
    }

    @Test
    fun `generalError - updateUsernameSync - UserDetailsChangedEvent not posted`() {
        generalError()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun `authError - updateUsernameSync - UserDetailsChangedEvent not posted`() {
        authError()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun `serverError - updateUsernameSync - UserDetailsChangedEvent not posted`() {
        serverError()

        SUT.updateUsernameSync(USER_ID, USER_NAME)

        verifyNoMoreInteractions(eventBusPosterMock)
    }

    @Test
    fun `success - updateUsernameSync - success returned`() {
        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertThat(result, `is`(UseCaseResult.SUCCESS))
    }

    @Test
    fun `generalError - updateUsernameSync - failure returned`() {
        generalError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    fun `authError - updateUsernameSync - failure returned`() {
        authError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    fun `serverError - updateUsernameSync - failure returned`() {
        serverError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertThat(result, `is`(UseCaseResult.FAILURE))
    }

    @Test
    fun `networkError - updateUsernameSync - failure returned`() {
        networkError()

        val result = SUT.updateUsernameSync(USER_ID, USER_NAME)

        assertThat(result, `is`(UseCaseResult.NETWORK_ERROR))
    }

    private fun success() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                USER_ID, USER_NAME
            )
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,
                USER_ID, USER_NAME
            )
        )
    }

    private fun generalError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                USER_ID, USER_NAME
            )
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                USER_ID, USER_NAME
            )
        )
    }

    private fun authError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                USER_ID, USER_NAME
            )
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                USER_ID, USER_NAME
            )
        )
    }

    private fun serverError() {
        `when`(
            updateUsernameHttpEndpointSyncMock.updateUsername(
                USER_ID, USER_NAME
            )
        ).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                USER_ID, USER_NAME
            )
        )
    }

    private fun networkError() {
        doThrow(NetworkErrorException()).`when`(updateUsernameHttpEndpointSyncMock).updateUsername(
            USER_ID, USER_NAME
        )
        /*
        whenever(updateUsernameHttpEndpointSyncMock.updateUsername(USER_ID, USER_NAME)).thenAnswer {
            throw NetworkErrorException()
        }
        * */

    }
}