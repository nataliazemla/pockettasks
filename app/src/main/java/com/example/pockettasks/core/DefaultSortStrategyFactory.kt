package com.example.pockettasks.core

import com.example.pockettasks.domain.sort.SortOption
import com.example.pockettasks.domain.sort.SortStrategyFactory
import com.example.pockettasks.domain.sort.TaskSortStrategy
import com.example.pockettasks.domain.sort.impl.SortByCreatedDesc
import com.example.pockettasks.domain.sort.impl.SortByPriorityDesc

class DefaultSortStrategyFactory : SortStrategyFactory {
    override fun create(option: SortOption): TaskSortStrategy =
        when (option) {
            SortOption.NEWEST -> SortByCreatedDesc()
            SortOption.PRIORITY -> SortByPriorityDesc()
        }
}