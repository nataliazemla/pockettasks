package com.example.pockettasks.domain.templates

interface TemplatesReadRepository {
    suspend fun fetchTemplates(page: Int, limit: Int): Result<List<Template>>
}