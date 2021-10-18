package com.techyourchance.testdrivendevelopment.exercise7

import com.nhaarman.mockitokotlin2.mock
import com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.Status
import com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.UseCaseResult
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`

class FetchReputationUseCaseSyncTest {

    companion object {
        private const val DEFAULT_REPUTATION = 0
    }

    private val getReputationHttpEndpointSyncMock: GetReputationHttpEndpointSync = mock()

    private val SUT = FetchReputationUseCaseSync(getReputationHttpEndpointSyncMock)

    @Test
    fun `success with reputation 1 - fetchReputation - success returned`() {
        // given
        val reputation = 1
        success(reputation)
        // when
        val result = SUT.fetchReputation()
        // then
        Assert.assertEquals(UseCaseResult(Status.SUCCESS, reputation), result)
    }

    @Test
    fun `success with reputation 20 - fetchReputation - success returned`() {
        // given
        val reputation = 20
        success(reputation)
        // when
        val result = SUT.fetchReputation()
        // then
        Assert.assertEquals(UseCaseResult(Status.SUCCESS, reputation), result)
    }

    @Test
    fun `generalError with reputation 20 - fetchReputation - failureReturned`() {
        val reputation = 20
        generalError(reputation)

        val result = SUT.fetchReputation()

        Assert.assertEquals(UseCaseResult(Status.FAILURE, DEFAULT_REPUTATION), result)
    }


    @Test
    fun `generalError with reputation 0 - fetchReputation - failureReturned`() {
        val reputation = 0
        generalError(reputation)

        val result = SUT.fetchReputation()

        Assert.assertEquals(UseCaseResult(Status.FAILURE, DEFAULT_REPUTATION), result)
    }

    @Test
    fun `serverError - fetchReputation - failureReturned`() {
        serverError()

        val result = SUT.fetchReputation()

        Assert.assertEquals(UseCaseResult(Status.FAILURE, DEFAULT_REPUTATION), result)
    }

    @Test
    fun `networkError - fetchReputation - networkReturned`() {
        networkError()

        val result = SUT.fetchReputation()

        Assert.assertEquals(UseCaseResult(Status.NETWORK_ERROR, DEFAULT_REPUTATION), result)
    }

    private fun success(reputation: Int) {
        `when`(getReputationHttpEndpointSyncMock.reputationSync()).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.SUCCESS,
                reputation
            )
        )
    }

    private fun generalError(reputation: Int) {
        `when`(getReputationHttpEndpointSyncMock.reputationSync()).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                reputation
            )
        )
    }

    private fun serverError() {
        `when`(getReputationHttpEndpointSyncMock.reputationSync()).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.SERVER_ERROR,
                DEFAULT_REPUTATION
            )
        )
    }

    private fun networkError() {
        `when`(getReputationHttpEndpointSyncMock.reputationSync()).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR,
                DEFAULT_REPUTATION
            )
        )
    }
}