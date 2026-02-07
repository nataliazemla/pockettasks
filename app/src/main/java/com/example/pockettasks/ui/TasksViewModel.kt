package com.example.pockettasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.usecase.AddTaskUseCase
import com.example.pockettasks.domain.usecase.ObserveTasksUseCase
import com.example.pockettasks.domain.usecase.ToggleTaskDoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val observeTasks: ObserveTasksUseCase,
    private val addTask: AddTaskUseCase,
    private val toggleTaskDone: ToggleTaskDoneUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state

    init {
        viewModelScope.launch {
            observeTasks.execute().collect { tasks ->
                    _state.update { it.copy(tasks = tasks) }
            }
        }
    }

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
}