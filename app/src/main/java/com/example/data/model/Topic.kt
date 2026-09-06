package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"])]
)
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val title: String,
    val theoryCompleted: Boolean = false,
    val rev1Completed: Boolean = false,
    val rev2Completed: Boolean = false,
    val rev3Completed: Boolean = false,
    val rev4Completed: Boolean = false,
    val rev5Completed: Boolean = false,
    val rev6Completed: Boolean = false,
    val rev7Completed: Boolean = false,
    val orderIndex: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
) {
    val completedCount: Int
        get() = listOf(
            theoryCompleted,
            rev1Completed,
            rev2Completed,
            rev3Completed,
            rev4Completed,
            rev5Completed,
            rev6Completed,
            rev7Completed
        ).count { it }

    val totalStages: Int
        get() = 8

    val progress: Float
        get() = completedCount.toFloat() / totalStages.toFloat()
}
