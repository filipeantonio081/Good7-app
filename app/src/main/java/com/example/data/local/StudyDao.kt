package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SettingEntity
import com.example.data.model.StudyLog
import com.example.data.model.Subject
import com.example.data.model.Topic
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {

    // Subjects
    @Query("SELECT * FROM subjects ORDER BY orderIndex ASC, id ASC")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects WHERE id = :id LIMIT 1")
    suspend fun getSubjectById(id: Long): Subject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject): Long

    @Update
    suspend fun updateSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Query("DELETE FROM subjects WHERE id = :id")
    suspend fun deleteSubjectById(id: Long)

    // Topics
    @Query("SELECT * FROM topics WHERE subjectId = :subjectId ORDER BY orderIndex ASC, id ASC")
    fun getTopicsForSubject(subjectId: Long): Flow<List<Topic>>

    @Query("SELECT * FROM topics ORDER BY id ASC")
    fun getAllTopics(): Flow<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: Topic): Long

    @Update
    suspend fun updateTopic(topic: Topic)

    @Delete
    suspend fun deleteTopic(topic: Topic)

    @Query("DELETE FROM topics WHERE id = :id")
    suspend fun deleteTopicById(id: Long)

    // Study logs for tracking weekly studies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyLog(log: StudyLog): Long

    @Query("SELECT * FROM study_logs WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp ASC")
    fun getStudyLogsBetween(startTime: Long, endTime: Long): Flow<List<StudyLog>>

    @Query("SELECT * FROM study_logs ORDER BY timestamp DESC")
    fun getAllStudyLogs(): Flow<List<StudyLog>>

    // Settings (Target Date, etc.)
    @Query("SELECT * FROM settings WHERE `key` = :key LIMIT 1")
    fun getSetting(key: String): Flow<SettingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: SettingEntity)
}
