package com.example.pockettasks

import com.example.pockettasks.data.SpyTaskWriteRepository
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.validation.NonBlankTitleRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddTaskUseCaseTest {

    @Test
    fun `blank title returns failure and does not write to repo`() = runTest {
        val writeRepo = SpyTaskWriteRepository()
        val useCase = AddTaskUseCase(
            writeRepo = writeRepo,
            titleRules = listOf(NonBlankTitleRule())
        )

        val result = useCase.execute("   ")

        assertTrue(result.isFailure)
        assertEquals(0, writeRepo.added.size)
        assertEquals(0, writeRepo.updated.size)
    }

    @Test
    fun `valid title writes a task to repo`() = runTest {
        val writeRepo = SpyTaskWriteRepository()
        val useCase = AddTaskUseCase(
            writeRepo = writeRepo,
            titleRules = listOf(NonBlankTitleRule())
        )

        val result = useCase.execute("Buy milk")

        assertTrue(result.isSuccess)
        assertEquals(1, writeRepo.added.size)
        assertEquals("Buy milk", writeRepo.added.first().title)
    }
}
