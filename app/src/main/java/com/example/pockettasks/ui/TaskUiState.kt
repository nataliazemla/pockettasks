package com.example.pockettasks.ui

import com.example.pockettasks.domain.model.Task

data class TaskUiState(
    val input: String = "",
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)