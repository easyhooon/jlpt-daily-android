package dev.jlpt.daily

import androidx.compose.runtime.*
import androidx.compose.runtime.retain.retain
import java.time.LocalDate
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class StudyUiState(
    val date: String, val tab: String, val attempts: List<Attempt>,
    val daily: List<Question>, val pending: List<Question>, val question: Question?,
    val review: Boolean, val answer: Attempt?, val busy: Boolean, val error: String?,
    val lesson: Lesson? = null, val showLesson: Boolean = false,
)
class StudyPresenter {
    var busy by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var submitted by mutableStateOf<Attempt?>(null)
    var token by mutableStateOf(UUID.randomUUID().toString())
    fun open() { submitted = null; token = UUID.randomUUID().toString(); error = null }
}
@Composable fun retainStudyPresenter(): StudyPresenter = retain { StudyPresenter() }

@Composable fun currentStudyDate(): LocalDate {
    var date by remember { mutableStateOf(LocalDate.now()) }
    LaunchedEffect(Unit) { while (true) { date = LocalDate.now(); delay(15_000) } }
    return date
}
