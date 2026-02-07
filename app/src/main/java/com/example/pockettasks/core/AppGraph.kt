package com.example.pockettasks.core

import com.example.pockettasks.data.InMemoryTaskRepository
import com.example.pockettasks.domain.sort.SortByCreatedDesc
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import com.example.pockettasks.domain.validation.NonBlankTitleRule

object AppGraph {
    // Data
    private val repo = InMemoryTaskRepository()

    // Domain - rules/strategies
    private val titleRules = listOf(NonBlankTitleRule())

    // Usecases
    val observeTasksUseCase = ObserveTasksUseCase(
        readRepo = repo,
        defaultSort = SortByCreatedDesc()
    )

    val addTaskUseCase = AddTaskUseCase(
        writeRepo = repo,
        titleRules = titleRules
    )

    val toggleTaskDoneUseCase = ToggleTaskDoneUseCase(
        writeRepo = repo
    )
}