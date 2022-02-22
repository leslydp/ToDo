package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.data.source.local.entity.ToDoEntity
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.viewmodel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ToDOScreen()
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ToDOScreen(
    toDoViewModel: ToDoViewModel = hiltViewModel()
) {
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val toDoList = toDoViewModel.toDoList.collectAsState(initial = null)

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                val textState = remember { mutableStateOf(TextFieldValue()) }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Actividad") },
                    value = textState.value,
                    onValueChange = { textState.value = it }
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Actividad") },
                    value = textState.value,
                    onValueChange = { textState.value = it }
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Actividad") },
                    value = textState.value,
                    onValueChange = { textState.value = it }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO  Add new Entity*/ }) {
                    Text(text = "ADD")
                }
            }
        }
    ) {
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBar(title = { Text("To Do List") }) },
            floatingActionButtonPosition = FabPosition.End,
            //isFloatingActionButtonDocked = true,
            floatingActionButton = {
                if (!state.isVisible)
                    FloatingActionButton(onClick = {

                        //TODO("ADD ENTITY")
                        /*val entity = ToDoEntity("Run", "akjsfhaf", date = System.currentTimeMillis())
                        toDoViewModel.insert(entity)*/
                        scope.launch { state.show() }


                    }) { Text("+") }
            },

            content = {


                toDoList.value?.let { listTodo ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listTodo) {
                            Text(text = it.actividad)
                        }
                    }
                    //TODO( "ADD TODO LIST" )

                }
            },
            // bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoTheme {
        //Greeting("Android")
    }
}