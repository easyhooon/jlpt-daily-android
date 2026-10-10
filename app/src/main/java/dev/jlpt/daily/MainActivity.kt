package dev.jlpt.daily

import android.os.Bundle
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.*

@Serializable data class StudyRoute(val tab: String = "오늘", val questionId: String? = null, val review: Boolean = false, val token: String = "") : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val graph = (application as StudyApplication).graph
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF33695B), background = androidx.compose.ui.graphics.Color(0xFFFAF8F2))) {
            SwrClientProvider(graph.client) { StudyApp(graph) }
        } }
    }
}
@OptIn(ExperimentalSoilQueryApi::class)
@Composable fun StudyApp(graph: AppGraph) {
    val stack = rememberNavBackStack(StudyRoute())
    val presenter = retainStudyPresenter()
    val subscription = rememberSubscription(graph.subscription)
    val attempts = subscription.data.orEmpty()
    val date = currentStudyDate()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    NavDisplay(backStack = stack, onBack = { if(stack.size > 1) stack.removeLastOrNull() }, entryProvider = entryProvider {
        entry<StudyRoute> { route ->
            val question = QuestionBank.all.find { it.id == route.questionId }
            val answer = if (route.review) attempts.find { it.key == "review:$date:${question?.id}:${route.token}" } ?: presenter.submitted?.takeIf { it.date == date.toString() && it.questionId == question?.id } else attempts.find { it.date == date.toString() && it.questionId == question?.id && !it.review }
            val state = StudyUiState(date.toString(), route.tab, attempts, QuestionBank.daily(date, attempts), graph.store.pending(attempts), question, route.review, answer, presenter.busy || subscription.data == null, presenter.error ?: if(subscription.error != null) "저장 기록을 읽지 못했습니다. 앱을 다시 열어 주세요." else null)
            StudyScreen(state,
                onOpen = { q, review -> if (subscription.data != null && !presenter.busy) { presenter.open(); stack.add(StudyRoute(route.tab, q.id, review, presenter.token)) } },
                onSubmit = submit@ { index ->
                    if (presenter.busy || question == null || answer != null) return@submit
                    presenter.busy = true
                    scope.launch {
                        try {
                            val inserted = graph.store.answer(date, question, index, route.review, route.token)
                            if (inserted) presenter.submitted = Attempt("ui", date.toString(), question.id, index, index == question.correctIndex, route.review, System.currentTimeMillis())
                        } catch (e: Exception) { presenter.error = "답을 저장하지 못했습니다. 다시 제출해 주세요." }
                        finally { presenter.busy = false }
                    }
                },
                onClose = { if(stack.size > 1) stack.removeLastOrNull() },
                onCopy = { q, a ->
                    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("JLPT 학습", graph.store.copyText(q,a)))
                    Toast.makeText(context,"복사했습니다. 기존 ChatGPT 대화에 붙여넣으세요.",Toast.LENGTH_SHORT).show()
                },
                onRetryReview = { presenter.open(); stack[stack.lastIndex] = route.copy(token = presenter.token) },
                onTab = { tab -> stack.clear(); stack.add(StudyRoute(tab)) }
            )
        }
    })
}
