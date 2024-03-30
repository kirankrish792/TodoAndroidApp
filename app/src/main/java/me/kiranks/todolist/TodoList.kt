package me.kiranks.todolist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class TodoItem(
    val id : Int,
    var title : String,
    var quantity : Int,
    var isEditing : Boolean = false
)

@Composable
fun TodoListApp(){
    var tItems by remember { mutableStateOf(listOf<TodoItem>()) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog=true }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
           items(tItems) {
               if(it.isEditing){
                   TodoEditItems(item = it, onSave = { title, quantity ->
                       tItems = tItems.map { item ->
                           if(item.id == it.id){
                               item.copy(title = title, quantity = quantity, isEditing = false)
                           }else{
                               item
                           }
                       }
                   }, onCancel = {
                       tItems = tItems.map { item ->
                           if(item.id == it.id){
                               item.copy(isEditing = false)
                            }else{
                                 item
                           }
                       }
                   })
               }
                else{
                     TodoListItems(item = it, onEdit = {
                          tItems = tItems.map { item ->
                                 item.copy(isEditing = item.id == it.id)
                          }
                     }, onDelete = {
                          tItems = tItems.filter { item -> item.id != it.id }
                     })
                }
           }
        }
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog=false }, confirmButton={
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val context = LocalContext.current
                Button(onClick = {
                    if(itemName.isEmpty() || itemQuantity.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please enter item name and quantity",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                            tItems = tItems + TodoItem(
                            id = tItems.size + 1,
                            title = itemName,
                            quantity = itemQuantity.toIntOrNull() ?: 1
                        )
                        showDialog=false
                        itemName = ""
                        itemQuantity = ""
                    }
                }) {
                    Text(text = "Add")
                }
                Button(onClick = { showDialog=false }) {
                    Text(text = "Cancel")
                }
            }
        },
            title = { Text(text ="Add Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
        })
    }
}


@Composable
fun TodoEditItems(
    item:TodoItem,
    onSave:(String,Int) -> Unit,
    onCancel:() -> Unit = {}
){
    var itemName by remember { mutableStateOf(item.title) }
    var itemQuantity by remember { mutableStateOf(item.quantity.toString()) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(value = itemName, onValueChange = { itemName = it }, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),)
            BasicTextField(value = itemQuantity, onValueChange = { itemQuantity = it }, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))
        }
        Button(onClick = {
            onSave(itemName, itemQuantity.toIntOrNull() ?: 1)
        },) {
            Text(text = "Save")
        }
        Button(onClick = onCancel) {
            Text(text = "Cancel")
        }
    }
}


@Composable
fun TodoListItems (
    item:TodoItem,
    onEdit:() -> Unit,
    onDelete:() -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
        ){
        Text(text =item.title, modifier = Modifier
            .weight(1f)
            .padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}
