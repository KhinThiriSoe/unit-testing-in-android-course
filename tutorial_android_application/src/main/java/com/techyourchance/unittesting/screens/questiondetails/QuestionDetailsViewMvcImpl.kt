package com.techyourchance.unittesting.screens.questiondetails

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.techyourchance.unittesting.R
import com.techyourchance.unittesting.questions.QuestionDetails
import com.techyourchance.unittesting.screens.common.ViewMvcFactory
import com.techyourchance.unittesting.screens.common.toolbar.ToolbarViewMvc
import com.techyourchance.unittesting.screens.common.views.BaseObservableViewMvc

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<QuestionDetailsViewMvc.Listener?>(), QuestionDetailsViewMvc {
    private val mToolbarViewMvc: ToolbarViewMvc
    private val mToolbar: Toolbar
    private val mTxtQuestionTitle: TextView
    private val mTxtQuestionBody: TextView
    private val mProgressBar: ProgressBar
    private fun initToolbar() {
        mToolbar.addView(mToolbarViewMvc.rootView)
        mToolbarViewMvc.setTitle(getString(R.string.question_details_screen_title))
        mToolbarViewMvc.enableUpButtonAndListen {
            for (listener in listeners) {
                listener!!.onNavigateUpClicked()
            }
        }
    }

    override fun bindQuestion(question: QuestionDetails?) {
        val questionTitle: String = question?.title ?: ""
        val questionBody: String = question?.body ?: ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTxtQuestionTitle.text = Html.fromHtml(questionTitle, Html.FROM_HTML_MODE_LEGACY)
            mTxtQuestionBody.text = Html.fromHtml(questionBody, Html.FROM_HTML_MODE_LEGACY)
        } else {
            mTxtQuestionTitle.text = Html.fromHtml(questionTitle)
            mTxtQuestionBody.text = Html.fromHtml(questionBody)
        }
    }

    override fun showProgressIndication() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndication() {
        mProgressBar.visibility = View.GONE
    }

    init {
        rootView = inflater.inflate(R.layout.layout_question_details, parent, false)
        mTxtQuestionTitle = findViewById(R.id.txt_question_title)
        mTxtQuestionBody = findViewById(R.id.txt_question_body)
        mProgressBar = findViewById(R.id.progress)
        mToolbar = findViewById(R.id.toolbar)
        mToolbarViewMvc = viewMvcFactory.getToolbarViewMvc(mToolbar)
        initToolbar()
    }
}