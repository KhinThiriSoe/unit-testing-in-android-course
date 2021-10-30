package com.techyourchance.unittesting.screens.questiondetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techyourchance.unittesting.screens.common.controllers.BaseFragment

class QuestionDetailsFragment : BaseFragment() {

    companion object {
        private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"

        fun newInstance(questionId: String?): QuestionDetailsFragment {
            val args = Bundle()
            args.putString(ARG_QUESTION_ID, questionId)
            val fragment = QuestionDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewMvc: QuestionDetailsViewMvc
    private lateinit var questionDetailsController: QuestionDetailsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionDetailsController = compositionRoot.questionDetailsController
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewMvc = compositionRoot.viewMvcFactory.getQuestionDetailsViewMvc(container)
        questionDetailsController.bindView(viewMvc)
        questionDetailsController.bindQuestionId(requireArguments().getString(ARG_QUESTION_ID))

        return viewMvc.rootView
    }

    override fun onStart() {
        super.onStart()
        questionDetailsController.onStart()
    }

    override fun onStop() {
        super.onStop()
        questionDetailsController.onStop()
    }
}