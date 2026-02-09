package com.example.pockettasks.data.templates

import com.apollographql.apollo.ApolloClient
import com.example.pockettasks.domain.templates.Template
import com.example.pockettasks.domain.templates.TemplatesReadRepository
import com.example.pockettasks.domain.templates.TemplatesWriteRepository
import com.example.pockettasks.graphql.MarkTemplateUsedMutation
import com.example.pockettasks.graphql.TemplatesQuery

class ApolloTemplatesRepository(
    private val apollo: ApolloClient
) : TemplatesReadRepository, TemplatesWriteRepository {

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

    override suspend fun markUsed(id: String, used: Boolean): Result<Unit> {
        return try {
            val response = apollo.mutation(
                MarkTemplateUsedMutation(
                    id = id,
                    completed = used
                )
            ).execute()

            if (response.hasErrors()) {
                val msg = response.errors?.joinToString("; ") { it.message } ?: "GraphQL error"
                return Result.failure(IllegalStateException(msg))
            }
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}