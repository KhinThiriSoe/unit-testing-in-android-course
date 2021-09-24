package com.techyourchance.testdrivendevelopment.exercise6

import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.*
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException
import com.techyourchance.testdrivendevelopment.exercise6.users.User
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache

class FetchUserUseCaseSyncImpl(
    private val fetchUserHttpEndpointSync: FetchUserHttpEndpointSync,
    private val usersCache: UsersCache
) : FetchUserUseCaseSync {

    override fun fetchUserSync(userId: String?): UseCaseResult {
        val user = usersCache.getUser(userId)
        return user?.let {
            UseCaseResult(Status.SUCCESS, user)
        } ?: kotlin.run {
            fetchUserFromServer(userId)
        }
    }

    private fun fetchUserFromServer(userId: String?): UseCaseResult {
        return try {
            val endpointResult = fetchUserHttpEndpointSync.fetchUserSync(userId)
            if (endpointResult.status == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS) {
                val user = User(endpointResult.userId, endpointResult.username)
                usersCache.cacheUser(user)
                UseCaseResult(Status.SUCCESS, user)
            } else {
                UseCaseResult(Status.FAILURE, user = null)
            }
        } catch (e: NetworkErrorException) {
            UseCaseResult(Status.NETWORK_ERROR, user = null)
        }
    }
}