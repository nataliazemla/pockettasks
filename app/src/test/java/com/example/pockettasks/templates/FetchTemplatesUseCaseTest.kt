package com.example.pockettasks.templates

import com.example.pockettasks.data.FakeTemplatesReadRepository
import com.example.pockettasks.domain.templates.FetchTemplatesUseCase
import com.example.pockettasks.domain.templates.Template
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FetchTemplatesUseCaseTest {

    @Test
    fun `returns templates from repository`() = runTest {
        val expected = listOf(
            Template(id = "1", title = "T1", completed = false),
            Template(id = "2", title = "T2", completed = true),
        )
        val repo = FakeTemplatesReadRepository(Result.success(expected))
        val useCase = FetchTemplatesUseCase(repo)

        val result = useCase.execute()

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }
}