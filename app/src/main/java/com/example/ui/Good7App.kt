package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Subject
import com.example.data.model.Topic
import com.example.ui.components.AddSubjectDialog
import com.example.ui.components.AddTopicDialog
import com.example.ui.components.DeleteConfirmDialog
import com.example.ui.components.StatisticsView
import com.example.ui.components.SubjectProgressBar
import com.example.ui.components.TargetDateBanner
import com.example.ui.components.TopicCard
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.PurpleSecondary
import com.example.ui.theme.PurpleVibrant
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MainAppTab
import com.example.ui.viewmodel.StudyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Good7App(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog states
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var subjectToEdit by remember { mutableStateOf<Subject?>(null) }
    var subjectToDelete by remember { mutableStateOf<Subject?>(null) }

    var showAddTopicDialog by remember { mutableStateOf(false) }
    var topicToEdit by remember { mutableStateOf<Topic?>(null) }
    var topicToDelete by remember { mutableStateOf<Topic?>(null) }

    var subjectMenuExpanded by remember { mutableStateOf(false) }

    val currentSubject = uiState.subjects.find { it.id == uiState.selectedSubjectId }

    // Progress metrics for currently selected subject
    val totalStagesForSubject = uiState.topics.size * 8
    val completedStagesForSubject = uiState.topics.sumOf { it.completedCount }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("good7_scaffold"),
        containerColor = PitchBlack,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PitchBlack)
            ) {
                // Main Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo & App Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(PurpleSecondary, PurplePrimary, PurpleVibrant)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "7",
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Good",
                                color = TextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "7",
                                color = PurplePrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Top Mode Switcher Pill: [ Matérias ] [ Estatísticas ]
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceCard,
                        border = BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Row(modifier = Modifier.padding(3.dp)) {
                            // Matérias Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (uiState.currentTab == MainAppTab.SUBJECTS) PurpleSecondary else Color.Transparent
                                    )
                                    .clickable { viewModel.selectTab(MainAppTab.SUBJECTS) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("tab_button_subjects"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Matérias",
                                    color = if (uiState.currentTab == MainAppTab.SUBJECTS) TextWhite else TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Estatísticas Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (uiState.currentTab == MainAppTab.STATISTICS) PurpleSecondary else Color.Transparent
                                    )
                                    .clickable { viewModel.selectTab(MainAppTab.STATISTICS) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("tab_button_statistics"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Estatísticas",
                                    color = if (uiState.currentTab == MainAppTab.STATISTICS) TextWhite else TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // Data de Objetivo - Exposta no topo da home do app em cor roxa!
                TargetDateBanner(
                    targetDateEpochMillis = uiState.targetDateEpochMillis,
                    daysLeft = uiState.targetDateDaysLeft,
                    onSetTargetDate = { viewModel.setTargetDate(it) },
                    onClearTargetDate = { viewModel.clearTargetDate() },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        },
        floatingActionButton = {
            if (uiState.currentTab == MainAppTab.SUBJECTS && currentSubject != null) {
                FloatingActionButton(
                    onClick = { showAddTopicDialog = true },
                    containerColor = PurpleSecondary,
                    contentColor = TextWhite,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("fab_add_topic")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Assunto")
                        Text(
                            text = "Novo Assunto",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            } else {
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_content_transition"
                ) { tab ->
                    when (tab) {
                        MainAppTab.SUBJECTS -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                // Subject Tabs Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(PitchBlack),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (uiState.subjects.isNotEmpty()) {
                                        val selectedIndex = uiState.subjects.indexOfFirst {
                                            it.id == uiState.selectedSubjectId
                                        }.coerceAtLeast(0)

                                        ScrollableTabRow(
                                            selectedTabIndex = selectedIndex,
                                            containerColor = PitchBlack,
                                            contentColor = PurpleVibrant,
                                            edgePadding = 16.dp,
                                            indicator = { tabPositions ->
                                                if (selectedIndex < tabPositions.size) {
                                                    TabRowDefaults.SecondaryIndicator(
                                                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                                        height = 2.dp,
                                                        color = PurpleVibrant
                                                    )
                                                }
                                            },
                                            divider = {},
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            uiState.subjects.forEachIndexed { index, subject ->
                                                val isSelected = subject.id == uiState.selectedSubjectId
                                                Tab(
                                                    selected = isSelected,
                                                    onClick = { viewModel.selectSubject(subject.id) },
                                                    text = {
                                                        Text(
                                                            text = subject.name,
                                                            color = if (isSelected) TextWhite else TextMuted,
                                                            fontSize = 14.sp,
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    },
                                                    modifier = Modifier.testTag("subject_tab_${subject.id}")
                                                )
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }

                                    // Button to Add New Subject (+ Aba)
                                    IconButton(
                                        onClick = { showAddSubjectDialog = true },
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(SurfaceCard)
                                            .testTag("add_subject_tab_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Criar Nova Matéria",
                                            tint = PurpleVibrant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                if (currentSubject != null) {
                                    // Header of the active subject: actions & Long thin purple progress bar
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(PitchBlack)
                                            .padding(horizontal = 16.dp, vertical = 6.dp)
                                    ) {
                                        // Subject title & Manage menu
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = currentSubject.name,
                                                color = TextWhite,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )

                                            Box {
                                                IconButton(
                                                    onClick = { subjectMenuExpanded = true },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.MoreVert,
                                                        contentDescription = "Ações da matéria",
                                                        tint = TextMuted,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }

                                                DropdownMenu(
                                                    expanded = subjectMenuExpanded,
                                                    onDismissRequest = { subjectMenuExpanded = false },
                                                    modifier = Modifier.background(SurfaceCardElevated)
                                                ) {
                                                    DropdownMenuItem(
                                                        text = { Text("Renomear Matéria", color = TextWhite) },
                                                        leadingIcon = {
                                                            Icon(
                                                                Icons.Default.Edit,
                                                                contentDescription = null,
                                                                tint = PurpleVibrant
                                                            )
                                                        },
                                                        onClick = {
                                                            subjectMenuExpanded = false
                                                            subjectToEdit = currentSubject
                                                        }
                                                    )
                                                    DropdownMenuItem(
                                                        text = { Text("Excluir Matéria", color = Color(0xFFEF4444)) },
                                                        leadingIcon = {
                                                            Icon(
                                                                Icons.Default.DeleteOutline,
                                                                contentDescription = null,
                                                                tint = Color(0xFFEF4444)
                                                            )
                                                        },
                                                        onClick = {
                                                            subjectMenuExpanded = false
                                                            subjectToDelete = currentSubject
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Long thin purple bar at the top of the tab that fills with study progress
                                        SubjectProgressBar(
                                            completedStages = completedStagesForSubject,
                                            totalStages = totalStagesForSubject
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Topics list inside this subject
                                    if (uiState.topics.isEmpty()) {
                                        // Empty state
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .clip(CircleShape)
                                                        .background(PurpleContainer),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.LibraryBooks,
                                                        contentDescription = null,
                                                        tint = PurpleVibrant,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(16.dp))

                                                Text(
                                                    text = "Nenhum assunto cadastrado",
                                                    color = TextWhite,
                                                    fontSize = 17.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )

                                                Spacer(modifier = Modifier.height(6.dp))

                                                Text(
                                                    text = "Adicione pontos do edital ou temas para controlar suas 7 revisões.",
                                                    color = TextMuted,
                                                    fontSize = 13.sp,
                                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                                )

                                                Spacer(modifier = Modifier.height(20.dp))

                                                Button(
                                                    onClick = { showAddTopicDialog = true },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = PurplePrimary,
                                                        contentColor = TextWhite
                                                    ),
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier.testTag("add_first_topic_button")
                                                ) {
                                                    Icon(Icons.Default.Add, contentDescription = null)
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("Adicionar Primeiro Assunto")
                                                }
                                            }
                                        }
                                    } else {
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp)
                                                .testTag("topics_list"),
                                            contentPadding = PaddingValues(bottom = 96.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(
                                                items = uiState.topics,
                                                key = { it.id }
                                            ) { topic ->
                                                TopicCard(
                                                    topic = topic,
                                                    onToggleStage = { stageIndex, isChecked ->
                                                        viewModel.toggleTopicStage(topic, stageIndex, isChecked)
                                                    },
                                                    onEditTopic = { topicToEdit = topic },
                                                    onDeleteTopic = { topicToDelete = topic }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // No subjects at all
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "Comece criando uma Matéria",
                                                color = TextWhite,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Crie uma aba para cada disciplina do seu concurso ou vestibular.",
                                                color = TextMuted,
                                                fontSize = 13.sp,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.height(20.dp))
                                            Button(
                                                onClick = { showAddSubjectDialog = true },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = PurplePrimary,
                                                    contentColor = TextWhite
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = null)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("Criar Nova Matéria")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        MainAppTab.STATISTICS -> {
                            StatisticsView(
                                weeklyStats = uiState.weeklyStats,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Subject Dialog
    if (showAddSubjectDialog) {
        AddSubjectDialog(
            onDismiss = { showAddSubjectDialog = false },
            onConfirm = { name ->
                viewModel.addSubject(name)
            }
        )
    }

    // Edit Subject Dialog
    subjectToEdit?.let { subject ->
        AddSubjectDialog(
            initialName = subject.name,
            isEditing = true,
            onDismiss = { subjectToEdit = null },
            onConfirm = { newName ->
                viewModel.updateSubject(subject.copy(name = newName))
            }
        )
    }

    // Delete Subject Confirmation Dialog
    subjectToDelete?.let { subject ->
        DeleteConfirmDialog(
            title = "Excluir Matéria",
            message = "Tem certeza que deseja excluir a matéria '${subject.name}' e todos os assuntos dentro dela? Essa ação não pode ser desfeita.",
            onDismiss = { subjectToDelete = null },
            onConfirm = {
                viewModel.deleteSubject(subject.id)
            }
        )
    }

    // Add Topic Dialog
    if (showAddTopicDialog && currentSubject != null) {
        AddTopicDialog(
            onDismiss = { showAddTopicDialog = false },
            onConfirm = { title ->
                viewModel.addTopic(currentSubject.id, title)
            }
        )
    }

    // Edit Topic Dialog
    topicToEdit?.let { topic ->
        AddTopicDialog(
            initialTitle = topic.title,
            isEditing = true,
            onDismiss = { topicToEdit = null },
            onConfirm = { newTitle ->
                viewModel.updateTopic(topic.copy(title = newTitle))
            }
        )
    }

    // Delete Topic Confirmation Dialog
    topicToDelete?.let { topic ->
        DeleteConfirmDialog(
            title = "Excluir Assunto",
            message = "Tem certeza que deseja excluir o assunto '${topic.title}'?",
            onDismiss = { topicToDelete = null },
            onConfirm = {
                viewModel.deleteTopic(topic.id)
            }
        )
    }
}
