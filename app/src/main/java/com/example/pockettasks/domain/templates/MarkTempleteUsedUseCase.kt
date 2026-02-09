package com.example.pockettasks.domain.templates

class MarkTemplateUsedUseCase(
    private val repo: TemplatesWriteRepository
) {
    suspend fun execute(id: String, used: Boolean = true): Result<Unit> =
        repo.markUsed(id, used)
}