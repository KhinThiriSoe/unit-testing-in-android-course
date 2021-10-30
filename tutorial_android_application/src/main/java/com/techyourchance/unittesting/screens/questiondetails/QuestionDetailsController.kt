package com.techyourchance.unittesting.screens.questiondetails

import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCase
import com.techyourchance.unittesting.questions.Listener
import com.techyourchance.unittesting.questions.QuestionDetails
import com.techyourchance.unittesting.screens.common.screensnavigator.ScreensNavigator
import com.techyourchance.unittesting.screens.common.toastshelper.ToastsHelper

class QuestionDetailsController(
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val toastsHelper: ToastsHelper
) : QuestionDetailsViewMvc.Listener {

    private var _questionId: String? = null
    private lateinit var _viewMvc: QuestionDetailsViewMvc

    private val fetchQuestionDetailsUseCaseListener = object : Listener {
            override fun onQuestionDetailsFetched(questionDetails: QuestionDetails) {
                _viewMvc.hideProgressIndication()
                _viewMvc.bindQuestion(questionDetails)
            }

            override fun onQuestionDetailsFetchFailed() {
                _viewMvc.hideProgressIndication()
                toastsHelper.showUseCaseError()
            }
        }

    fun bindQuestionId(questionId: String?) {
        _questionId = questionId
    }

    fun bindView(viewMvc: QuestionDetailsViewMvc) {
        _viewMvc = viewMvc
    }

    fun onStart() {
         fetchQuestionDetailsUseCase.registerListener(fetchQuestionDetailsUseCaseListener)
        _viewMvc.registerListener(this)
        _viewMvc.showProgressIndication()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(_questionId)
    }

    fun onStop() {
        fetchQuestionDetailsUseCase.unregisterListener(fetchQuestionDetailsUseCaseListener)
        _viewMvc.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }
}