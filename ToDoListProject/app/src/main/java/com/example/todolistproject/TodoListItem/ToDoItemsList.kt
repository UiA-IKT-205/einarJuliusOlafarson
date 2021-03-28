package com.example.todolistproject.TodoListItem

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToDoItem(val description:String, var isDone:Boolean): Parcelable {
    // Updated data class to be able to toggle its own boolean
    // Helps with binding to the view

    fun returnBool(): Boolean{
        return isDone
    }

}