package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.StudyLog
import com.example.data.model.Subject
import com.example.data.model.Topic
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class MainAppTab {
    SUBJECTS,
    STATISTICS
}

data class WeeklyDayStat(
    val dayName: String, // Seg, Ter, Qua, Qui, Sex, Sáb, Dom
    val dayOfMonth: String, // 06/09
    val dateMillis: Long,
    val studyCount: Int,
    val isToday: Boolean
)

data class SubjectComparison(
    val subjectId: Long,
    val subjectName: String,
    val topicCount: Int,
    val completedStages: Int,
    val totalStages: Int,
    val progressPercent: Int,
    val weeklyStudiesCount: Int
)

data class WeeklyStats(
    val weekLabel: String,
    val totalWeeklyStudies: Int,
    val activeDaysCount: Int,
    val topSubjectName: String?,
    val dailyStats: List<WeeklyDayStat>,
    val subjectComparisons: List<SubjectComparison>,
    val totalTheoryDone: Int,
    val totalReviewsDone: Int
)

data class StudyUiState(
    val subjects: List<Subject> = emptyList(),
    val selectedSubjectId: Long? = null,
    val topics: List<Topic> = emptyList(), // topics for selected subject
    val allTopics: List<Topic> = emptyList(),
    val targetDateEpochMillis: Long? = null,
    val targetDateDaysLeft: Long? = null,
    val weeklyStats: WeeklyStats = WeeklyStats(
        weekLabel = "",
        totalWeeklyStudies = 0,
        activeDaysCount = 0,
        topSubjectName = null,
        dailyStats = emptyList(),
        subjectComparisons = emptyList(),
        totalTheoryDone = 0,
        totalReviewsDone = 0
    ),
    val currentTab: MainAppTab = MainAppTab.SUBJECTS,
    val isLoading: Boolean = false
)

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudyRepository
    private val _selectedSubjectId = MutableStateFlow<Long?>(null)
    private val _currentTab = MutableStateFlow(MainAppTab.SUBJECTS)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = StudyRepository(db.studyDao())

        // Seed starter subjects if database is fresh
        viewModelScope.launch {
            checkAndSeedDefaults()
        }
    }

    private suspend fun checkAndSeedDefaults() {
        // Observe once if empty
        val db = AppDatabase.getDatabase(getApplication())
        val initialSubjects = db.studyDao().getSubjectById(1L)
        if (initialSubjects == null) {
            // Seed 2 default subjects so the user sees a complete, interactive app immediately
            val matId = repository.createSubject("Direito Constitucional")
            repository.createTopic(matId, "Direitos e Garantias Fundamentais")
            repository.createTopic(matId, "Organização do Estado")

            val portId = repository.createSubject("Língua Portuguesa")
            repository.createTopic(portId, "Interpretação de Texto")
            repository.createTopic(portId, "Concordância e Regência")
        }
    }

    fun selectTab(tab: MainAppTab) {
        _currentTab.value = tab
    }

    fun selectSubject(subjectId: Long) {
        _selectedSubjectId.value = subjectId
    }

    fun addSubject(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val newId = repository.createSubject(name)
            _selectedSubjectId.value = newId
        }
    }

    fun updateSubject(subject: Subject) {
        viewModelScope.launch {
            repository.updateSubject(subject)
        }
    }

    fun deleteSubject(subjectId: Long) {
        viewModelScope.launch {
            repository.deleteSubject(subjectId)
        }
    }

    fun addTopic(subjectId: Long, title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.createTopic(subjectId, title)
        }
    }

    fun updateTopic(topic: Topic) {
        viewModelScope.launch {
            repository.updateTopic(topic)
        }
    }

    fun deleteTopic(topicId: Long) {
        viewModelScope.launch {
            repository.deleteTopic(topicId)
        }
    }

    fun toggleTopicStage(topic: Topic, stageIndex: Int, isChecked: Boolean) {
        viewModelScope.launch {
            repository.toggleTopicStage(topic, stageIndex, isChecked)
        }
    }

    fun setTargetDate(epochMillis: Long) {
        viewModelScope.launch {
            repository.setTargetDate(epochMillis)
        }
    }

    fun clearTargetDate() {
        viewModelScope.launch {
            repository.clearTargetDate()
        }
    }

    private data class RepositoryData(
        val subjects: List<Subject>,
        val allTopics: List<Topic>,
        val logs: List<StudyLog>,
        val targetDateSetting: com.example.data.model.SettingEntity?
    )

    private val repositoryDataFlow = combine(
        repository.allSubjects,
        repository.allTopics,
        repository.allLogs,
        repository.getTargetDate()
    ) { subjects, allTopics, logs, targetDateSetting ->
        RepositoryData(subjects, allTopics, logs, targetDateSetting)
    }

    // Combine flows to produce single reactive UiState
    val uiState: StateFlow<StudyUiState> = combine(
        repositoryDataFlow,
        _selectedSubjectId,
        _currentTab
    ) { repoData, selectedSubId, currentTab ->
        val subjects = repoData.subjects
        val allTopics = repoData.allTopics
        val logs = repoData.logs
        val targetDateSetting = repoData.targetDateSetting

        val actualSelectedId = if (subjects.isEmpty()) {
            null
        } else if (selectedSubId != null && subjects.any { it.id == selectedSubId }) {
            selectedSubId
        } else {
            subjects.firstOrNull()?.id
        }

        val topicsForSelected = if (actualSelectedId != null) {
            allTopics.filter { it.subjectId == actualSelectedId }
        } else {
            emptyList()
        }

        // Calculate Target Date and Days Left
        val targetMillis = targetDateSetting?.value?.toLongOrNull()
        val daysLeft = targetMillis?.let { calculateDaysLeft(it) }

        // Calculate Weekly Statistics
        val weeklyStats = calculateWeeklyStatistics(subjects, allTopics, logs)

        StudyUiState(
            subjects = subjects,
            selectedSubjectId = actualSelectedId,
            topics = topicsForSelected,
            allTopics = allTopics,
            targetDateEpochMillis = targetMillis,
            targetDateDaysLeft = daysLeft,
            weeklyStats = weeklyStats,
            currentTab = currentTab,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StudyUiState(isLoading = true)
    )

    private fun calculateDaysLeft(targetMillis: Long): Long {
        val targetCal = Calendar.getInstance().apply {
            timeInMillis = targetMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val diff = targetCal.timeInMillis - todayCal.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    private fun calculateWeeklyStatistics(
        subjects: List<Subject>,
        allTopics: List<Topic>,
        logs: List<StudyLog>
    ): WeeklyStats {
        val cal = Calendar.getInstance()
        val todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        // Set to start of this week (Monday)
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfWeek = cal.timeInMillis

        // End of week (Sunday 23:59:59)
        val calEnd = Calendar.getInstance().apply {
            timeInMillis = startOfWeek
            add(Calendar.DAY_OF_YEAR, 6)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endOfWeek = calEnd.timeInMillis

        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val weekLabelFormat = SimpleDateFormat("dd 'de' MMM", Locale("pt", "BR"))
        val weekLabel = "${weekLabelFormat.format(startOfWeek)} - ${weekLabelFormat.format(endOfWeek)}"

        // Filter logs within this week
        val weeklyLogs = logs.filter { it.timestamp in startOfWeek..endOfWeek }

        // Studies marked this week (isChecked = true is an action completed; we count positive actions)
        val weeklyCompletedLogs = weeklyLogs.filter { it.isChecked }
        val totalWeeklyStudies = weeklyCompletedLogs.size

        // Build daily stats for Mon..Sun
        val dayNames = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
        val dailyStats = mutableListOf<WeeklyDayStat>()
        val dayCal = Calendar.getInstance().apply { timeInMillis = startOfWeek }
        val nowCal = Calendar.getInstance()

        var activeDays = 0

        for (i in 0..6) {
            val dayStart = dayCal.timeInMillis
            val dayEnd = dayStart + 24 * 60 * 60 * 1000 - 1

            val countForDay = weeklyCompletedLogs.count { it.timestamp in dayStart..dayEnd }
            val isToday = dayCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
                    dayCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

            if (countForDay > 0) {
                activeDays++
            }

            dailyStats.add(
                WeeklyDayStat(
                    dayName = dayNames[i],
                    dayOfMonth = dateFormat.format(dayStart),
                    dateMillis = dayStart,
                    studyCount = countForDay,
                    isToday = isToday
                )
            )
            dayCal.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Subject comparisons (evolution and rendimento across disciplines)
        val subjectComparisons = subjects.map { subject ->
            val topicsForSub = allTopics.filter { it.subjectId == subject.id }
            val completedStages = topicsForSub.sumOf { it.completedCount }
            val totalStages = topicsForSub.size * 8
            val progressPercent = if (totalStages > 0) {
                ((completedStages.toFloat() / totalStages.toFloat()) * 100).toInt()
            } else {
                0
            }

            val weeklyCount = weeklyCompletedLogs.count { it.subjectId == subject.id }

            SubjectComparison(
                subjectId = subject.id,
                subjectName = subject.name,
                topicCount = topicsForSub.size,
                completedStages = completedStages,
                totalStages = totalStages,
                progressPercent = progressPercent,
                weeklyStudiesCount = weeklyCount
            )
        }

        // Top studied subject this week
        val topSubject = subjectComparisons.filter { it.weeklyStudiesCount > 0 }
            .maxByOrNull { it.weeklyStudiesCount }?.subjectName

        // Theory vs Review totals across all topics
        val totalTheory = allTopics.count { it.theoryCompleted }
        val totalReviews = allTopics.sumOf {
            listOf(
                it.rev1Completed,
                it.rev2Completed,
                it.rev3Completed,
                it.rev4Completed,
                it.rev5Completed,
                it.rev6Completed,
                it.rev7Completed
            ).count { check -> check }
        }

        return WeeklyStats(
            weekLabel = weekLabel,
            totalWeeklyStudies = totalWeeklyStudies,
            activeDaysCount = activeDays,
            topSubjectName = topSubject,
            dailyStats = dailyStats,
            subjectComparisons = subjectComparisons,
            totalTheoryDone = totalTheory,
            totalReviewsDone = totalReviews
        )
    }
}
