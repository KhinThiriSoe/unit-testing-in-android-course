package com.techyourchance.unittesting.questions

import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint
import com.techyourchance.unittesting.networking.questions.QuestionSchema

interface FetchQuestionDetailsUseCase {
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener?)
    fun fetchQuestionDetailsAndNotify(questionId: String?)
}

interface Listener {
    fun onQuestionDetailsFetched(questionDetails: QuestionDetails)
    fun onQuestionDetailsFetchFailed()
}

class FetchQuestionDetailsUseCaseImpl(
    private val fetchQuestionDetailsEndpoint: FetchQuestionDetailsEndpoint
) : FetchQuestionDetailsUseCase {

    private val listeners = mutableListOf<Listener>()

    private val fetchQuestionDetailsEndpointListener =
        object : FetchQuestionDetailsEndpoint.Listener {
            override fun onQuestionDetailsFetched(question: QuestionSchema) {
                notifySuccess(question.toQuestionDetails())
            }

            override fun onQuestionDetailsFetchFailed() {
                notifyFailure()
            }
        }

    override fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: Listener?) {
        listeners.remove(listener)
    }

    override fun fetchQuestionDetailsAndNotify(questionId: String?) {
        fetchQuestionDetailsEndpoint.fetchQuestionDetails(
            questionId,
            fetchQuestionDetailsEndpointListener
        )
    }

    private fun notifyFailure() {
        listeners.forEach {
            it.onQuestionDetailsFetchFailed()
        }
    }

    private fun notifySuccess(questionDetails: QuestionDetails) {
        listeners.forEach {
            it.onQuestionDetailsFetched(
                questionDetails
            )
        }
    }

    private fun QuestionSchema.toQuestionDetails() =
        QuestionDetails(id = id, title = title, body = body)
}