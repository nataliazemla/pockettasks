package com.example.pockettasks.data

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.repo.TaskReadRepository
import com.example.pockettasks.domain.repo.TaskWriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Czytelne test-double:
 * - TaskReadRepository (ISP) osobno
 * - TaskWriteRepository (ISP) osobno
 * Pozwala testować use case’y bez zależności od InMemoryTaskRepository.
 */

class FakeTaskReadRepository(initial: List<Task> = emptyList()) : TaskReadRepository {
    private val state = MutableStateFlow(initial)
    override fun observeTasks(): Flow<List<Task>> = state

    fun set(tasks: List<Task>) {
        state.value = tasks
    }
}

class SpyTaskWriteRepository : TaskWriteRepository {
    val added = mutableListOf<Task>()
    val updated = mutableListOf<Task>()

    override suspend fun add(task: Task) {
        added += task
    }

    override suspend fun update(task: Task) {
        updated += task
    }
}

/**
 * Repo do testów VM (read+write w jednym), ale dalej przez interfejsy (DIP).
 */
class FakeReadWriteTaskRepository(initial: List<Task> = emptyList()) : TaskReadRepository, TaskWriteRepository {
    private val state = MutableStateFlow(initial)
    override fun observeTasks(): Flow<List<Task>> = state

    override suspend fun add(task: Task) {
        state.update { it + task }
    }

    override suspend fun update(task: Task) {
        state.update { list -> list.map { if (it.id == task.id) task else it } }
    }
}
