package com.example.pockettasks.domain.validation

class NonBlankTitleRule : TaskTitleRule {
    override fun validate(title: String): String? =
        if (title.isBlank()) "Title cannot be blank" else null
}