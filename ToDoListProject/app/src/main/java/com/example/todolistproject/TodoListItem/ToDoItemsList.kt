package com.example.todolistproject.TodoListItem

data class ToDoItem(val description:String, val isDone:Boolean){

    // Updated data class to be able to toggle its own boolean
    // Helps with binding to the view
    fun booleanFlip(){
        isDone != isDone
    }

}