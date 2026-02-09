package com.example.pockettasks.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettasks.domain.sort.SortOption

@Composable
fun TasksScreen(vm: TasksViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pocket Tasks", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.sortOption == SortOption.NEWEST,
                onClick = { vm.onSortChange(option = SortOption.NEWEST) },
                label = { Text("Newest") }
            )
            FilterChip(
                selected = state.sortOption == SortOption.PRIORITY,
                onClick = { vm.onSortChange(option = SortOption.PRIORITY) },
                label = { Text("Priority") }
            )
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.input,
            onValueChange = vm::onInputChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("New task") },
            isError = state.error != null,
            singleLine = true
        )
        state.error?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = vm::onAddClick) { Text("Add") }

        Spacer(Modifier.height(16.dp))

        Text("Templates (GraphQL)", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = vm::refreshTemplates, enabled = !state.templatesLoading) {
                Text(if (state.templatesLoading) "Loading..." else "Refresh")
            }
            state.templatesError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }

        Spacer(Modifier.height(8.dp))
        state.templates.take(6).forEach { t ->
            AssistChip(
                onClick = { vm.onTemplateClick(t.title) },
                label = { Text(t.title) }
            )
            Spacer(Modifier.height(6.dp))
        }

        LazyColumn(Modifier.fillMaxSize()) {
            items(state.tasks, key = { it.id }) { task ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(task.title, modifier = Modifier.weight(1f))
                    Checkbox(checked = task.isDone, onCheckedChange = { vm.onToggle(task) })
                }
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}