package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.data.source.local.entity.ToDoEntity
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.viewmodel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint

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
fun ToDOScreen(toDoViewModel: ToDoViewModel = hiltViewModel()) {

    val toDoList = toDoViewModel.toDoList.collectAsState(initial = null)
    val materialBlue700 = Color(0xFF1976D2)
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("To Do List") }, backgroundColor = materialBlue700) },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(onClick = {

                //TODO("ADD ENTITY")
                val entity = ToDoEntity("Run", "akjsfhaf", date = System.currentTimeMillis())
                toDoViewModel.insert(entity)

            }) {
                Text("+")
            }
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
        bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
    )


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoTheme {
        //Greeting("Android")
    }
}