package com.example.pockettasks.domain.sort

fun interface SortStrategyFactory {
    fun create(option: SortOption): TaskSortStrategy
}