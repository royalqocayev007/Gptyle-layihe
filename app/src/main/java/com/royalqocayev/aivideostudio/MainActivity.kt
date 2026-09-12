package com.royalqocayev.aivideostudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Scene(
    val number: Int,
    val title: String,
    val description: String,
    val duration: Int
)

data class Project(
    val title: String,
    val idea: String,
    val script: String = "",
    val scenes: List<Scene> = emptyList()
)

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

    var screen by remember { mutableStateOf("home") }

    var project by remember {
        mutableStateOf<Project?>(null)
    }

    when (screen) {

        "home" -> HomeScreen(
            project = project,
            onNewProject = {
                screen = "new"
            },
            onOpenProject = {
                screen = "studio"
            }
        )

        "new" -> NewProjectScreen(
            onBack = {
                screen = "home"
            },
            onCreate = { title, idea ->
                project = Project(
                    title = title,
                    idea = idea
                )
                screen = "studio"
            }
        )

        "studio" -> project?.let { current ->

            StudioScreen(
                project = current,
                onBack = {
                    screen = "home"
                },
                onUpdate = {
                    project = it
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    project: Project?,
    onNewProject: () -> Unit,
    onOpenProject: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            "AI Video Studio",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        Text("Fikirdən videoya — öz AI video studiyan.")

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onNewProject,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("＋ Yeni layihə")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Son layihə",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        if (project == null) {

            Text("Hələ layihə yaradılmayıb.")

        } else {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    Modifier.padding(16.dp)
                ) {

                    Text(
                        project.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(project.idea)

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Səhnələr: ${project.scenes.size}"
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = onOpenProject,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Layihəni aç")
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

    var title by remember { mutableStateOf("") }
    var idea by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextButton(onClick = onBack) {
            Text("← Geri")
        }

        Text(
            "Yeni layihə",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Layihənin adı") }
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = idea,
            onValueChange = { idea = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            label = { Text("Video ideyası") },
            placeholder = {
                Text("Məsələn: İnsanların artıq müharibə etmədiyi bir dünyada gözlənilməz hadisə baş verir...")
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && idea.isNotBlank()) {
                    onCreate(
                        title.trim(),
                        idea.trim()
                    )
                }
            },
            enabled = title.isNotBlank() && idea.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Layihəni yarat")
        }
    }
}

@Composable
fun StudioScreen(
    project: Project,
    onBack: () -> Unit,
    onUpdate: (Project) -> Unit
) {

    var tab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextButton(onClick = onBack) {
                Text("←")
            }

            Text(
                project.title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                "${project.scenes.size} səhnə",
                modifier = Modifier.padding(top = 14.dp)
            )
        }

        TabRow(selectedTabIndex = tab) {

            Tab(
                selected = tab == 0,
                onClick = { tab = 0 },
                text = { Text("Ssenari") }
            )

            Tab(
                selected = tab == 1,
                onClick = { tab = 1 },
                text = { Text("Səhnələr") }
            )

            Tab(
                selected = tab == 2,
                onClick = { tab = 2 },
                text = { Text("AI") }
            )
        }

        when (tab) {

            0 -> ScriptEditor(
                project = project,
                onUpdate = onUpdate
            )

            1 -> SceneEditor(
                project = project,
                onUpdate = onUpdate
            )

            2 -> AIDirectorScreen(
                project = project,
                onUpdate = onUpdate
            )
        }
    }
}

@Composable
fun ScriptEditor(
    project: Project,
    onUpdate: (Project) -> Unit
) {

    var script by remember(project.script) {
        mutableStateOf(project.script)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Ssenari",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "İdeya: ${project.idea}"
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = script,
            onValueChange = {
                script = it
                onUpdate(
                    project.copy(script = it)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            label = {
                Text("Ssenarini yaz və ya AI ilə yarat")
            }
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {

                val generated = """
                    ${project.title}

                    GİRİŞ

                    ${project.idea}

                    Hadisələr gözlənilməz şəkildə inkişaf edir.
                    Baş qəhrəman baş verənlərin arxasındakı həqiqəti
                    anlamağa çalışır.

                    İNKİŞAF

                    Qarşısına yeni suallar çıxır.
                    Hər cavab isə daha böyük bir sirrə aparır.

                    SONLUQ

                    Əsl həqiqət ortaya çıxır.
                    İnsan dəyişməli olduğunu anlayır.
                """.trimIndent()

                script = generated

                onUpdate(
                    project.copy(script = generated)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("✦ AI ilə ssenari yarat")
        }
    }
}

@Composable
fun SceneEditor(
    project: Project,
    onUpdate: (Project) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Səhnə lövhəsi",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {

                val nextNumber = project.scenes.size + 1

                val newScene = Scene(
                    number = nextNumber,
                    title = "Yeni səhnə $nextNumber",
                    description = "Bu səhnədə baş verənləri yaz...",
                    duration = 5
                )

                onUpdate(
                    project.copy(
                        scenes = project.scenes + newScene
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("＋ Səhnə əlavə et")
        }

        Spacer(Modifier.height(12.dp))

        if (project.scenes.isEmpty()) {

            Text(
                "Hələ səhnə yoxdur.\nSəhnə əlavə et və ya AI ilə yarat."
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                itemsIndexed(project.scenes) { index, scene ->

                    SceneCard(
                        scene = scene,
                        onChange = { changed ->

                            val updated =
                                project.scenes.toMutableList()

                            updated[index] = changed

                            onUpdate(
                                project.copy(
                                    scenes = updated
                                )
                            )
                        },
                        onDelete = {

                            val updated =
                                project.scenes
                                    .filterIndexed { i, _ ->
                                        i != index
                                    }
                                    .mapIndexed { i, item ->
                                        item.copy(
                                            number = i + 1
                                        )
                                    }

                            onUpdate(
                                project.copy(
                                    scenes = updated
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SceneCard(
    scene: Scene,
    onChange: (Scene) -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            Modifier.padding(14.dp)
        ) {

            Text(
                "SƏHNƏ ${scene.number}",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = scene.title,
                onValueChange = {
                    onChange(
                        scene.copy(title = it)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Səhnənin adı") }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = scene.description,
                onValueChange = {
                    onChange(
                        scene.copy(description = it)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Səhnənin təsviri") }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = scene.duration.toString(),
                onValueChange = {
                    it.toIntOrNull()?.let { seconds ->
                        onChange(
                            scene.copy(duration = seconds)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Müddət (saniyə)") }
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onDelete
            ) {
                Text("🗑 Səhnəni sil")
            }
        }
    }
}

@Composable
fun AIDirectorScreen(
    project: Project,
    onUpdate: (Project) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "AI Director",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Burada sonradan müxtəlif AI provider-lər qoşulacaq."
        )

        Spacer(Modifier.height(20.dp))

        Card(
            Modifier.fillMaxWidth()
        ) {

            Column(
                Modifier.padding(16.dp)
            ) {

                Text("Hazırkı layihə")

                Spacer(Modifier.height(8.dp))

                Text("Ad: ${project.title}")

                Text("Səhnələr: ${project.scenes.size}")

                Text(
                    "Ssenari: ${
                        if (project.script.isBlank())
                            "hazır deyil"
                        else
                            "hazırdır"
                    }"
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {

                val generatedScenes = listOf(
                    Scene(
                        1,
                        "Giriş",
                        "Kamera dünyanı və əsas vəziyyəti göstərir.",
                        6
                    ),
                    Scene(
                        2,
                        "İnkişaf",
                        "Əsas hadisə başlayır və qəhrəman qərar verməli olur.",
                        8
                    ),
                    Scene(
                        3,
                        "Dönüş nöqtəsi",
                        "Gizli həqiqətin ilk hissəsi ortaya çıxır.",
                        8
                    ),
                    Scene(
                        4,
                        "Sonluq",
                        "Hekayənin əsas fikri emosional şəkildə tamamlanır.",
                        7
                    )
                )

                onUpdate(
                    project.copy(
                        scenes = generatedScenes
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("✦ AI ilə səhnələri yarat")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Növbəti mərhələdə bu düymə real AI API-lərinə bağlanacaq."
        )
    }
}
