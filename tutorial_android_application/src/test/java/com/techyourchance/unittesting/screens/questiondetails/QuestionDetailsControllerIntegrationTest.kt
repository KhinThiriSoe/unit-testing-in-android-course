package com.techyourchance.unittesting.screens.questiondetails

import com.nhaarman.mockitokotlin2.*
import com.techyourchance.unittesting.networking.StackoverflowApi
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCase
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCaseImpl
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCaseImpl.*
import com.techyourchance.unittesting.questions.Listener
import com.techyourchance.unittesting.screens.common.screensnavigator.ScreensNavigator
import com.techyourchance.unittesting.screens.common.toastshelper.ToastsHelper
import com.techyourchance.unittesting.testdata.QuestionsTestData
import org.junit.Before
import org.junit.Test

class QuestionDetailsControllerIntegrationTest {

    companion object {
        private const val QUESTION_ID = "QUESTION_ID"
    }
    private val fetchQuestionDetailsEndpointMock: FetchQuestionDetailsEndpoint = mock()
    private val fetchQuestionDetailsUseCase = FetchQuestionDetailsUseCaseImpl(fetchQuestionDetailsEndpointMock)

    private val screensNavigatorMock: ScreensNavigator = mock()

    private val toastsHelperMock: ToastsHelper = mock()

    private val questionDetailsViewMvcMock: QuestionDetailsViewMvc = mock()

    private val SUT = QuestionDetailsController(fetchQuestionDetailsUseCase, screensNavigatorMock, toastsHelperMock)

    @Before
    fun setup() {
        SUT.bindView(questionDetailsViewMvcMock)
    }

    @Test
    fun `fetchQuestionDetails success - onStart - bindQuestion of questionDetailsViewMvcMock called with correct values`() {
        // given
        fetchQuestionDetailsSuccess()
        // when
        SUT.bindQuestionId(QUESTION_ID)
        SUT.onStart()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(QUESTION_ID)
        // then
        questionDetailsViewMvcMock.inOrder {
            verify().registerListener(SUT)
            verify().showProgressIndication()
            verify().hideProgressIndication()
            verify().bindQuestion(QuestionsTestData.questionDetails)
        }

    }

    @Test
    fun `onStop - unRegister of questionDetailsViewMvcMock is called`() {
        SUT.onStop()

        verify(questionDetailsViewMvcMock).unregisterListener(SUT)
    }

    @Test
    fun `fetchQuestionDetails success - onStart, onStop and fetchQuestionDetailsUseCase - hideProgressIndication and bindQuestion of questionDetailsViewMvcMock are not called`() {
        fetchQuestionDetailsSuccess()
        SUT.bindQuestionId(QUESTION_ID)
        SUT.onStart()

        SUT.onStop()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verify(questionDetailsViewMvcMock, times(1)).hideProgressIndication()
        verify(questionDetailsViewMvcMock, times(1)).bindQuestion(any())
    }


    @Test
    fun `fetchQuestionDetails success - onStart and fetchQuestionDetailsUseCase - hideProgressIndication and bindQuestion of questionDetailsViewMvcMock are not called`() {
        fetchQuestionDetailsSuccess()
        SUT.bindQuestionId(QUESTION_ID)

        val listener: Listener = mock()
        fetchQuestionDetailsUseCase.registerListener(listener)

        SUT.onStart()
        fetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verify(questionDetailsViewMvcMock, times(2)).hideProgressIndication()
        verify(questionDetailsViewMvcMock, times(2)).bindQuestion(any())

        verify(listener, times(2)).onQuestionDetailsFetched(any())
    }

    private fun fetchQuestionDetailsSuccess() {
        whenever(fetchQuestionDetailsEndpointMock.fetchQuestionDetails(any(), any())).thenAnswer {
            val callback = it.arguments[1] as FetchQuestionDetailsEndpoint.Listener
            callback.onQuestionDetailsFetched(QuestionsTestData.questionSchema)
            callback
        }
    }

}