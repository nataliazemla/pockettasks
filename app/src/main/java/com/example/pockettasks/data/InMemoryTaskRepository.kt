package com.example.pockettasks.data

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.repo.TaskReadRepository
import com.example.pockettasks.domain.repo.TaskWriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class InMemoryTaskRepository : TaskReadRepository, TaskWriteRepository {

    private val tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun observeTasks(): Flow<List<Task>> = tasks

    override suspend fun add(task: Task) {
        tasks.update { current -> current + task }
    }

    override suspend fun update(task: Task) {
        tasks.update { current -> current.map { if (it.id == task.id) task else it } }
    }
}