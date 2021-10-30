package com.techyourchance.unittesting.screens.common.screensnavigator

import com.techyourchance.unittesting.screens.common.fragmentframehelper.FragmentFrameHelper
import com.techyourchance.unittesting.screens.questiondetails.QuestionDetailsFragment
import com.techyourchance.unittesting.screens.questionslist.QuestionsListFragment

interface ScreensNavigator {
    fun toQuestionDetails(questionId: String?)

    fun toQuestionsList()

    fun navigateUp()
}

class ScreensNavigatorImpl(private val mFragmentFrameHelper: FragmentFrameHelper) :
    ScreensNavigator {
    override fun toQuestionDetails(questionId: String?) {
        mFragmentFrameHelper.replaceFragment(QuestionDetailsFragment.newInstance(questionId))
    }

    override fun toQuestionsList() {
        mFragmentFrameHelper.replaceFragmentAndClearBackstack(QuestionsListFragment.newInstance())
    }

    override fun navigateUp() {
        mFragmentFrameHelper.navigateUp()
    }
}