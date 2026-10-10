package dev.jlpt.daily

data class Lesson(
    val id: String,
    val title: String,
    val goal: String,
    val explanation: String,
    val connection: String,
    val examples: List<LessonExample>,
    val caution: String,
    val sourceUrls: List<String>,
)

data class LessonExample(val japanese: String, val korean: String)
