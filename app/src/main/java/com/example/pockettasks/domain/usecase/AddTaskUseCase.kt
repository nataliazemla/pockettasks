package com.example.pockettasks.domain.usecase

import com.example.pockettasks.domain.model.Priority
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.repo.TaskWriteRepository
import com.example.pockettasks.domain.validation.TaskTitleRule

class AddTaskUseCase(
    private val writeRepo: TaskWriteRepository,
    private val titleRules: List<TaskTitleRule>
) {
    suspend fun execute(title: String): Result<Unit> {
        val error = titleRules.firstNotNullOfOrNull { it.validate(title) }
        if (error != null) return Result.failure(IllegalArgumentException(error))

        val task = Task(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            isDone = false,
            priority = Priority.MEDIUM,
            createdAtMillis = System.currentTimeMillis()
        )
        writeRepo.add(task)
        return Result.success(Unit)
    }
}