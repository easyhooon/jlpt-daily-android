package dev.jlpt.daily

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "attempts")
data class Attempt(
    @PrimaryKey val key: String,
    val date: String,
    val questionId: String,
    val selectedIndex: Int,
    val correct: Boolean,
    val review: Boolean,
    val createdAt: Long,
)

@Dao
interface StudyDao {
    @Query("SELECT * FROM attempts ORDER BY createdAt ASC, rowid ASC")
    fun observe(): Flow<List<Attempt>>

    @Query("SELECT * FROM attempts ORDER BY createdAt ASC, rowid ASC")
    suspend fun all(): List<Attempt>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attempt: Attempt): Long
}

@Database(entities = [Attempt::class], version = 1, exportSchema = false)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun dao(): StudyDao

    companion object {
        fun open(context: Context): StudyDatabase =
            Room.databaseBuilder(context.applicationContext, StudyDatabase::class.java, "study.db")
                .build()
    }
}

class StudyStore(
    private val dao: StudyDao,
    private val bank: List<Question> = QuestionBank.all,
) {
    /** A stable token for each displayed review allows retries while rejecting double taps. */
    suspend fun answer(
        date: LocalDate,
        question: Question,
        index: Int,
        review: Boolean = false,
        reviewToken: String? = null,
    ): Boolean {
        require(index in question.options.indices) { "Invalid answer index" }
        require(bank.any { it == question }) { "Unknown question" }
        val key = if (review) {
            "review:$date:${question.id}:${reviewToken ?: "daily"}"
        } else {
            "daily:$date:${question.id}"
        }
        return dao.insert(
            Attempt(key, date.toString(), question.id, index,
                index == question.correctIndex, review, System.currentTimeMillis()),
        ) != -1L
    }

    /** Uses insertion order for attempts with equal timestamps, as returned by the DAO. */
    fun pending(attempts: List<Attempt>): List<Question> {
        val latest = attempts.sortedBy { it.createdAt }.associateBy { it.questionId }
        return bank.filter { latest[it.id]?.correct == false }
    }

    fun copyText(question: Question, attempt: Attempt): String {
        require(attempt.questionId == question.id)
        require(attempt.selectedIndex in question.options.indices)
        return buildString {
            appendLine("JLPT N3 개인 학습 · ${attempt.date}")
            appendLine("자체 제작 연습 문제이며 공식 기출문제가 아닙니다.")
            appendLine()
            appendLine("문제 ID: ${question.id} · 콘텐츠 v${QuestionBank.contentVersion}")
            appendLine("문제: ${question.title}")
            question.passage.takeIf { it.isNotBlank() }?.let { appendLine("지문:\n$it") }
            appendLine(question.prompt)
            appendLine("보기:")
            question.options.forEachIndexed { index, option -> appendLine("${index + 1}. $option") }
            appendLine("내 답: ${attempt.selectedIndex + 1}. ${question.options[attempt.selectedIndex]}")
            appendLine("정답: ${question.correctIndex + 1}. ${question.options[question.correctIndex]}")
            appendLine("채점: ${if (attempt.correct) "정답" else "오답"}")
            appendLine("해설: ${question.explanation}")
            append("이 문제의 문법과 오답 이유를 한국어로 쉽게 설명하고 비슷한 예문을 알려 주세요.")
        }
    }
}
