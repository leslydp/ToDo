package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.data.source.local.entity.ToDoEntity
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.viewmodel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

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
@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
fun ToDOScreen(
    toDoViewModel: ToDoViewModel = hiltViewModel()
) {
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val toDoList = toDoViewModel.toDoList.collectAsState(initial = null)
    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                var textState = remember { mutableStateOf(TextFieldValue()) }
                val textState1 = remember { mutableStateOf(TextFieldValue()) }
                val textState2 = remember { mutableStateOf(TextFieldValue()) }

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Actividad") },
                    value = textState.value,
                    onValueChange = { textState.value = it }
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Descripción") },
                    value = textState1.value,
                    onValueChange = { textState1.value = it }
                )

                /*TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Actividad") },
                    value = textState2.value,
                    onValueChange = { textState2.value = it }
                )*/

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val entity = ToDoEntity(textState.value.text, textState1.value.text, date = System.currentTimeMillis())
                        toDoViewModel.insert(entity)
                       // var sheetState = state.currentValue
                        scope.launch { state.hide() }
                        keyboardController?.hide()

                    }) {
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
                            ToDoItem(it.actividad, it.todoDescription, it.date)
                            //Text(text = it.todoDescription)
                            //SwipeToDismissListItems(items = listTodo)
                        }
                    }
                    //TODO( "ADD TODO LIST" )

                }
            },
            // bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
        )

    }
}

@Composable
fun ToDoItem(title: String = "actividad", description: String = "descripcion", date: Long ) {
    val scope = rememberCoroutineScope()
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(20),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            val checkedState = remember { mutableStateOf(false) }
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it },
            )
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = description)
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateString = simpleDateFormat.format(date)
                Text(text = dateString)



            }
            /*if(checkedState.value)
                scope.launch {Modifier.alpha(0f)}*/
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoTheme {
        //Greeting("Android")
    }
}