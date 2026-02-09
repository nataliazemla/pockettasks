package com.example.pockettasks.data

import com.example.pockettasks.domain.templates.Template
import com.example.pockettasks.domain.templates.TemplatesReadRepository
import com.example.pockettasks.domain.templates.TemplatesWriteRepository

class FakeTemplatesReadRepository(
    private val result: Result<List<Template>>
) : TemplatesReadRepository {
    override suspend fun fetchTemplates(page: Int, limit: Int): Result<List<Template>> = result
}

class FakeTemplatesWriteRepository : TemplatesWriteRepository {
    var lastId: String? = null
    var lastUsed: Boolean? = null
    var result: Result<Unit> = Result.success(Unit)

    override suspend fun markUsed(id: String, used: Boolean): Result<Unit> {
        lastId = id
        lastUsed = used
        return result
    }
}