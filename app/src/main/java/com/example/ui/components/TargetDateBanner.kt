package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.PurpleContainer
import com.example.ui.theme.PurpleGlassBg
import com.example.ui.theme.PurpleGlassBorder
import com.example.ui.theme.PurpleGlow
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.PurpleSecondary
import com.example.ui.theme.PurpleVibrant
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetDateBanner(
    targetDateEpochMillis: Long?,
    daysLeft: Long?,
    onSetTargetDate: (Long) -> Unit,
    onClearTargetDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("pt", "BR")) }

    // Frosted glass purple banner matching Design HTML: bg-purple-900/30, border-purple-500/30
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { showDatePicker = true }
            .testTag("target_date_banner"),
        shape = RoundedCornerShape(16.dp),
        color = PurpleGlassBg,
        border = BorderStroke(1.dp, PurpleGlassBorder)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x26A855F7),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PurpleGlassBorder.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Data de Objetivo",
                            tint = PurpleVibrant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "DATA DE OBJETIVO",
                            color = PurpleVibrant,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        if (targetDateEpochMillis != null) {
                            Text(
                                text = dateFormat.format(Date(targetDateEpochMillis)),
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = "Clique para definir sua meta",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                if (targetDateEpochMillis != null && daysLeft != null) {
                    // Countdown badge with frosted purple pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (daysLeft >= 0) PurpleSecondary else Color(0x33EF4444),
                        border = BorderStroke(
                            1.dp,
                            if (daysLeft >= 0) PurpleGlassBorder else Color(0x66EF4444)
                        )
                    ) {
                        Text(
                            text = when {
                                daysLeft > 1 -> "Faltam $daysLeft dias"
                                daysLeft == 1L -> "Falta 1 dia!"
                                daysLeft == 0L -> "É HOJE!"
                                else -> "Finalizado"
                            },
                            color = TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PurpleGlassBg,
                        border = BorderStroke(1.dp, PurpleGlassBorder)
                    ) {
                        Text(
                            text = "+ Definir",
                            color = PurpleVibrant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = targetDateEpochMillis ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = SurfaceCard
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onSetTargetDate(it)
                        }
                        showDatePicker = false
                    },
                    modifier = Modifier.testTag("confirm_target_date_button")
                ) {
                    Text("Definir", color = PurpleVibrant, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Row {
                    if (targetDateEpochMillis != null) {
                        TextButton(
                            onClick = {
                                onClearTargetDate()
                                showDatePicker = false
                            },
                            modifier = Modifier.testTag("clear_target_date_button")
                        ) {
                            Text("Remover", color = Color(0xFFEF4444))
                        }
                    }
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar", color = TextMuted)
                    }
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = SurfaceCard,
                    titleContentColor = TextWhite,
                    headlineContentColor = PurpleVibrant,
                    weekdayContentColor = TextMuted,
                    subheadContentColor = TextWhite,
                    yearContentColor = TextWhite,
                    currentYearContentColor = PurpleVibrant,
                    selectedYearContentColor = TextWhite,
                    selectedYearContainerColor = PurpleSecondary,
                    dayContentColor = TextWhite,
                    disabledDayContentColor = TextMuted.copy(alpha = 0.38f),
                    selectedDayContentColor = TextWhite,
                    disabledSelectedDayContentColor = TextWhite.copy(alpha = 0.38f),
                    selectedDayContainerColor = PurpleSecondary,
                    disabledSelectedDayContainerColor = PurpleSecondary.copy(alpha = 0.38f),
                    todayContentColor = PurpleVibrant,
                    todayDateBorderColor = PurpleVibrant
                )
            )
        }
    }
}

