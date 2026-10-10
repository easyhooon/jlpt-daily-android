package dev.jlpt.daily

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val StudyColors = lightColorScheme(
    primary = Color(0xFF28624B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDFEEE2),
    onPrimaryContainer = Color(0xFF183E2D),
    background = Color(0xFFFAF8F0),
    surface = Color(0xFFFFFEF8),
    onSurface = Color(0xFF26352D),
    secondary = Color(0xFF677664),
    error = Color(0xFFAA453A),
)

@Composable
fun StudyScreen(
    state: StudyUiState,
    onOpen: (Question, Boolean) -> Unit,
    onSubmit: (Int) -> Unit,
    onClose: () -> Unit,
    onCopy: (Question, Attempt) -> Unit,
    onRetryReview: () -> Unit,
    onTab: (String) -> Unit,
    onStartQuestion: () -> Unit,
    onReadLesson: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme(colorScheme = StudyColors) {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing), color = MaterialTheme.colorScheme.background) {
            val question = state.question
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("まいにち N3", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(if (state.showLesson) "오늘 배울 내용" else if (question == null) "하루 한 걸음" else if (state.review) "오답 다시 풀기" else "배운 내용 확인하기", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(if (state.showLesson) "짧은 설명과 예문을 읽고 연결된 문제를 풀어요." else if (question == null) "설명부터 읽고, 관련 문제로 확인해요. 처음은 13개 학습 내용으로 시작해요." else "필요하면 학습 설명을 다시 읽어도 괜찮아요.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                state.error?.let { error -> item { Text(error, color = MaterialTheme.colorScheme.error) } }
                if (question != null) {
                    item { TextButton(onClick = onClose) { Text("← 목록으로") } }
                    item {
                        if (state.showLesson && state.lesson != null) {
                            LessonDetail(state.lesson, state.busy, state.answer != null, onStartQuestion)
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                TextButton(onClick = onReadLesson) { Text("학습 설명 다시 보기") }
                                QuestionDetail(question, state.answer, state.busy, onSubmit, onCopy, onRetryReview, state.review, Modifier.fillMaxWidth())
                            }
                        }
                    }
                } else {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("오늘" to "오늘 학습", "오답" to "오답 복습", "기록" to "학습 기록").forEach { (key, title) ->
                                if (state.tab == key) {
                                    Button(onClick = { onTab(key) }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) { Text(title) }
                                } else {
                                    OutlinedButton(onClick = { onTab(key) }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) { Text(title) }
                                }
                            }
                        }
                    }
                    when (state.tab) {
                        "오답" -> {
                            item {
                                SectionHeading("다시 만나면 더 쉬워져요", "틀린 문제를 정답으로 풀면 복습 목록에서 사라져요.")
                            }
                            if (state.pending.isEmpty()) item { EmptyCard("복습할 오답이 없어요", "오늘의 문제를 풀고 작은 성취를 쌓아 보세요.") }
                            items(state.pending, key = { it.id }) { q -> QuestionCard(q, "다시 풀기", { onOpen(q, true) }, Modifier.fillMaxWidth()) }
                        }
                        "기록" -> {
                            item { SectionHeading("쌓이는 학습 기록", "문제와 답을 복사해 기존 ChatGPT 대화에 직접 붙여 넣을 수 있어요.") }
                            if (state.attempts.isEmpty()) item { EmptyCard("아직 기록이 없어요", "첫 문제를 풀면 날짜별로 여기에 남아요.") }
                            val questionById = QuestionBank.all.associateBy { it.id }
                            state.attempts.groupBy { it.date }.toSortedMap(reverseOrder()).forEach { (date, attempts) ->
                                item(key = "date-$date") {
                                    SectionHeading(date, "${attempts.size}회 풀이 · 정답 ${attempts.count { it.correct }}회")
                                }
                                items(attempts.sortedByDescending { it.createdAt }, key = { it.key }) { attempt ->
                                    val q = questionById[attempt.questionId]
                                    if (q != null) HistoryCard(q, attempt, onCopy, Modifier.fillMaxWidth())
                                    else EmptyCard(attempt.questionId, "${if (attempt.review) "복습" else "오늘 학습"} · ${if (attempt.correct) "정답" else "오답"}")
                                }
                            }
                        }
                        else -> {
                            val completedIds = state.attempts.filter { it.date == state.date && !it.review }.map { it.questionId }.toSet()
                            val completed = state.daily.count { it.id in completedIds }
                            item {
                                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(state.date, style = MaterialTheme.typography.labelLarge)
                                        Text("설명 한 조각, 문제 한 걸음", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                        Text("문법 최대 3개 · 독해 1개를 짧게 배우고 확인해요.")
                                        Text("문법과 짧은 독해 ${state.daily.size}문제 · $completed/${state.daily.size} 완료")
                                        LinearProgressIndicator(progress = { if (state.daily.isEmpty()) 0f else completed.toFloat() / state.daily.size }, modifier = Modifier.fillMaxWidth())
                                        if (state.daily.isNotEmpty() && completed == state.daily.size) Text("오늘 학습 완료! 오답을 한 번 더 살펴보세요.", fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                            if (state.daily.isEmpty()) item {
                                if (state.busy) EmptyCard("기록을 불러오고 있어요", "잠시만 기다려 주세요.")
                                else EmptyCard("새 문제를 모두 풀었어요", "검수한 다음 문제 묶음을 추가하기 전까지 오답과 기록에서 복습할 수 있어요.")
                            }
                            items(state.daily, key = { it.id }) { q ->
                                LessonCard(LessonBank.forQuestion(q), if (q.id in completedIds) "내용·결과 다시 보기" else "설명 읽고 풀기", { onOpen(q, false) })
                            }
                        }
                    }
                    item { ResourceFooter(Modifier.fillMaxWidth()) }
                }
            }
        }
    }
}

