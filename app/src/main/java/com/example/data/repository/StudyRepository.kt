package com.example.data.repository

import com.example.data.local.StudyDao
import com.example.data.model.SettingEntity
import com.example.data.model.StudyLog
import com.example.data.model.Subject
import com.example.data.model.Topic
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val dao: StudyDao) {

    val allSubjects: Flow<List<Subject>> = dao.getAllSubjects()
    val allTopics: Flow<List<Topic>> = dao.getAllTopics()
    val allLogs: Flow<List<StudyLog>> = dao.getAllStudyLogs()

    fun getTopicsForSubject(subjectId: Long): Flow<List<Topic>> = dao.getTopicsForSubject(subjectId)

    fun getWeeklyLogs(startTime: Long, endTime: Long): Flow<List<StudyLog>> =
        dao.getStudyLogsBetween(startTime, endTime)

    fun getTargetDate(): Flow<SettingEntity?> = dao.getSetting("TARGET_DATE")

    suspend fun setTargetDate(dateEpochMillis: Long) {
        dao.saveSetting(SettingEntity(key = "TARGET_DATE", value = dateEpochMillis.toString()))
    }

    suspend fun clearTargetDate() {
        dao.saveSetting(SettingEntity(key = "TARGET_DATE", value = ""))
    }

    suspend fun createSubject(name: String): Long {
        val subject = Subject(name = name.trim())
        return dao.insertSubject(subject)
    }

    suspend fun updateSubject(subject: Subject) {
        dao.updateSubject(subject)
    }

    suspend fun deleteSubject(subjectId: Long) {
        dao.deleteSubjectById(subjectId)
    }

    suspend fun createTopic(subjectId: Long, title: String): Long {
        val topic = Topic(
            subjectId = subjectId,
            title = title.trim()
        )
        return dao.insertTopic(topic)
    }

    suspend fun updateTopic(topic: Topic) {
        dao.updateTopic(topic)
    }

    suspend fun deleteTopic(topicId: Long) {
        dao.deleteTopicById(topicId)
    }

    suspend fun toggleTopicStage(topic: Topic, stageIndex: Int, isChecked: Boolean) {
        val stageName = when (stageIndex) {
            0 -> "Teoria"
            1 -> "Revisão 1"
            2 -> "Revisão 2"
            3 -> "Revisão 3"
            4 -> "Revisão 4"
            5 -> "Revisão 5"
            6 -> "Revisão 6"
            7 -> "Revisão 7"
            else -> "Estudo"
        }

        val updatedTopic = when (stageIndex) {
            0 -> topic.copy(theoryCompleted = isChecked, updatedAt = System.currentTimeMillis())
            1 -> topic.copy(rev1Completed = isChecked, updatedAt = System.currentTimeMillis())
            2 -> topic.copy(rev2Completed = isChecked, updatedAt = System.currentTimeMillis())
            3 -> topic.copy(rev3Completed = isChecked, updatedAt = System.currentTimeMillis())
            4 -> topic.copy(rev4Completed = isChecked, updatedAt = System.currentTimeMillis())
            5 -> topic.copy(rev5Completed = isChecked, updatedAt = System.currentTimeMillis())
            6 -> topic.copy(rev6Completed = isChecked, updatedAt = System.currentTimeMillis())
            7 -> topic.copy(rev7Completed = isChecked, updatedAt = System.currentTimeMillis())
            else -> topic
        }

        dao.updateTopic(updatedTopic)

        // Log study action for weekly analytics
        val log = StudyLog(
            topicId = topic.id,
            subjectId = topic.subjectId,
            stageName = stageName,
            isChecked = isChecked,
            timestamp = System.currentTimeMillis()
        )
        dao.insertStudyLog(log)
    }

    suspend fun seedInitialDataIfEmpty() {
        // Will be called by ViewModel if subjects list is empty to provide a welcoming start
    }
}
