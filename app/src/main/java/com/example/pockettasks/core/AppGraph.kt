package com.example.pockettasks.core

import com.example.pockettasks.data.InMemoryTaskRepository
import com.example.pockettasks.data.templates.ApolloClientProvider
import com.example.pockettasks.data.templates.ApolloTemplatesRepository
import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.templates.FetchTemplatesUseCase
import com.example.pockettasks.domain.templates.MarkTemplateUsedUseCase
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import com.example.pockettasks.domain.validation.NonBlankTitleRule

object AppGraph {
    // Data
    private val repo = InMemoryTaskRepository()
    // Domain - rules/strategies
    private val titleRules = listOf(NonBlankTitleRule())
    private val apolloClient = ApolloClientProvider.create()
    private val templatesRepo = ApolloTemplatesRepository(apolloClient)

    val fetchTemplatesUseCase = FetchTemplatesUseCase(templatesRepo)
    val markTemplateUsedUseCase = MarkTemplateUsedUseCase(templatesRepo)
    val sortFactory = DefaultSortStrategyFactory()

    // Usecases
    val observeTasksUseCase = ObserveTasksUseCase(
        readRepo = repo,
        defaultSort = sortFactory.create(SortOption.NEWEST)
    )

    val addTaskUseCase = AddTaskUseCase(
        writeRepo = repo,
        titleRules = titleRules
    )

    val toggleTaskDoneUseCase = ToggleTaskDoneUseCase(
        writeRepo = repo
    )
}