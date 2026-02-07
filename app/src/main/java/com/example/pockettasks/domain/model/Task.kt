package com.example.pockettasks.domain.model

data class Task(
    val id: String,
    val title: String,
    val isDone: Boolean,
    val priority: Priority,
    val createdAtMillis: Long
)
