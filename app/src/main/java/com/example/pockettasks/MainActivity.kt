package com.example.pockettasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pockettasks.core.AppGraph
import com.example.pockettasks.ui.TasksScreen
import com.example.pockettasks.ui.TasksViewModel

class MainActivity : ComponentActivity() {

    private val vmFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TasksViewModel(
                observeTasks = AppGraph.observeTasksUseCase,
                addTask = AppGraph.addTaskUseCase,
                toggleTaskDone = AppGraph.toggleTaskDoneUseCase,
                sortStrategyFactory = AppGraph.sortFactory
            ) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val vm = androidx.lifecycle.viewmodel.compose.viewModel<TasksViewModel>(factory = vmFactory)
                TasksScreen(vm = vm)
            }
        }
    }
}
