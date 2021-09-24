package com.techyourchance.testdrivendevelopment.exercise6

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise6.users.User
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache

interface FetchUserUseCaseSync {
    enum class Status {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    data class UseCaseResult(val status: Status, val user: User?)

    fun fetchUserSync(userId: String?): UseCaseResult
}



interface GetUserUseCase {
    fun execute(userId: String): Any
}

class GetUserUseCaseImpl(
    private val usersCache: UsersCache,
    private val endpointSync: FetchUserHttpEndpointSync
) : GetUserUseCase {
    override fun execute(userId: String): User {
        val cachedUser = usersCache.getUser(userId)
        return if (cachedUser == null) {
            fetchUser(userId)
        } else {
            cachedUser
        }
    }

    private fun fetchUser(userId: String, ): User {
        val result = endpointSync.fetchUserSync(userId)
        val user = User(result.userId, result.username)
        usersCache.cacheUser(user)
        return user
    }

}

interface FetchUserUseCase {
    fun execute(userId: String): Any
}

class FetchUserUseCaseImpl(
    private val usersCache: UsersCache,
    private val endpointSync: FetchUserHttpEndpointSync
) : FetchUserUseCase {
    override fun execute(userId: String): User {
        val result = endpointSync.fetchUserSync(userId)
        val user = User(result.userId, result.username)
        usersCache.cacheUser(user)
        return user
    }
}
