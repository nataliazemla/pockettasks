package com.example.pockettasks.domain.repo

import com.example.pockettasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskReadRepository {
    fun observeTasks(): Flow<List<Task>>
}