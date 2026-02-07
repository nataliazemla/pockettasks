package com.example.pockettasks.domain.usecase

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.repo.TaskWriteRepository

class ToggleTaskDoneUseCase(
    private val writeRepo: TaskWriteRepository
) {
    suspend fun execute(task: Task) {
        writeRepo.update(task.copy(isDone = !task.isDone))
    }
}