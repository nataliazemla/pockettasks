package com.example.pockettasks.domain.validation

fun interface TaskTitleRule {
    fun validate(title: String): String? // null = ok, else error message
}