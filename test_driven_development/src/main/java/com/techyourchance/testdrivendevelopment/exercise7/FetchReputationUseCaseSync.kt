package com.techyourchance.testdrivendevelopment.exercise7

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus

class FetchReputationUseCaseSync(private val getReputationHttpEndpointSync: GetReputationHttpEndpointSync) {

    companion object {
        private const val DEFAULT_REPUTATION = 0
    }

    enum class Status {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    data class UseCaseResult(val status: Status, val reputation: Int)

    fun fetchReputation(): UseCaseResult {
        val result = getReputationHttpEndpointSync.reputationSync()
        return when (result.status) {
            EndpointStatus.SUCCESS -> {
                UseCaseResult(Status.SUCCESS, result.reputation)
            }
            EndpointStatus.GENERAL_ERROR,
            EndpointStatus.SERVER_ERROR -> {
                UseCaseResult(Status.FAILURE, DEFAULT_REPUTATION)
            }
            EndpointStatus.NETWORK_ERROR -> {
                UseCaseResult(Status.NETWORK_ERROR, DEFAULT_REPUTATION)
            }
        }
    }

}