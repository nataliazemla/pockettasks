package com.example.pockettasks.ui

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.sort.SortStrategyFactory
import com.example.pockettasks.domain.sort.impl.SortByCreatedDesc
import com.example.pockettasks.domain.sort.impl.SortByPriorityDesc
import com.example.pockettasks.domain.sort.TaskSortStrategy
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val observeTasks: ObserveTasksUseCase,
    private val addTask: AddTaskUseCase,
    private val toggleTaskDone: ToggleTaskDoneUseCase,
    private val sortStrategyFactory: SortStrategyFactory
) : ViewModel() {

    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state

    private var observeJob: Job? = null

    init { startObserving() }

    fun onInputChange(value: String) {
        _state.update { it.copy(input = value, error = null) }
    }

    fun onAddClick() {
        val title = state.value.input
        viewModelScope.launch {
            val result = addTask.execute(title)
            result.fold(
                onSuccess = { _state.update { it.copy(input = "", error = null) } },
                onFailure = { e -> _state.update { it.copy(error = e.message) } }
            )
        }
    }

    fun onToggle(task: Task) {
        viewModelScope.launch { toggleTaskDone.execute(task) }
    }

    fun onSortChange(option: SortOption) {
        _state.update { it.copy(sortOption = option) }
        startObserving()
    }

    private fun startObserving() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            val strategy = sortStrategyFactory.create(state.value.sortOption)
            observeTasks.execute(sort = strategy)
                .collect { tasks -> _state.update { it.copy(tasks = tasks) } }
        }
    }

}