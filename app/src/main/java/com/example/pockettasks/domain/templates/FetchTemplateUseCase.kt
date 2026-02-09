package com.example.pockettasks.domain.templates

class FetchTemplatesUseCase(
    private val repo: TemplatesReadRepository
) {
    suspend fun execute(page: Int = 1, limit: Int = 10): Result<List<Template>> =
        repo.fetchTemplates(page, limit)
}