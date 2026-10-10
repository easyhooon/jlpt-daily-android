package dev.jlpt.daily

import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class NavigationCompatibilityTest {
    @Test fun previousSavedQuizRouteKeepsQuestionAndReviewToken() {
        val old = """{"tab":"오답","questionId":"g01","review":true,"token":"previous-review"}"""
        val restored = Json.decodeFromString<StudyRoute>(old)
        assertEquals("g01", restored.questionId)
        assertEquals("previous-review", restored.token)
        assertTrue(restored.review)
        assertFalse(restored.showLesson)
        assertFalse(restored.returning)
        assertEquals("lesson_g01", LessonBank.forQuestion(QuestionBank.all.single { it.id == restored.questionId }).id)
    }

    @Test fun lessonRevisitRouteKeepsSameQuestionAndReviewTokenAfterSerialization() {
        val route = StudyRoute("오답", "g03", true, "review-token", showLesson = true, returning = true)
        val restored = Json.decodeFromString<StudyRoute>(Json.encodeToString(route))
        assertEquals(route, restored)
    }
}
