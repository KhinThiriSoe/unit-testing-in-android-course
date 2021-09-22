package com.techyourchance.mockitofundamentals.exercise5.networking

import kotlin.Throws

interface UpdateUsernameHttpEndpointSync {
    /**
     * Update user's username on the server
     * @return the aggregated result
     * @throws NetworkErrorException if operation failed due to network error
     */
    @Throws(NetworkErrorException::class)
    fun updateUsername(userId: String?, username: String?): EndpointResult?
    enum class EndpointResultStatus {
        SUCCESS, AUTH_ERROR, SERVER_ERROR, GENERAL_ERROR
    }

    data class EndpointResult(val status: EndpointResultStatus, val userId: String, val username: String)
}