package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Topic
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PurpleGlassBg
import com.example.ui.theme.PurpleGlassBorder
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.PurpleSecondary
import com.example.ui.theme.PurpleVibrant
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceBorderHighlight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite

data class StageInfo(
    val index: Int,
    val shortLabel: String,
    val displaySymbol: String,
    val subLabel: String,
    val fullLabel: String,
    val isCompleted: Boolean
)

@Composable
fun TopicCard(
    topic: Topic,
    onToggleStage: (stageIndex: Int, isChecked: Boolean) -> Unit,
    onEditTopic: () -> Unit,
    onDeleteTopic: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val stages = remember(topic) {
        listOf(
            StageInfo(0, "Teoria", "T", "Teo", "Teoria Concluída", topic.theoryCompleted),
            StageInfo(1, "R1", "1", "R1", "Revisão 1", topic.rev1Completed),
            StageInfo(2, "R2", "2", "R2", "Revisão 2", topic.rev2Completed),
            StageInfo(3, "R3", "3", "R3", "Revisão 3", topic.rev3Completed),
            StageInfo(4, "R4", "4", "R4", "Revisão 4", topic.rev4Completed),
            StageInfo(5, "R5", "5", "R5", "Revisão 5", topic.rev5Completed),
            StageInfo(6, "R6", "6", "R6", "Revisão 6", topic.rev6Completed),
            StageInfo(7, "R7", "7", "R7", "Revisão 7", topic.rev7Completed)
        )
    }

    val isAllComplete = topic.completedCount == 8

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("topic_card_${topic.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(
            1.dp,
            if (isAllComplete) PurpleGlassBorder else SurfaceBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Topic Title, Completed Badge, Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topic.title,
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Frosted Glass Progress Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isAllComplete) PurpleGlassBg else Color(0x0FFFFFFF),
                    border = BorderStroke(
                        1.dp,
                        if (isAllComplete) PurpleGlassBorder else SurfaceBorder
                    )
                ) {
                    Text(
                        text = "${topic.completedCount}/8 CONCLUÍDOS",
                        color = if (isAllComplete) PurpleVibrant else TextSubtle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opções do assunto",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(SurfaceCardElevated)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar Nome", color = TextWhite) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = PurpleVibrant
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onEditTopic()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir Assunto", color = Color(0xFFEF4444)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444)
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onDeleteTopic()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 8-column Frosted Glass Stage Grid matching Design HTML
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                stages.forEach { stage ->
                    StageGlassChip(
                        stage = stage,
                        onToggle = { isChecked ->
                            onToggleStage(stage.index, isChecked)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun StageGlassChip(
    stage: StageInfo,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isChecked = stage.isCompleted

    val boxBg by animateColorAsState(
        targetValue = if (isChecked) PurpleSecondary else SurfaceCardElevated,
        animationSpec = tween(180),
        label = "stage_box_bg"
    )

    val boxBorder by animateColorAsState(
        targetValue = if (isChecked) PurplePrimary else SurfaceBorderHighlight,
        animationSpec = tween(180),
        label = "stage_box_border"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                role = Role.Checkbox,
                onClick = { onToggle(!isChecked) }
            )
            .testTag("stage_chip_${stage.shortLabel}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stage Symbol Box (T, 1, 2, ..., 7)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isChecked) {
                        Brush.verticalGradient(
                            listOf(PurpleVibrant, PurpleSecondary)
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(SurfaceCardElevated, Color(0xFF16161A))
                        )
                    }
                )
                .then(
                    if (!isChecked) {
                        Modifier.background(Color.Transparent)
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.matchParentSize(),
                shape = RoundedCornerShape(8.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, boxBorder)
            ) {}

            Text(
                text = stage.displaySymbol,
                color = if (isChecked) TextWhite else TextSubtle,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Stage Label underneath (Teo, R1, R2, ...)
        Text(
            text = stage.subLabel,
            color = if (isChecked) PurpleVibrant else TextSubtle,
            fontSize = 9.sp,
            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

