package com.example.pockettasks.domain.sort

import com.example.pockettasks.domain.model.Task

class SortByCreatedDesc : TaskSortStrategy {
    override fun sort(tasks: List<Task>): List<Task> =
        tasks.sortedByDescending { it.createdAtMillis }
}