@Composable
private fun QuestionDetail(
    question: Question,
    answer: Attempt?,
    busy: Boolean,
    onSubmit: (Int) -> Unit,
    onCopy: (Question, Attempt) -> Unit,
    onRetryReview: () -> Unit,
    review: Boolean,
    modifier: Modifier = Modifier,
) {
    var selected by rememberSaveable(question.id, answer?.key) { mutableIntStateOf(-1) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeading(question.title, "자체 제작 연습 문제 · 공식 기출 아님")
        if (question.passage.isNotBlank()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Text(question.passage, Modifier.padding(18.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Text(question.prompt, style = MaterialTheme.typography.titleMedium)
        question.options.forEachIndexed { index, option ->
            val checked = (answer?.selectedIndex ?: selected) == index
            val correctOption = answer != null && index == question.correctIndex
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = answer == null && !busy) { selected = index },
                shape = RoundedCornerShape(16.dp),
                color = if (correctOption) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                border = BorderStroke(if (checked) 2.dp else 1.dp, if (checked) MaterialTheme.colorScheme.primary else Color(0xFFE1E5DB)),
            ) {
                Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadioButton(selected = checked, enabled = answer == null && !busy, onClick = null)
                    Column(Modifier.weight(1f).padding(vertical = 10.dp)) {
                        Text("${index + 1}. $option", style = MaterialTheme.typography.bodyLarge)
                        if (correctOption) Text("정답", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                        if (answer != null && checked && !answer.correct) Text("내가 고른 답", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
        if (answer == null) {
            Button(onClick = { onSubmit(selected) }, enabled = selected >= 0 && !busy, modifier = Modifier.fillMaxWidth()) { Text(if (busy) "저장 중…" else "답 확인하기") }
        } else {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(if (answer.correct) "정답이에요!" else "괜찮아요, 한 번 더 기억해요", style = MaterialTheme.typography.titleMedium, color = if (answer.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                    Text(question.explanation, style = MaterialTheme.typography.bodyLarge)
                }
            }
            OutlinedButton(onClick = { onCopy(question, answer) }, modifier = Modifier.fillMaxWidth()) { Text("문제 · 내 답 · 해설 복사") }
            Text("복사한 내용을 기존 ChatGPT 대화에 직접 붙여 넣어 질문하세요.", style = MaterialTheme.typography.bodySmall)
            if (review && !answer.correct) Button(onClick = onRetryReview, enabled = !busy, modifier = Modifier.fillMaxWidth()) { Text("이 문제 다시 풀기") }
        }
    }
}

@Composable
private fun QuestionCard(question: Question, action: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(if (question.passage.isBlank()) "문법" else "짧은 독해", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(question.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(question.prompt, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onClick) { Text("$action →") }
        }
    }
}

@Composable
private fun HistoryCard(question: Question, attempt: Attempt, onCopy: (Question, Attempt) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("${if (attempt.review) "오답 복습" else "오늘 학습"} · ${if (attempt.correct) "정답" else "오답"}", color = if (attempt.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge)
            Text(question.title, fontWeight = FontWeight.SemiBold)
            question.passage.takeIf { it.isNotBlank() }?.let { Text(it) }
            Text(question.prompt)
            question.options.forEachIndexed { index, option -> Text("${index + 1}. $option") }
            Text("내 답: ${attempt.selectedIndex + 1}. ${question.options.getOrNull(attempt.selectedIndex).orEmpty()}")
            Text("정답: ${question.correctIndex + 1}. ${question.options[question.correctIndex]}", color = MaterialTheme.colorScheme.primary)
            Text(question.explanation, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = { onCopy(question, attempt) }) { Text("이 풀이 복사") }
        }
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun EmptyCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        SectionHeading(title, subtitle, Modifier.padding(20.dp))
    }
}

@Composable
private fun ResourceFooter(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("N3 대비 자체 제작 연습이며 기본 문법 복습도 포함합니다. 출제와 AI 검토를 거쳤으며 공식 기출·전문가 검수·JLPT 인증 자료가 아닙니다.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        TextButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jlpt.jp/e/samples/sampleindex.html"))) }) { Text("JLPT 공식 샘플 자료 ↗") }
        Text("공식 페이지의 2012·2018 자료는 최신 기출이 아닙니다.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
    }
}
