package com.example.pockettasks.data.templates

import com.apollographql.apollo.ApolloClient
import com.example.pockettasks.domain.templates.Template
import com.example.pockettasks.domain.templates.TemplatesReadRepository
import com.example.pockettasks.graphql.TemplatesQuery

class ApolloTemplatesRepository(
    private val apollo: ApolloClient
) : TemplatesReadRepository {

    override suspend fun fetchTemplates(page: Int, limit: Int): Result<List<Template>> = runCatching {
        val response = apollo.query(
            TemplatesQuery(
                page = page,
                limit = limit
            )
        ).execute()

        val errors = response.errors
        if (!errors.isNullOrEmpty()) {
            error(errors.joinToString { it.message })
        }

        val data = response.data?.todos?.data.orEmpty()
        data.mapNotNull { todo ->
            val id = todo?.todoTemplate?.id ?: return@mapNotNull null
            val title = todo.todoTemplate.title ?: return@mapNotNull null
            Template(id = id, title = title, completed = todo.todoTemplate.completed ?: false)
        }
    }
}