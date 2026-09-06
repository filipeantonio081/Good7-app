package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PurpleBar
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.PurpleDark
import com.example.ui.theme.PurpleGlassBg
import com.example.ui.theme.PurpleGlassBorder
import com.example.ui.theme.PurpleGlassLight
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
import com.example.ui.viewmodel.SubjectComparison
import com.example.ui.viewmodel.WeeklyDayStat
import com.example.ui.viewmodel.WeeklyStats

@Composable
fun StatisticsView(
    weeklyStats: WeeklyStats,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("statistics_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Week Title & Subtitle
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = PurpleVibrant,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "DESEMPENHO SEMANAL",
                        color = PurpleVibrant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = weeklyStats.weekLabel.ifEmpty { "Semana Atual" },
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Overview KPI Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total studies this week
                StatKpiCard(
                    title = "Estudos na Semana",
                    value = "${weeklyStats.totalWeeklyStudies}",
                    subtitle = "marcações",
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )

                // Active days
                StatKpiCard(
                    title = "Dias Ativos",
                    value = "${weeklyStats.activeDaysCount}/7",
                    subtitle = "dias com estudo",
                    icon = Icons.Default.LocalFireDepartment,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Weekly Daily Activity Bar Chart matching Design HTML: bg-purple-900/10, border-purple-500/20
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PurpleGlassLight),
                border = BorderStroke(1.dp, PurpleGlassBorder.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "DESEMPENHO SEMANAL",
                                color = PurpleVibrant,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${weeklyStats.totalWeeklyStudies} atividades",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = PurpleGlassBg,
                            border = BorderStroke(1.dp, PurpleGlassBorder)
                        ) {
                            Text(
                                text = "Seg - Dom",
                                color = PurpleVibrant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    DailyActivityBarChart(
                        dailyStats = weeklyStats.dailyStats,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )
                }
            }
        }

        // Comparative Bar Chart: Rendimento entre diferentes disciplinas
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = PurpleVibrant,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Comparativo de Rendimento",
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "% Concluído",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (weeklyStats.subjectComparisons.isEmpty()) {
                        Text(
                            text = "Nenhuma matéria cadastrada ainda.",
                            color = TextMuted,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            weeklyStats.subjectComparisons.forEach { comparison ->
                                SubjectComparisonRow(comparison = comparison)
                            }
                        }
                    }
                }
            }
        }

        // Stages distribution card (Teoria vs Revisões)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Divisão de Estudos Geral",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceCardElevated)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Teorias Concluídas",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${weeklyStats.totalTheoryDone}",
                                    color = TextWhite,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PurpleContainer)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Revisões (R1 a R7)",
                                    color = PurpleVibrant,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${weeklyStats.totalReviewsDone}",
                                    color = PurpleVibrant,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PurpleVibrant,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = TextSubtle,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun DailyActivityBarChart(
    dailyStats: List<WeeklyDayStat>,
    modifier: Modifier = Modifier
) {
    val maxCount = (dailyStats.maxOfOrNull { it.studyCount } ?: 0).coerceAtLeast(1)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        dailyStats.forEach { day ->
            val fraction = (day.studyCount.toFloat() / maxCount.toFloat()).coerceIn(0.05f, 1f)
            val animatedHeight by animateFloatAsState(
                targetValue = fraction,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "bar_height_${day.dayName}"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Count label
                Text(
                    text = if (day.studyCount > 0) "${day.studyCount}" else "",
                    color = if (day.isToday) PurpleVibrant else TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bar with Frosted Glass look: translucent purple or glowing purple
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(0.72f * animatedHeight)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(
                            if (day.isToday) {
                                Brush.verticalGradient(
                                    listOf(PurpleVibrant, PurpleSecondary)
                                )
                            } else if (day.studyCount > 0) {
                                Brush.verticalGradient(
                                    listOf(PurpleSecondary.copy(alpha = 0.85f), PurpleDark.copy(alpha = 0.6f))
                                )
                            } else {
                                Brush.verticalGradient(
                                    listOf(Color(0x1AFFFFFF), Color(0x0DFFFFFF))
                                )
                            }
                        )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Day name (SEG, TER, ...)
                Text(
                    text = day.dayName.uppercase(),
                    color = if (day.isToday) PurpleVibrant else TextSubtle,
                    fontSize = 10.sp,
                    fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium
                )

                Text(
                    text = day.dayOfMonth,
                    color = TextSubtle,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun SubjectComparisonRow(
    comparison: SubjectComparison
) {
    val fraction = (comparison.progressPercent.toFloat() / 100f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "comp_progress_${comparison.subjectId}"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = comparison.subjectName,
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${comparison.topicCount} assuntos • ${comparison.completedStages}/${comparison.totalStages} etapas",
                    color = TextSubtle,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${comparison.progressPercent}%",
                    color = if (comparison.progressPercent > 0) PurpleVibrant else TextMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                if (comparison.weeklyStudiesCount > 0) {
                    Text(
                        text = "+${comparison.weeklyStudiesCount} na semana",
                        color = PurpleVibrant,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal comparative bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceBorder.copy(alpha = 0.5f))
        ) {
            if (animatedProgress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(PurpleDark, PurplePrimary, PurpleVibrant)
                            )
                        )
                )
            }
        }
    }
}
