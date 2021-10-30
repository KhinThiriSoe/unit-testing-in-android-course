package com.techyourchance.unittesting.questions

import com.nhaarman.mockitokotlin2.*
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint
import com.techyourchance.unittesting.networking.questions.QuestionSchema
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCaseImpl.*
import org.junit.Before
import org.junit.Test

class FetchQuestionDetailsUseCaseTest {

    companion object {
        private const val QUESTION_ID = "question_id"
        private const val QUESTION_TITLE = "question_title"
        private const val QUESTION_BODY = "question_body"
    }

    private lateinit var SUT: FetchQuestionDetailsUseCase

    private val fetchQuestionDetailsEndpointMock: FetchQuestionDetailsEndpoint = mock()
    private val listenerMock1: Listener = mock()
    private val listenerMock2: Listener = mock()

    @Before
    fun setup() {
        SUT = FetchQuestionDetailsUseCaseImpl(fetchQuestionDetailsEndpointMock)
    }

    @Test
    fun `fetchQuestionDetailsAndNotify - correct parameter passed to the endpoint`() {
        // given
        success()
        // when
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        verify(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(eq(QUESTION_ID), any())
    }

    @Test
    fun `fetchQuestionDetailsAndNotify - success - observers notified of correct data`() {
        // given
        success()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        val questionDetails = getQuestionDetails()
        verify(listenerMock1).onQuestionDetailsFetched(questionDetails)
        verify(listenerMock2).onQuestionDetailsFetched(questionDetails)
    }

    @Test
    fun `fetchQuestionDetailsAndNotify - success - unsubscribed observers not notified`() {
        // given
        success()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.unregisterListener(listenerMock2)
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        verify(listenerMock1).onQuestionDetailsFetched(getQuestionDetails())
        verifyNoMoreInteractions(listenerMock2)
    }

    @Test
    fun `fetchQuestionDetailsAndNotify - failure - observers notified of failure`() {
        // given
        failure()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        verify(listenerMock1).onQuestionDetailsFetchFailed()
        verify(listenerMock2).onQuestionDetailsFetchFailed()
    }

    @Test
    fun `fetchQuestionDetailsAndNotify - failure - unsubscribed observers not notified of failure`() {
        // given
        failure()
        // when
        SUT.registerListener(listenerMock1)
        SUT.registerListener(listenerMock2)
        SUT.unregisterListener(listenerMock2)
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        verify(listenerMock1).onQuestionDetailsFetchFailed()
        verifyNoMoreInteractions(listenerMock2)
    }


    private fun getQuestionDetails() = QuestionDetails(QUESTION_ID, QUESTION_TITLE, QUESTION_BODY)

    private fun getQuestionScheme() = QuestionSchema(QUESTION_TITLE, QUESTION_ID, QUESTION_BODY)

    private fun success() {
        whenever(fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any()))
            .thenAnswer {
                val args = it.arguments
                val callback = args[1] as FetchQuestionDetailsEndpoint.Listener
                callback.onQuestionDetailsFetched(getQuestionScheme())
            }
    }

    private fun failure() {
        whenever(fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any()))
            .thenAnswer {
                val args = it.arguments
                val callback = args[1] as FetchQuestionDetailsEndpoint.Listener
                callback.onQuestionDetailsFetchFailed()
            }
    }
}