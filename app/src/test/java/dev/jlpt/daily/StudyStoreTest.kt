package dev.jlpt.daily

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33], application = Application::class)
class StudyStoreTest {
    private lateinit var context: Context
    private lateinit var database: StudyDatabase
    private lateinit var store: StudyStore
    private val today = LocalDate.of(2026, 10, 10)
    private val question get() = QuestionBank.all.first()
    private val wrongIndex get() = (question.correctIndex + 1) % question.options.size

    @Before fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase("study.db")
        database = StudyDatabase.open(context)
        store = StudyStore(database.dao())
    }

    @After fun tearDown() {
        database.close()
        context.deleteDatabase("study.db")
    }

    @Test fun dailyAnswerIsGradedOnceAndCannotBeOverwritten() = runBlocking {
        assertTrue(store.answer(today, question, wrongIndex))
        assertFalse(store.answer(today, question, question.correctIndex))
        val attempt = database.dao().observe().first().single()
        assertEquals(wrongIndex, attempt.selectedIndex)
        assertFalse(attempt.correct)
        assertFalse(attempt.review)
        assertEquals(today.toString(), attempt.date)
        assertEquals(listOf(question), store.pending(listOf(attempt)))
    }

    @Test fun concurrentDoubleTapStoresOnlyOneAnswer() = runBlocking {
        val results = coroutineScope {
            (1..8).map { async(Dispatchers.Default) {
                store.answer(today, question, question.correctIndex)
            } }.awaitAll()
        }
        assertEquals(1, results.count { it })
        assertEquals(1, database.dao().all().size)
        assertTrue(database.dao().all().single().correct)
    }

    @Test fun sameQuestionOnAnotherDayCreatesSeparateRecord() = runBlocking {
        store.answer(today, question, wrongIndex)
        assertTrue(store.answer(today.plusDays(1), question, question.correctIndex))
        val records = database.dao().all().groupBy { it.date }
        assertEquals(setOf(today.toString(), today.plusDays(1).toString()), records.keys)
        assertFalse(records.getValue(today.toString()).single().correct)
        assertTrue(records.getValue(today.plusDays(1).toString()).single().correct)
    }

    @Test fun wrongReviewCanBeRetriedAndSuccessfulReviewClearsPending() = runBlocking {
        store.answer(today, question, wrongIndex)
        assertTrue(store.answer(today, question, wrongIndex, review = true, reviewToken = "first"))
        assertFalse(store.answer(today, question, question.correctIndex, review = true, reviewToken = "first"))
        assertEquals(listOf(question), store.pending(database.dao().all()))
        assertTrue(store.answer(today, question, question.correctIndex, review = true, reviewToken = "retry"))
        assertTrue(store.pending(database.dao().all()).isEmpty())
        assertEquals(3, database.dao().all().size)
        assertTrue(database.dao().all().last().review)
    }

    @Test fun databaseReopenRestoresAnswersAndReviewQueue() = runBlocking {
        store.answer(today, question, wrongIndex)
        val before = database.dao().all()
        database.close()
        database = StudyDatabase.open(context)
        store = StudyStore(database.dao())
        val restored = database.dao().observe().first()
        assertEquals(before, restored)
        assertEquals(listOf(question), store.pending(restored))
        assertFalse(store.answer(today, question, question.correctIndex))
    }

    @Test fun copiedReadingIncludesPassageEveryOptionAndBothAnswers() = runBlocking {
        val reading = QuestionBank.all.first { it.passage.isNotBlank() }
        val wrong = (reading.correctIndex + 1) % reading.options.size
        store.answer(today, reading, wrong)
        val text = store.copyText(reading, database.dao().all().single())
        assertTrue(text.contains("문제 ID: ${reading.id}"))
        assertTrue(text.contains(reading.passage))
        assertTrue(text.contains(reading.prompt))
        reading.options.forEachIndexed { index, option ->
            assertTrue(text.contains("${index + 1}. $option"))
        }
        assertTrue(text.contains("내 답: ${wrong + 1}. ${reading.options[wrong]}"))
        assertTrue(text.contains("정답: ${reading.correctIndex + 1}. ${reading.options[reading.correctIndex]}"))
        assertTrue(text.contains(reading.explanation))
        assertTrue(text.contains("공식 기출문제가 아닙니다"))
        assertTrue(text.contains(today.toString()))
    }

    @Test fun invalidAnswerDoesNotCreateHistory() = runBlocking {
        try {
            store.answer(today, question, -1)
            fail("Invalid answer must be rejected")
        } catch (_: IllegalArgumentException) {
            assertTrue(database.dao().all().isEmpty())
        }
    }
}
