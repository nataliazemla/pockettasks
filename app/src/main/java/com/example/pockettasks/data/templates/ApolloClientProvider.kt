package com.example.pockettasks.data.templates

import com.apollographql.apollo.ApolloClient

object ApolloClientProvider {
    fun create(): ApolloClient =
        ApolloClient.Builder()
            .serverUrl("https://graphqlzero.almansi.me/api")
            .build()
}