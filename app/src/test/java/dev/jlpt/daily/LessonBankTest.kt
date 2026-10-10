package dev.jlpt.daily

import java.time.LocalDate
import org.junit.Assert.*
import org.junit.Test

class LessonBankTest {
    @Test fun everyExistingQuestionHasExactlyOneRelevantLesson() {
        val expectedIds = (1..10).map { "g%02d".format(it) } + (1..3).map { "r%02d".format(it) }
        assertEquals(expectedIds.toSet(), QuestionBank.all.map { it.id }.toSet())
        assertEquals(13, LessonBank.all.size)
        assertEquals(13, LessonBank.all.map { it.id }.toSet().size)
        QuestionBank.all.forEach { question ->
            assertEquals("lesson_${question.id}", question.lessonId)
            assertEquals(question.lessonId, LessonBank.forQuestion(question).id)
            assertEquals(1, LessonBank.all.count { it.id == question.lessonId })
        }
        assertEquals(LessonBank.all.map { it.id }.toSet(), QuestionBank.all.map { it.lessonId }.toSet())
    }

    @Test fun lessonsIncludeExplanationConnectionExamplesAndCautionBeforeTheQuiz() {
        LessonBank.all.forEach { lesson ->
            assertTrue("${lesson.id} title", lesson.title.isNotBlank())
            assertTrue("${lesson.id} goal", lesson.goal.isNotBlank())
            assertTrue("${lesson.id} explanation", lesson.explanation.isNotBlank())
            assertTrue("${lesson.id} connection", lesson.connection.isNotBlank())
            assertTrue("${lesson.id} caution", lesson.caution.isNotBlank())
            assertTrue("${lesson.id} examples", lesson.examples.isNotEmpty())
            lesson.examples.forEach { example ->
                assertTrue("${lesson.id} Japanese example", Regex("[ぁ-んァ-ヶ一-龯]").containsMatchIn(example.japanese))
                assertTrue("${lesson.id} Korean translation", Regex("[가-힣]").containsMatchIn(example.korean))
            }
        }
        QuestionBank.all.filter { it.kind == "grammar" }.forEach { question ->
            assertTrue("${question.id} grammar sources", LessonBank.forQuestion(question).sourceUrls.isNotEmpty())
            LessonBank.forQuestion(question).sourceUrls.forEach { source -> assertTrue(source.startsWith("https://")) }
        }
    }

    @Test fun dailyLessonsMatchOnlyTodaysQuestionsAndPreviouslyAnsweredIdsStayConsumed() {
        val today = LocalDate.of(2026, 10, 10)
        val previous = QuestionBank.daily(today.minusDays(1)).map { answered(it, today.minusDays(1)) }
        val questions = QuestionBank.daily(today, previous)
        val lessons = questions.map(LessonBank::forQuestion)
        assertEquals(questions.map { "lesson_${it.id}" }, lessons.map { it.id })
        assertTrue(lessons.none { lesson -> previous.any { "lesson_${it.questionId}" == lesson.id } })
        assertEquals(questions.size, lessons.map { it.id }.toSet().size)
        val partialToday = previous + answered(questions.first(), today)
        assertEquals(lessons, QuestionBank.daily(today, partialToday).map(LessonBank::forQuestion))
    }

    @Test fun exhaustedDailyQuestionsDoNotProduceUnrelatedLessons() {
        val today = LocalDate.of(2026, 10, 10)
        val history = QuestionBank.all.map { answered(it, today.minusDays(1)) }
        assertTrue(QuestionBank.daily(today, history).map(LessonBank::forQuestion).isEmpty())
        val restoredQuestion = QuestionBank.all.single { it.id == "r03" }
        val withToday = history.filterNot { it.questionId == restoredQuestion.id } + answered(restoredQuestion, today)
        assertEquals(listOf(LessonBank.forQuestion(restoredQuestion)),
            QuestionBank.daily(today, withToday).map(LessonBank::forQuestion))
    }

    private fun answered(question: Question, date: LocalDate) = Attempt(
        "daily:$date:${question.id}", date.toString(), question.id,
        question.correctIndex, true, false, 0L,
    )
}
