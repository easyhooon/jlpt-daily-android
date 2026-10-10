package dev.jlpt.daily

import java.time.LocalDate
import org.junit.Assert.*
import org.junit.Test

class QuestionBankTest {
    @Test fun dailySessionContainsThreeGrammarAndOneReadingWithoutDuplicates() {
        val first = LocalDate.of(2026, 1, 1)
        repeat(366) { offset ->
            val date = first.plusDays(offset.toLong())
            val questions = QuestionBank.daily(date)
            assertEquals(4, questions.size)
            assertEquals(3, questions.count { it.kind == "grammar" })
            assertEquals(1, questions.count { it.kind == "reading" })
            assertEquals(4, questions.map { it.id }.toSet().size)
            assertEquals(questions, QuestionBank.daily(date))
        }
    }

    @Test fun rotationEventuallyIncludesEveryQuestionAndHandlesDatesBeforeEpoch() {
        val first = LocalDate.of(1969, 12, 1)
        val seen = (0L until 30L).flatMap { QuestionBank.daily(first.plusDays(it)) }.map { it.id }.toSet()
        assertEquals(QuestionBank.all.map { it.id }.toSet(), seen)
        assertEquals(4, QuestionBank.daily(LocalDate.MIN).size)
        assertEquals(4, QuestionBank.daily(LocalDate.MAX).size)
    }

    @Test fun contentHasValidAnswerIndexesAndStableUniqueIds() {
        assertEquals(13, QuestionBank.all.size)
        assertEquals(13, QuestionBank.all.map { it.id }.toSet().size)
        QuestionBank.all.forEach {
            assertEquals(4, it.options.size)
            assertEquals(4, it.options.toSet().size)
            assertTrue(it.correctIndex in it.options.indices)
            assertTrue(it.explanation.isNotBlank())
            if (it.kind == "reading") assertTrue(it.passage.isNotBlank())
        }
    }

    @Test fun previouslyAnsweredDailyQuestionsAreNotPresentedAsNew() {
        val date = LocalDate.of(2026, 10, 10)
        val initial = QuestionBank.daily(date)
        val previous = initial.map { attempt(it, date) }
        val next = QuestionBank.daily(date.plusDays(1), previous)
        assertTrue(next.none { question -> previous.any { it.questionId == question.id } })
        assertEquals(initial.map { it.id }.toSet(), QuestionBank.daily(date, previous).map { it.id }.toSet())
    }

    @Test fun todayAnswersRestoreAndReviewAnswersDoNotConsumeNewQuestions() {
        val date = LocalDate.of(2026, 10, 10)
        val initial = QuestionBank.daily(date)
        val partial = listOf(attempt(initial.first(), date))
        assertEquals(initial.map { it.id }.toSet(), QuestionBank.daily(date, partial).map { it.id }.toSet())
        assertEquals(initial, QuestionBank.daily(date, listOf(attempt(initial.first(), date, review = true))))
    }

    @Test fun exhaustedBankReturnsNoNewQuestionsAndOnlyRestoresToday() {
        val date = LocalDate.of(2026, 10, 10)
        val history = QuestionBank.all.map { attempt(it, date.minusDays(1)) }
        assertTrue(QuestionBank.daily(date, history).isEmpty())
        val todayQuestion = QuestionBank.all.first()
        val withToday = history.filterNot { it.questionId == todayQuestion.id } + attempt(todayQuestion, date)
        assertEquals(listOf(todayQuestion), QuestionBank.daily(date, withToday))
    }

    private fun attempt(question: Question, date: LocalDate, review: Boolean = false) = Attempt(
        key = "${question.id}:$date:$review", date = date.toString(), questionId = question.id,
        selectedIndex = question.correctIndex, correct = true, review = review, createdAt = 0,
    )
}
