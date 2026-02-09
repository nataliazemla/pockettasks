package com.example.pockettasks.ui

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.templates.Template

data class TaskUiState(
    val input: String = "",
    val tasks: List<Task> = emptyList(),
    val error: String? = null,
    val sortOption: SortOption = SortOption.NEWEST,
    val templates: List<Template> = emptyList(),
    val templatesLoading: Boolean = false,
    val templatesError: String? = null
)