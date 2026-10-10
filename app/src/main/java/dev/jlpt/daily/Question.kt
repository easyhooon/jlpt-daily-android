package dev.jlpt.daily

data class Question(
    val id: String,
    val lessonId: String,
    val kind: String,
    val title: String,
    val passage: String = "",
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)
