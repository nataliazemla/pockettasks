package com.example.pockettasks.domain.sort.impl

import com.example.pockettasks.domain.model.Task
import com.example.pockettasks.domain.sort.TaskSortStrategy

class SortByPriorityDesc : TaskSortStrategy {
    override fun sort(tasks: List<Task>): List<Task> =
        tasks.sortedWith(
            compareByDescending<Task> { it.priority }
                .thenByDescending { it.createdAtMillis }
        )
}