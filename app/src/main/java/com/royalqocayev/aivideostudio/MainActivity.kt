package com.royalqocayev.aivideostudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Project(
    val title: String,
    val idea: String
)

enum class StageStatus {
    DRAFT,
    RUNNING,
    READY,
    APPROVED,
    FAILED,
    NEEDS_EDIT
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AIVideoStudioApp()
                }
            }
        }
    }
}

@Composable
fun AIVideoStudioApp() {

    var showNewProject by remember { mutableStateOf(false) }

    val projects = remember {
        mutableStateListOf<Project>()
    }

    if (showNewProject) {

        NewProjectScreen(
            onBack = {
                showNewProject = false
            },
            onCreate = { title, idea ->
                projects.add(Project(title, idea))
                showNewProject = false
            }
        )

    } else {

        HomeScreen(
            projects = projects,
            onNewProject = {
                showNewProject = true
            }
        )
    }
}

@Composable
fun HomeScreen(
    projects: List<Project>,
    onNewProject: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "AI Video Studio",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fikirdən videoya — mərhələ-mərhələ AI video studiyası"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNewProject,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("＋ Yeni layihə")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Layihələr",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (projects.isEmpty()) {

            Text(
                text = "Hələ layihə yoxdur.\nİlk videonu yaratmaq üçün yeni layihə aç."
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(projects) { project ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = project.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(project.idea)

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("Status: DRAFT")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewProjectScreen(
    onBack: () -> Unit,
    onCreate: (String, String) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var idea by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextButton(onClick = onBack) {
                Text("Geri")
            }

            Text(
                text = "Yeni layihə",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Layihənin adı")
            },
            placeholder = {
                Text("Məsələn: Qiyamətdən 33 gün öncə")
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = idea,
            onValueChange = { idea = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            label = {
                Text("Video ideyası")
            },
            placeholder = {
                Text("Videoda nə baş verməsini istəyirsən?")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && idea.isNotBlank()) {
                    onCreate(title.trim(), idea.trim())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && idea.isNotBlank()
        ) {
            Text("Layihəni yarat")
        }
    }
}
