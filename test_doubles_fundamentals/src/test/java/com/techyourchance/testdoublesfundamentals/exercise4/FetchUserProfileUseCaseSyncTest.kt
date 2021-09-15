package com.techyourchance.testdoublesfundamentals.exercise4

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException
import com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResult
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResultStatus
import com.techyourchance.testdoublesfundamentals.exercise4.users.User
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FetchUserProfileUseCaseSyncTest {

    companion object {
        // change all the constats to private
        private const val USER_ID = "1"
        private const val FULL_NAME = "Khin"
        private const val IMAGE_URL = "imageUrl"
        private const val EMPTY_USER = ""
    }

    private lateinit var fetchUserProfileUseCaseSync: FetchUserProfileUseCaseSync
    private lateinit var userProfileHttpEndpointSyncTd: UserProfileHttpEndpointSyncTd
    private lateinit var userCacheTd: UsersCacheTd

    @Before
    fun setup() {
        userProfileHttpEndpointSyncTd = UserProfileHttpEndpointSyncTd()
        userCacheTd = UsersCacheTd()
        fetchUserProfileUseCaseSync =
            FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, userCacheTd)
    }

    @Test
    fun fetchUserProfile_success_userIdEmptyPassedToEndpoint() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.SUCCESS

        fetchUserProfileUseCaseSync.fetchUserProfileSync(EMPTY_USER)

        assertThat(userProfileHttpEndpointSyncTd.userId, `is`(EMPTY_USER))
    }

    @Test
    fun fetchUserProfile_success_userIdPassedToEndpoint() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.SUCCESS

        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(userProfileHttpEndpointSyncTd.userId, `is`(USER_ID))
    }

    @Test
    fun fetchUserProfile_success_userCached() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.SUCCESS

        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(userCacheTd.getUser(USER_ID), `is`(User(USER_ID, FULL_NAME, IMAGE_URL)))
        assertEquals(User(USER_ID, FULL_NAME, IMAGE_URL), userCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfile_generalError_userNotCached() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.GENERAL_ERROR

        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(userCacheTd.getUser(USER_ID), `is`(nullValue()))
        assertNull(userCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfile_authError_userNotCached() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.AUTH_ERROR

        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(userCacheTd.getUser(USER_ID), `is`(nullValue()))
        assertNull(userCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfile_serverError_userNotCached() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.SERVER_ERROR

        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(userCacheTd.getUser(USER_ID), `is`(nullValue()))
        assertNull(userCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfile_networkError_userNotCached() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = null

        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(userCacheTd.getUser(USER_ID), `is`(nullValue()))
        assertNull(userCacheTd.getUser(USER_ID))
    }

    @Test
    fun fetchUserProfile_success_successReturned() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.SUCCESS

        val result = fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(result, `is`(UseCaseResult.SUCCESS))
        assertEquals(UseCaseResult.SUCCESS, result)
    }

    @Test
    fun fetchUserProfile_generalError_failureReturned() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.GENERAL_ERROR

        val result = fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(result, `is`(UseCaseResult.FAILURE))
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun fetchUserProfile_authError_failureReturned() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.AUTH_ERROR

        val result = fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(result, `is`(UseCaseResult.FAILURE))
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun fetchUserProfile_serverError_failureReturned() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = EndpointResultStatus.SERVER_ERROR

        val result = fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(result, `is`(UseCaseResult.FAILURE))
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    fun fetchUserProfile_networkError_failureReturned() {
        userProfileHttpEndpointSyncTd.endpointResultStatus = null

        val result = fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID)

        assertThat(result, `is`(UseCaseResult.NETWORK_ERROR))
        assertEquals(UseCaseResult.NETWORK_ERROR, result)
    }

    // region test doubles

    class UserProfileHttpEndpointSyncTd : UserProfileHttpEndpointSync {

        var userId: String = ""
        // Aung changed it from many boolean variables to one EndpointResultStatus variable
        var endpointResultStatus: EndpointResultStatus? = null

        override fun getUserProfile(userId: String): EndpointResult {
            this.userId = userId

            return when (endpointResultStatus) {
                EndpointResultStatus.AUTH_ERROR,
                EndpointResultStatus.SERVER_ERROR,
                EndpointResultStatus.GENERAL_ERROR -> {
                    EndpointResult(endpointResultStatus, null, null, null)
                }
                EndpointResultStatus.SUCCESS -> {
                    EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, FULL_NAME, IMAGE_URL)
                }
                null -> {
                    throw NetworkErrorException()
                }
            }
        }
    }

    class UsersCacheTd : UsersCache {

        // Aung changed to private and from arrayListOf to mutableListOf
        // kotlint names should not start with m. so, it should not be mUser
        // it should be plural because it's a list
        private val users = mutableListOf<User>()

        override fun cacheUser(user: User) {
            users.add(user)
        }

        override fun getUser(userId: String?): User? {
            return users.find { user ->
                user.userId == userId
            }
        }

    }

    // endregion test doubles

}





