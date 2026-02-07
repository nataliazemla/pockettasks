package com.example.pockettasks.domain.usecase

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.repo.TaskReadRepository
import com.example.pockettasks.domain.sort.TaskSortStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveTasksUseCase(
    private val readRepo: TaskReadRepository,
    private val defaultSort: TaskSortStrategy
) {
    fun execute(sort: TaskSortStrategy = defaultSort): Flow<List<Task>> =
        readRepo.observeTasks().map(sort::sort)
}