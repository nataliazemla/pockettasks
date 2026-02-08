package com.example.pockettasks.domain.sort.impl

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.TaskSortStrategy

class SortByCreatedDesc : TaskSortStrategy {
    override fun sort(tasks: List<Task>): List<Task> =
        tasks.sortedByDescending { it.createdAtMillis }
}