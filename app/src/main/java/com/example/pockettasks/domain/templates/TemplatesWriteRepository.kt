package com.example.pockettasks.domain.templates

interface TemplatesWriteRepository {
    suspend fun markUsed(id: String, used: Boolean): Result<Unit>
}