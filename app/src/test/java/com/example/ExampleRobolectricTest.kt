package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Good7", appName)
    }

    @Test
    fun `test topic completion count and progress`() {
        val topic = Topic(
            subjectId = 1L,
            title = "Direitos Fundamentais",
            theoryCompleted = true,
            rev1Completed = true,
            rev2Completed = true,
            rev3Completed = false,
            rev4Completed = false,
            rev5Completed = false,
            rev6Completed = false,
            rev7Completed = false
        )

        assertEquals(3, topic.completedCount)
        assertEquals(8, topic.totalStages)
        assertEquals(3f / 8f, topic.progress, 0.001f)
    }

    @Test
    fun `test topic all stages completed`() {
        val topic = Topic(
            subjectId = 1L,
            title = "Controle de Constitucionalidade",
            theoryCompleted = true,
            rev1Completed = true,
            rev2Completed = true,
            rev3Completed = true,
            rev4Completed = true,
            rev5Completed = true,
            rev6Completed = true,
            rev7Completed = true
        )

        assertEquals(8, topic.completedCount)
        assertEquals(1f, topic.progress, 0.001f)
    }
}
