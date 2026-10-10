package dev.jlpt.daily

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [33], application = Application::class)
class LessonUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun readLessonThenLinkedQuestionThenExplanationAndResult() {
        val q = QuestionBank.all.first()
        val lesson = LessonBank.forQuestion(q)
        val state = mutableStateOf(StudyUiState("2026-10-10", "오늘", emptyList(), listOf(q), emptyList(), null, false, null, false, null))
        var submissions = 0
        compose.setContent {
            // Separate destinations, as in the Navigation3 host.
            key(state.value.question?.id, state.value.showLesson) {
            StudyScreen(state.value,
                onOpen = { question, review -> state.value = state.value.copy(question = question, review = review, lesson = LessonBank.forQuestion(question), showLesson = true) },
                onSubmit = { index -> submissions++; state.value = state.value.copy(answer = Attempt("test", "2026-10-10", q.id, index, index == q.correctIndex, false, 1)) },
                onClose = { state.value = state.value.copy(question = null, lesson = null, showLesson = false) },
                onCopy = { _, _ -> }, onRetryReview = {}, onTab = {},
                onStartQuestion = { state.value = state.value.copy(showLesson = false) },
                onReadLesson = { state.value = state.value.copy(showLesson = true) },
            )
            }
        }
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("설명 읽고 풀기 →"))
        compose.onNodeWithText("설명 읽고 풀기 →").performClick()
        compose.onNodeWithText("오늘 배울 내용").assertIsDisplayed()
        compose.onNodeWithText(lesson.explanation).assertExists()
        compose.onNodeWithText(q.prompt).assertDoesNotExist()
        compose.onNodeWithText("관련 문제 풀기").performScrollTo().performClick()
        compose.onNodeWithText(q.prompt).assertExists()
        compose.onNodeWithText("답 확인하기").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithText("${q.correctIndex + 1}. ${q.options[q.correctIndex]}").performScrollTo().performClick()
        compose.onNodeWithText("답 확인하기").performScrollTo().performClick()
        compose.onNodeWithText("정답이에요!").assertExists()
        compose.onNodeWithText("답 확인하기").assertDoesNotExist()
        assertEquals(1, submissions)
        compose.onNodeWithText("학습 설명 다시 보기").performScrollTo().performClick()
        compose.onNodeWithText("오늘 배울 내용").assertExists()
        compose.onNodeWithText("연결된 문제 결과 보기").performScrollTo().performClick()
        compose.onNodeWithText(q.explanation).assertExists()
        assertEquals(q.id, state.value.answer?.questionId)
    }

    @Test fun exhaustedLessonsOfferHonestReviewMessage() {
        val state = StudyUiState("2026-10-10", "오늘", emptyList(), emptyList(), emptyList(), null, false, null, false, null)
        compose.setContent { StudyScreen(state, { _, _ -> }, {}, {}, { _, _ -> }, {}, {}, {}, {}) }
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("새 문제를 모두 풀었어요"))
        compose.onNodeWithText("새 문제를 모두 풀었어요").assertExists()
        compose.onNodeWithText("설명 읽고 풀기 →").assertDoesNotExist()
    }
}
