package com.techyourchance.unittesting.questions

import com.techyourchance.unittesting.common.BaseObservable
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint
import com.techyourchance.unittesting.networking.questions.QuestionSchema

class FetchQuestionDetailsUseCase(private val mFetchQuestionDetailsEndpoint: FetchQuestionDetailsEndpoint) :
    BaseObservable<FetchQuestionDetailsUseCase.Listener>() {

    interface Listener {
        fun onQuestionDetailsFetched(questionDetails: QuestionDetails?)
        fun onQuestionDetailsFetchFailed()
    }

    fun fetchQuestionDetailsAndNotify(questionId: String?) {
        mFetchQuestionDetailsEndpoint.fetchQuestionDetails(
            questionId,
            object : FetchQuestionDetailsEndpoint.Listener {
                override fun onQuestionDetailsFetched(question: QuestionSchema) {
                    notifySuccess(question.toQuestionDetails())
                }

                override fun onQuestionDetailsFetchFailed() {
                    notifyFailure()
                }
            })
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

    private fun QuestionSchema.toQuestionDetails() = QuestionDetails(id = id, title = title, body = body)
}