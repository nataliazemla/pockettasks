package com.example.pockettasks.domain.repo

import com.example.pockettasks.domain.model.Task

interface TaskWriteRepository {
    suspend fun add(task: Task)
    suspend fun update(task: Task)
}