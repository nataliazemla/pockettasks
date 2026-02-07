package com.example.pockettasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pockettasks.core.AppGraph
import com.example.pockettasks.ui.TasksScreen
import com.example.pockettasks.ui.TasksViewModel
import com.example.pockettasks.ui.theme.PocketTasksTheme

class MainActivity : ComponentActivity() {

    private val vmFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TasksViewModel(
                observeTasks = AppGraph.observeTasksUseCase,
                addTask = AppGraph.addTaskUseCase,
                toggleTaskDone = AppGraph.toggleTaskDoneUseCase
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
