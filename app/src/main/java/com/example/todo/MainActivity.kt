package com.example.todo

import android.os.Bundle
import android.text.style.UnderlineSpan
import android.util.Log
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
import androidx.compose.ui.text.style.TextDecoration
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
import kotlin.random.Random

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
                        val entity = ToDoEntity(
                            textState.value.text,
                            textState1.value.text,
                            date = System.currentTimeMillis()
                        )
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
                        items(listTodo) { currentTodo ->
                            ToDoItem(currentTodo, onCheckedChangeState = { done ->
                                Log.d("LIST_ITEM", "currentTodo: ${currentTodo.actividad}-$done")
                                toDoViewModel.update(
                                    currentTodo.copy(
                                        done = done,
                                    )
                                )
                            })

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
fun ToDoItem(toDoEntity: ToDoEntity, onCheckedChangeState: (Boolean) -> Unit) {

    val checkedState = remember { mutableStateOf(false) }
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(20),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(4.dp)) {

            Checkbox(
                checked = toDoEntity.done,
                onCheckedChange =
                onCheckedChangeState

            )
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = toDoEntity.actividad,
                    textDecoration =
                    if (toDoEntity.done) {
                        TextDecoration.combine(
                            listOf(
                                TextDecoration.LineThrough
                            )
                        )
                    } else {
                        null
                    }, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
                Text(text = toDoEntity.todoDescription)
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateString = simpleDateFormat.format(toDoEntity.date)
                Text(text = dateString)
            }


        }

    }

}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SwipeToDismissListItems(items: List<ToDoEntity>) {
    ///val toDoList = toDoViewModel.toDoList.collectAsState(initial = null)
    // This is an example of a list of dismissible items, similar to what you would see in an
    // email app. Swiping left reveals a 'delete' icon and swiping right reveals a 'done' icon.
    // The background will start as grey, but once the dismiss threshold is reached, the colour
    // will animate to red if you're swiping left or green if you're swiping right. When you let
    // go, the item will animate out of the way if you're swiping left (like deleting an email) or
    // back to its default position if you're swiping right (like marking an email as read/unread).
    LazyColumn {
        items(items) { item ->
            var unread by remember { mutableStateOf(false) }
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd) unread = !unread
                    it != DismissValue.DismissedToEnd
                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> Color.Red
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Localized description",
                            modifier = Modifier.scale(scale)
                        )
                    }
                },
                dismissContent = {
                    /* Card(
                         elevation = animateDpAsState(
                             if (dismissState.dismissDirection != null) 4.dp else 0.dp
                         ).value
                     ) {
                         ListItem(
                             text = {
                                 Text(item, fontWeight = if (unread) FontWeight.Bold else null)
                             },
                             secondaryText = { Text("Swipe me left or right!") }
                         )
                     }*/
                    //toDoList.value?.let { listTodo ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items) {
                            //ToDoItem(it.actividad, it.todoDescription, it.date)
                            //Text(text = it.todoDescription)
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value,
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
                                        Text(
                                            text = it.actividad,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(text = it.todoDescription)
                                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                                        val dateString = simpleDateFormat.format(it.date)
                                        Text(text = dateString)


                                    }
                                    /*if(checkedState.value)
                                        scope.launch {Modifier.alpha(0f)}*/
                                }

                            }
                        }
                        // }
                        //TODO( "ADD TODO LIST" )

                    }
                }

            )
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