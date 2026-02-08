package com.example.pockettasks

import com.example.pockettasks.data.FakeTaskReadRepository
import com.example.pockettasks.domain.model.Priority
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.TaskSortStrategy
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveTasksUseCaseTest {

    @Test
    fun `execute applies provided sort strategy (OCP)`() = runTest {
        val t1 = Task(
            id = "1",
            title = "A",
            isDone = false,
            priority = Priority.LOW,
            createdAtMillis = 100
        )
        val t2 = Task(id = "2", title = "B", isDone = false, priority = Priority.HIGH, createdAtMillis = 200)
        val readRepo = FakeTaskReadRepository(initial = listOf(t1, t2))

        val identitySort = TaskSortStrategy { tasks -> tasks }
        val reverseSort = TaskSortStrategy { tasks -> tasks.reversed() }

        val useCase = ObserveTasksUseCase(readRepo = readRepo, defaultSort = identitySort)

        val result = useCase.execute(sort = reverseSort).first()

        assertEquals(listOf("2", "1"), result.map { it.id })
    }
}