package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_logs")
data class StudyLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topicId: Long,
    val subjectId: Long,
    val stageName: String,
    val isChecked: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
