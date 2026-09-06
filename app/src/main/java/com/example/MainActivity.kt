package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.ui.Good7App
import com.example.ui.theme.Good7Theme
import com.example.ui.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {

    private val studyViewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Good7Theme {
                Good7App(
                    viewModel = studyViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
