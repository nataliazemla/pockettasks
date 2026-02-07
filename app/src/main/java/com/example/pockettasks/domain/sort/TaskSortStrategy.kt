package com.example.pockettasks.domain.sort

import com.example.pockettasks.domain.model.Task

fun interface TaskSortStrategy {
    fun sort(tasks: List<Task>): List<Task>
}