package com.example.pockettasks.ui

import android.util.Log
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.sort.SortStrategyFactory
import com.example.pockettasks.domain.sort.impl.SortByCreatedDesc
import com.example.pockettasks.domain.sort.impl.SortByPriorityDesc
import com.example.pockettasks.domain.sort.TaskSortStrategy
import com.example.pockettasks.domain.templates.FetchTemplatesUseCase
import com.example.pockettasks.domain.templates.MarkTemplateUsedUseCase
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
    private val sortStrategyFactory: SortStrategyFactory,
    private val fetchTemplates: FetchTemplatesUseCase,
    private val markTemplateUsed: MarkTemplateUsedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state

    private var observeJob: Job? = null

    init {
        startObserving()
        refreshTemplates()
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

    fun refreshTemplates() {
        _state.update { it.copy(templatesLoading = true, templatesError = null) }
        viewModelScope.launch {
            val result = fetchTemplates.execute()
            result.fold(
                onSuccess = { list ->
                    _state.update { it.copy(templates = list, templatesLoading = false, templatesError = null) }
                },
                onFailure = { e ->
                    _state.update { it.copy(templatesLoading = false, templatesError = e.message) }
                }
            )
        }
    }

    fun onTemplateClick(templateId: String, title: String) {
        _state.update { st ->
            st.copy(
                templates = st.templates.map { if (it.id == templateId) it.copy(completed = true) else it },
                templatesError = null
            )
        }

        onInputChange(title)

        viewModelScope.launch {
            val result = markTemplateUsed.execute(templateId, used = true)
            result.onFailure { e ->
                _state.update { st ->
                    st.copy(
                        templates = st.templates.map { if (it.id == templateId) it.copy(completed = false) else it },
                        templatesError = e.message ?: "Failed to mark template as used"
                    )
                }
            }
        }
    }

}