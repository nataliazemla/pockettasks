@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pockettasks.templates

import com.example.pockettasks.MainDispatcherRule
import com.example.pockettasks.data.FakeReadWriteTaskRepository
import com.example.pockettasks.data.FakeTemplatesReadRepository
import com.example.pockettasks.data.FakeTemplatesWriteRepository
import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.sort.SortStrategyFactory
import com.example.pockettasks.domain.sort.TaskSortStrategy
import com.example.pockettasks.domain.templates.FetchTemplatesUseCase
import com.example.pockettasks.domain.templates.MarkTemplateUsedUseCase
import com.example.pockettasks.domain.templates.Template
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import com.example.pockettasks.domain.validation.TaskTitleRule
import com.example.pockettasks.ui.TasksViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TemplatesOptimisticUpdateTest {

    @get:org.junit.Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `on mutation failure template is rolled back and error is exposed`() = runTest {
        val allowAllRule = TaskTitleRule { _ -> null }

        val tasksRepo = FakeReadWriteTaskRepository()
        val observe = ObserveTasksUseCase(tasksRepo, defaultSort = TaskSortStrategy { it })
        val add = AddTaskUseCase(tasksRepo, titleRules = listOf(allowAllRule))
        val toggle = ToggleTaskDoneUseCase(tasksRepo)

        val factory = SortStrategyFactory { option ->
            when (option) {
                SortOption.NEWEST -> TaskSortStrategy { it }
                SortOption.PRIORITY -> TaskSortStrategy { it.reversed() }
            }
        }

        val writeRepo = FakeTemplatesWriteRepository().apply {
            result = Result.failure(IllegalStateException("network"))
        }
        val markUsed = MarkTemplateUsedUseCase(writeRepo)

        val templatesReadRepo = FakeTemplatesReadRepository(
            Result.success(listOf(Template("1", "Temp", completed = false)))
        )
        val fetchTemplates = FetchTemplatesUseCase(templatesReadRepo)

        val vm = TasksViewModel(
            observeTasks = observe,
            addTask = add,
            toggleTaskDone = toggle,
            sortStrategyFactory = factory,
            fetchTemplates = fetchTemplates,
            markTemplateUsed = markUsed
        )

        vm.refreshTemplates()
        advanceUntilIdle()
        assertEquals(1, vm.state.value.templates.size)
        assertFalse(vm.state.value.templates.first().completed)

        vm.onTemplateClick("1", "Temp")
        advanceUntilIdle()

        // po failure -> rollback
        assertFalse(vm.state.value.templates.first().completed)

        assertTrue(vm.state.value.templatesError?.contains("network") == true)
        assertEquals("1", writeRepo.lastId)
        assertEquals(true, writeRepo.lastUsed)
    }
}