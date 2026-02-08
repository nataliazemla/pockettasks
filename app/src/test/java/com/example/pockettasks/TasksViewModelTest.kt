package com.example.pockettasks

import com.example.pockettasks.data.FakeReadWriteTaskRepository
import com.example.pockettasks.domain.model.Priority
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.sort.SortStrategyFactory
import com.example.pockettasks.domain.sort.TaskSortStrategy
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import com.example.pockettasks.domain.validation.TaskTitleRule
import com.example.pockettasks.ui.TasksViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TasksViewModelTest {

    @Test
    fun `changing sort option changes tasks order via injected factory (DIP)`() = runTest {
        val t1 = Task("1", "A", false, Priority.MEDIUM, 100)
        val t2 = Task("2", "B", false, Priority.MEDIUM, 200)
        val repo = FakeReadWriteTaskRepository(initial = listOf(t1, t2))

        val identity = TaskSortStrategy { it }
        val observe = ObserveTasksUseCase(readRepo = repo, defaultSort = identity)
        val allowAllRule = TaskTitleRule { _ -> null }

        val add = AddTaskUseCase(
            writeRepo = repo,
            titleRules = listOf(allowAllRule)
        )
        val toggle = ToggleTaskDoneUseCase(writeRepo = repo)

        val factory = SortStrategyFactory { option ->
            when (option) {
                SortOption.NEWEST -> TaskSortStrategy { it }
                SortOption.PRIORITY -> TaskSortStrategy { it.reversed() }
            }
        }

        val vm = TasksViewModel(
            observeTasks = observe,
            addTask = add,
            toggleTaskDone = toggle,
            sortStrategyFactory = factory
        )

        assertEquals(listOf("1", "2"), vm.state.value.tasks.map { it.id })

        vm.onSortChange(SortOption.PRIORITY)

        assertEquals(listOf("2", "1"), vm.state.value.tasks.map { it.id })
    }
}