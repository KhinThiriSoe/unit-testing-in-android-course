package com.techyourchance.unittesting.screens.questiondetails

import com.techyourchance.unittesting.questions.QuestionDetails
import com.techyourchance.unittesting.screens.common.views.ObservableViewMvc

interface QuestionDetailsViewMvc : ObservableViewMvc<QuestionDetailsViewMvc.Listener?> {
    interface Listener {
        fun onNavigateUpClicked()
    }

    fun bindQuestion(question: QuestionDetails?)
    fun showProgressIndication()
    fun hideProgressIndication()
}