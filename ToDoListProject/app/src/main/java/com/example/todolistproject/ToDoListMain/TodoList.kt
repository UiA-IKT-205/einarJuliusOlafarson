package com.example.todolistproject.ToDoListMain

import com.example.todolistproject.TodoListItem.ToDoItem

data class TodoList(val title:String, val items:MutableList<ToDoItem>)