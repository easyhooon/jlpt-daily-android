package dev.jlpt.daily

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LessonCard(lesson: Lesson, action: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(lesson.goal, style = MaterialTheme.typography.bodyMedium)
            Text("짧은 설명 + 연결된 확인 문제 1개", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            TextButton(onClick = onClick) { Text("$action →") }
        }
    }
}

@Composable
fun LessonDetail(lesson: Lesson, busy: Boolean, answered: Boolean, onStart: () -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(lesson.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("학습 목표", fontWeight = FontWeight.SemiBold)
                Text(lesson.goal)
                Text(lesson.explanation, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Text("접속 형태 · 읽는 요령", fontWeight = FontWeight.SemiBold)
        Text(lesson.connection, style = MaterialTheme.typography.bodyLarge)
        Text("예문과 뜻", fontWeight = FontWeight.SemiBold)
        lesson.examples.forEach { example ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(example.japanese, style = MaterialTheme.typography.bodyLarge)
                    Text(example.korean, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        Text("기억할 점", fontWeight = FontWeight.SemiBold)
        Text(lesson.caution)
        Text("이 학습 목표에 연결된 문제 1개로 확인해요. 문제 중에도 설명을 다시 볼 수 있어요.", style = MaterialTheme.typography.bodyMedium)
        Text("자체 제작 설명·예문과 AI 검토 자료입니다. 공식 기출·전문가 검수 자료가 아닙니다.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        Button(onClick = onStart, enabled = !busy, modifier = Modifier.fillMaxWidth()) {
            Text(if (answered) "연결된 문제 결과 보기" else "관련 문제 풀기")
        }
    }
}
