package com.techyourchance.unittesting.screens.questiondetails

import com.nhaarman.mockitokotlin2.*
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCase
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCaseImpl
import com.techyourchance.unittesting.questions.Listener
import com.techyourchance.unittesting.screens.common.screensnavigator.ScreensNavigator
import com.techyourchance.unittesting.screens.common.toastshelper.ToastsHelper
import com.techyourchance.unittesting.testdata.QuestionsTestData
import org.junit.Before
import org.junit.Test

class QuestionDetailsControllerTest {

    companion object {
        private const val QUESTION_ID = "Question_id"
    }

    private val listenerMock: Listener = mock()
    private val fetchQuestionDetailsEndpointMock: FetchQuestionDetailsEndpoint = mock()
    private val screensNavigatorMock: ScreensNavigator = mock()
    private val toastsHelperMock: ToastsHelper = mock()
    private val questionDetailsViewMvcMock: QuestionDetailsViewMvc = mock()

    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase =
        FetchQuestionDetailsUseCaseImpl(fetchQuestionDetailsEndpointMock)

    private lateinit var SUT: QuestionDetailsController

    @Before
    fun setup() {
        SUT = QuestionDetailsController(
            fetchQuestionDetailsUseCase,
            screensNavigatorMock,
            toastsHelperMock
        )

        SUT.bindView(questionDetailsViewMvcMock)
        SUT.bindQuestionId(QUESTION_ID)
    }

    @Test
    fun `success - fetch question details - bind to view`() {
        // given
        fetchQuestionsSuccess()
        // when
        SUT.onStart()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        verify(questionDetailsViewMvcMock, times(2)).bindQuestion(QuestionsTestData.questionDetails)
    }

    @Test
    fun `fetchQuestionDetails success - onStart - register listener, show progress bar, bind questions, hide progress bar`() {
        // given
        fetchQuestionsSuccess()
        // when
        SUT.onStart()
        // then
        verify(questionDetailsViewMvcMock).registerListener(SUT)
        questionDetailsViewMvcMock.inOrder {
            verify().showProgressIndication()
            verify().hideProgressIndication()
            verify().bindQuestion(QuestionsTestData.questionDetails)
        }
    }

    @Test
    fun `fetchQuestionDetails success - onStart - called listener 2 times, show progress bar, bind questions and hide progress bar`() {
        // given
        fetchQuestionsSuccess()
        // when
        SUT.onStart()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        verify(questionDetailsViewMvcMock).registerListener(SUT)
        questionDetailsViewMvcMock.apply {
            verify(this).showProgressIndication()
            verify(this, times(2)).hideProgressIndication()
            verify(this, times(2)).bindQuestion(QuestionsTestData.questionDetails)
        }
    }

    @Test
    fun `fetchQuestionDetails failure - onStart - register listener show progress bar, show error without bind view and hide progress bar`() {
        // given
        fetchQuestionsFailure()
        // when
        SUT.onStart()
        fetchQuestionDetailsUseCase.registerListener(listenerMock)
        // then
        verify(questionDetailsViewMvcMock).registerListener(SUT)
        questionDetailsViewMvcMock.inOrder {
            verify().showProgressIndication()
            verify().hideProgressIndication()

        }
        verify(toastsHelperMock).showUseCaseError()
        verify(questionDetailsViewMvcMock, never()).bindQuestion(any())
    }

    @Test
    fun `fetchQuestionDetails - onStop - unregistered listener`() {
        // when
        SUT.onStop()
        fetchQuestionDetailsUseCase.unregisterListener(listenerMock)
        // then
        verify(listenerMock, never()).onQuestionDetailsFetchFailed()
        verify(listenerMock, never()).onQuestionDetailsFetched(QuestionsTestData.questionDetails)
        verify(questionDetailsViewMvcMock).unregisterListener(SUT)
    }

    @Test
    fun `when back button clicked - back to previous screen `() {
        // when
        SUT.onNavigateUpClicked()
        // then
        verify(screensNavigatorMock).navigateUp()
    }

    private fun fetchQuestionsSuccess() {
        whenever(
            fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any())
        ).thenAnswer {
            val callback = it.arguments[1] as FetchQuestionDetailsEndpoint.Listener
            callback.onQuestionDetailsFetched(QuestionsTestData.questionSchema)
            callback
        }
    }

    private fun fetchQuestionsFailure() {
        whenever(
            fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any())
        ).thenAnswer {
            val callback = it.arguments[1] as FetchQuestionDetailsEndpoint.Listener
            callback.onQuestionDetailsFetchFailed()
            callback
        }
    }

}