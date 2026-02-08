package com.example.pockettasks

import com.example.pockettasks.data.SpyTaskWriteRepository
import com.example.pockettasks.domain.model.Priority
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleTaskDoneUseCaseTest {

    @Test
    fun `toggle flips isDone and writes update`() = runTest {
        val writeRepo = SpyTaskWriteRepository()
        val useCase = ToggleTaskDoneUseCase(writeRepo = writeRepo)

        val task = Task(
            id = "1",
            title = "X",
            isDone = false,
            priority = Priority.MEDIUM,
            createdAtMillis = 0
        )

        useCase.execute(task)

        assertEquals(1, writeRepo.updated.size)
        assertTrue(writeRepo.updated.first().isDone)
    }
}