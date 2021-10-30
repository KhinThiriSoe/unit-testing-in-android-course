package com.techyourchance.unittesting.testdata

import com.techyourchance.unittesting.networking.questions.QuestionSchema
import com.techyourchance.unittesting.questions.Question
import com.techyourchance.unittesting.questions.QuestionDetails
import java.util.*

object QuestionsTestData {

    @JvmStatic
    val questionSchema: QuestionSchema
        get() = QuestionSchema("id", "title", "body")

    @JvmStatic
    val question: Question
        get() = Question("id", "title")

    @JvmStatic
    val questionDetails: QuestionDetails
        get() = QuestionDetails(questionSchema.id, questionSchema.title, questionSchema.body)

    @JvmStatic
    val questions: List<Question>
        get() {
            val questions: MutableList<Question> = LinkedList()
            questions.add(Question("id1", "title1"))
            questions.add(Question("id2", "title2"))
            return questions
        }
}