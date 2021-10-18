package com.techyourchance.testdrivendevelopment.exercise7.networking

interface GetReputationHttpEndpointSync {

    enum class EndpointStatus {
        SUCCESS, GENERAL_ERROR, SERVER_ERROR, NETWORK_ERROR
    }

    class EndpointResult(val status: EndpointStatus, val reputation: Int = 0)

    fun reputationSync(): EndpointResult
}